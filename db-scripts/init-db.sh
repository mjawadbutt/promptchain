#!/bin/bash
set -e

echo "Running idempotent database initialization..."

# Set PGPASSWORD for psql command
# This variable is expected to be passed into the container's environment
export PGPASSWORD="$POSTGRES_SUPER_PASSWORD"

# Execute idempotent SQL to create user and grant permissions
# This assumes 'mydb' is already created by POSTGRES_DB env variable for the main Postgres service.
psql -h "$POSTGRES_HOST" -U postgres -d "$POSTGRES_DB" -v ON_ERROR_STOP=1 <<EOF
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename = 'myappuser_prod') THEN
    CREATE USER myappuser_prod WITH PASSWORD 'myappsecret_prod';
    GRANT CONNECT ON DATABASE mydb TO myappuser_prod;
    GRANT USAGE ON SCHEMA public TO myappuser_prod;
    ALTER DEFAULT PRIVILEGES FOR USER myappuser_prod IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO myappuser_prod;
    RAISE NOTICE 'User myappuser_prod created.';
  ELSE
    RAISE NOTICE 'User myappuser_prod already exists, skipping creation.';
  END IF;
END
\$\$;
EOF

echo "Database initialization script finished."
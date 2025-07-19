#!/bin/sh
set -e

echo "Running idempotent database initialization..."

# Set PGPASSWORD for psql command. This will be consumed from the environment.
export PGPASSWORD="${POSTGRES_SUPER_USER_PASSWORD}"

echo "Waiting for PostgreSQL to be ready at ${POSTGRES_HOST_INTERNAL}:${POSTGRES_PORT:-5432}..."
# Loop until pg_isready reports success (exit code 0)
# -h: host
# -p: port (default 5432, you might need to pass it if different)
# -U: username for the check
# -d: database name for the check (can be 'postgres' or your actual DB)
until pg_isready -h "${POSTGRES_HOST_INTERNAL}" -p "5432" -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB_NAME}" > /dev/null 2>&1; do
  echo "PostgreSQL is still unavailable - sleeping (check will retry every 2 seconds)"
  sleep 2
done
echo "PostgreSQL is up and running! Proceeding with user creation."

# Execute idempotent SQL to create user and grant permissions
psql -h "${POSTGRES_HOST_INTERNAL}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB_NAME}" -v ON_ERROR_STOP=1 <<EOF
DO \$\$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_user WHERE usename = '${APP_DB_USER_NAME}') THEN
    CREATE USER ${APP_DB_USER_NAME} WITH PASSWORD '${APP_DB_USER_PASSWORD}';
    RAISE NOTICE 'User ${APP_DB_USER_NAME} created.';
  ELSE
    RAISE NOTICE 'User ${APP_DB_USER_NAME} already exists, skipping creation.';
  END IF;
END
\$\$;

-- Grant CONNECT privilege on the specific database
GRANT CONNECT ON DATABASE "${APP_DB_NAME}" TO ${APP_DB_USER_NAME};

-- Grant CREATE privilege on the database itself (needed to create schemas, extensions, etc.)
GRANT CREATE ON DATABASE "${APP_DB_NAME}" TO ${APP_DB_USER_NAME};

-- Grant USAGE and CREATE privileges on the public schema
GRANT USAGE, CREATE ON SCHEMA public TO ${APP_DB_USER_NAME};

-- Set default privileges for objects created by APP_DB_USER_NAME in the public schema.
ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES ON TABLES TO ${APP_DB_USER_NAME};

ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, USAGE ON SEQUENCES TO ${APP_DB_USER_NAME};

ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT EXECUTE ON FUNCTIONS TO ${APP_DB_USER_NAME};

-- Explicitly grant ALL PRIVILEGES on existing tables, sequences, and views in the public schema.
-- This ensures objects created BEFORE this script runs get necessary permissions if needed.
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO ${APP_DB_USER_NAME};

EOF

echo "Database initialization script finished successfully."

# Unset PGPASSWORD for security
unset PGPASSWORD
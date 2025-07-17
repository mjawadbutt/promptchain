#!/bin/bash
set -e

echo "Running idempotent database initialization..."

# Set PGPASSWORD for psql command
# This variable is expected to be passed into the container's environment
export PGPASSWORD="${POSTGRES_SUPER_USER_PASSWORD}"

# Execute idempotent SQL to create user and grant permissions
psql -h "${POSTGRES_HOST}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB_NAME}" -v ON_ERROR_STOP=1 <<EOF
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
GRANT CONNECT ON DATABASE "${POSTGRES_DB_NAME}" TO ${APP_DB_USER_NAME};

-- Grant CREATE privilege on the database itself (needed to create schemas, extensions, etc.)
-- This is important for Liquibase to create its internal tables (e.g., DATABASECHANGELOG)
GRANT CREATE ON DATABASE "${POSTGRES_DB_NAME}" TO ${APP_DB_USER_NAME};

-- Grant USAGE and CREATE privileges on the public schema
-- USAGE: Allows access to objects within the schema.
-- CREATE: Allows creation of new objects within the schema (tables, views, sequences, etc.).
GRANT USAGE, CREATE ON SCHEMA public TO ${APP_DB_USER_NAME};

-- Set default privileges for objects created by APP_DB_USER_NAME in the public schema.
-- This ensures that any new tables, sequences, or functions created by Liquibase (or the app)
-- will automatically have the necessary permissions for the APP_DB_USER_NAME.
ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES ON TABLES TO ${APP_DB_USER_NAME};

ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, USAGE ON SEQUENCES TO ${APP_DB_USER_NAME};

ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT EXECUTE ON FUNCTIONS TO ${APP_DB_USER_NAME};

-- Explicitly grant ALL PRIVILEGES on existing tables, sequences, and views in the public schema.
-- This is important if there are pre-existing objects that Liquibase needs to modify or drop.
-- Note: ALL PRIVILEGES for TABLES includes SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES, TRIGGER.
-- For VIEWS, it typically includes SELECT.
-- For SEQUENCES, it typically includes SELECT, USAGE, UPDATE.
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO ${APP_DB_USER_NAME};
-- If you have pre-existing views that Liquibase needs to manage (e.g., drop and recreate)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ${APP_DB_USER_NAME}; -- Views are considered "table-like" for this grant.

EOF

echo "Database initialization script finished."
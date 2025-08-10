#!/bin/bash
# create-promptchain-db-user.sh
# This script waits for PostgreSQL to be ready, then creates a new application database user
# and grants privileges. Designed to be idempotent and short-lived.

# Exit immediately if a command exits with a non-zero status (-e)
# Print commands and their arguments as they are executed (-x)
set -euo pipefail
set -x

echo "--- Starting create-app-db-user script ---"

# Ensure all required environment variables are set
REQUIRED_VARS="POSTGRES_HOST POSTGRES_PORT POSTGRES_DB POSTGRES_SUPER_USER_NAME POSTGRES_SUPER_USER_PASSWORD APP_DB_NAME APP_DB_USER_NAME APP_DB_USER_PASSWORD"
for var in $REQUIRED_VARS; do
  if [ -z "${!var:-}" ]; then
    echo "ERROR: Required environment variable $var is not set" >&2
    exit 1
  fi
done

# Set PGPASSWORD for pg_isready and psql to authenticate as superuser
export PGPASSWORD="${POSTGRES_SUPER_USER_PASSWORD}"

POSTGRES_PORT="${POSTGRES_PORT:-5432}"

echo "Waiting for PostgreSQL at ${POSTGRES_HOST}:${POSTGRES_PORT} as superuser ${POSTGRES_SUPER_USER_NAME}..."

# Wait for PostgreSQL to be ready
MAX_RETRIES=10
RETRY_INTERVAL=3
CURRENT_RETRY=0

until pg_isready -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB}" > /dev/null 2>&1; do
  CURRENT_RETRY=$((CURRENT_RETRY + 1))
  if [ "$CURRENT_RETRY" -gt "$MAX_RETRIES" ]; then
    echo "ERROR: PostgreSQL not ready after $MAX_RETRIES retries. Aborting." >&2
    unset PGPASSWORD # Unset password before exiting
    exit 1
  fi
  echo "PostgreSQL is still unavailable (${POSTGRES_HOST}:${POSTGRES_PORT}) - retrying in ${RETRY_INTERVAL}s (attempt ${CURRENT_RETRY}/${MAX_RETRIES})"
  sleep "$RETRY_INTERVAL"
done
echo "PostgreSQL is up and running! Proceeding with user and database creation."

# Create the application database if it doesn't exist (idempotent)
echo "Checking if database '${APP_DB_NAME}' exists..."
if psql -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "postgres" -tc "SELECT 1 FROM pg_database WHERE datname = '${APP_DB_NAME}'" | grep -q 1; then
  echo "Database '${APP_DB_NAME}' already exists. Skipping creation."
else
  echo "Creating database '${APP_DB_NAME}'..."
  psql -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "postgres" -c "CREATE DATABASE ${APP_DB_NAME};"
  if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create database '${APP_DB_NAME}'." >&2
    unset PGPASSWORD
    exit 1
  fi
  echo "Database '${APP_DB_NAME}' created."
fi

# Create the application user if it doesn't exist (idempotent)
echo "Checking if user '${APP_DB_USER_NAME}' exists..."
if psql -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${APP_DB_NAME}" -tc "SELECT 1 FROM pg_user WHERE usename = '${APP_DB_USER_NAME}'" | grep -q 1; then
  echo "User '${APP_DB_USER_NAME}' already exists. Skipping creation."
else
  echo "Creating user '${APP_DB_USER_NAME}'..."
  psql -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${APP_DB_NAME}" -c "CREATE USER ${APP_DB_USER_NAME} WITH PASSWORD '${APP_DB_USER_PASSWORD}';"
  if [ $? -ne 0 ]; then
    echo "ERROR: Failed to create user '${APP_DB_USER_NAME}'." >&2
    unset PGPASSWORD
    exit 1
  fi
  echo "User '${APP_DB_USER_NAME}' created."
fi

# --- START: Grant specific privileges on the application database to the new user ---
echo "Granting specific privileges on database '${APP_DB_NAME}' to user '${APP_DB_USER_NAME}'..."

# Combine all SQL commands into a single string, separated by semicolons
# Use double quotes for database names that might contain special characters (like hyphens)
# Use single quotes for string literals where needed
SQL_COMMANDS="
GRANT CONNECT ON DATABASE \"${APP_DB_NAME}\" TO ${APP_DB_USER_NAME};
GRANT CREATE ON DATABASE \"${APP_DB_NAME}\" TO ${APP_DB_USER_NAME};
GRANT USAGE, CREATE ON SCHEMA public TO ${APP_DB_USER_NAME};
ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES ON TABLES TO ${APP_DB_USER_NAME};
ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT SELECT, USAGE ON SEQUENCES TO ${APP_DB_USER_NAME};
ALTER DEFAULT PRIVILEGES FOR USER ${APP_DB_USER_NAME} IN SCHEMA public
GRANT EXECUTE ON FUNCTIONS TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ${APP_DB_USER_NAME};
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO ${APP_DB_USER_NAME};
"

# Execute the combined SQL commands
psql -h "${POSTGRES_HOST}" -p "${POSTGRES_PORT}" -U "${POSTGRES_SUPER_USER_NAME}" -d "${APP_DB_NAME}" -c "${SQL_COMMANDS}"

if [ $? -ne 0 ]; then
  echo "ERROR: Failed to grant specific privileges on database '${APP_DB_NAME}' to user '${APP_DB_USER_NAME}'." >&2
  unset PGPASSWORD
  exit 1
fi
echo "Specific privileges granted."
# --- END: Grant specific privileges ---

echo "--- create-promptchain-db-user script completed successfully ---"

# Unset PGPASSWORD for security
unset PGPASSWORD
exit 0 # Explicitly exit with success

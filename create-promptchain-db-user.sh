#!/usr/bin/env bash
# create-promptchain-db-user.sh
# This script waits for PostgreSQL to be ready, then creates a new application database user
# and grants privileges. Designed to be idempotent and short-lived.

set -euo pipefail

echo "--- Starting create-app-db-user script inside container ---"

# Ensure all required environment variables are set
REQUIRED_VARS=(
  POSTGRES_DB_NAME
  POSTGRES_SUPER_USER_NAME
  APP_DB_NAME
  APP_DB_USER_NAME
  APP_DB_USER_PASSWORD
)
for var in "${REQUIRED_VARS[@]}"; do
  if [ -z "${!var:-}" ]; then
    echo "ERROR: Required environment variable $var is not set" >&2
    exit 1
  fi
done

POSTGRES_PORT="${POSTGRES_PORT:-5432}"

# Get the Container-ID for the Postgres container
container_id=$(docker ps --filter "ancestor=timescale/timescaledb:latest-pg15" --format '{{.ID}}' | head -n1)
if [ -z "$container_id" ]; then
  echo "ERROR: No running ancestor=timescale/timescaledb:latest-pg15 container found! Aborting." >&2
  exit 2
fi

echo "Using container ID: $container_id"

# Wait for Postgres to be ready
MAX_RETRIES=10
RETRY_INTERVAL=3

for ((i=1; i<=MAX_RETRIES; i++)); do
  if docker exec "$container_id" pg_isready -U "$POSTGRES_SUPER_USER_NAME" -d "$POSTGRES_DB_NAME" >/dev/null 2>&1; then
    break
  fi
  if [ "$i" -eq "$MAX_RETRIES" ]; then
    echo "ERROR: PostgreSQL not ready after $MAX_RETRIES attempts! Aborting." >&2
    exit 3
  fi
  echo "PostgreSQL unavailable — retrying in $RETRY_INTERVAL seconds (attempt $i/$MAX_RETRIES)"
  sleep "$RETRY_INTERVAL"
done

echo "PostgreSQL is up — proceeding with setup."

# Create the database if it doesn't exist (idempotent)
if docker exec "$container_id" psql -p "$POSTGRES_PORT" -U "$POSTGRES_SUPER_USER_NAME" -d "$POSTGRES_DB_NAME" -tc \
  "SELECT 1 FROM pg_database WHERE datname = '$APP_DB_NAME'" | grep -q 1; then
  echo "Database '$APP_DB_NAME' already exists. Skipping creation."
else
  echo "Creating database '$APP_DB_NAME'..."
  docker exec "$container_id" psql -p "$POSTGRES_PORT" -U "$POSTGRES_SUPER_USER_NAME" -d "$POSTGRES_DB_NAME" \
    -c "CREATE DATABASE \"$APP_DB_NAME\";"
fi

# Create the user if it doesn't exist (idempotent)
if docker exec "$container_id" psql -p "$POSTGRES_PORT" -U "$POSTGRES_SUPER_USER_NAME" -d "$APP_DB_NAME" -tc \
  "SELECT 1 FROM pg_roles WHERE rolname = '$APP_DB_USER_NAME'" | grep -q 1; then
  echo "User '$APP_DB_USER_NAME' already exists. Skipping creation."
else
  echo "Creating user '$APP_DB_USER_NAME'..."
  docker exec "$container_id" psql -p "$POSTGRES_PORT" -U "$POSTGRES_SUPER_USER_NAME" -d "$APP_DB_NAME" \
    -c "CREATE USER \"$APP_DB_USER_NAME\" WITH PASSWORD '$APP_DB_USER_PASSWORD';"
fi

# Grant privileges (always re-run to ensure they're correct)
echo "Granting privileges to '$APP_DB_USER_NAME'..."
SQL_COMMANDS="
GRANT CONNECT ON DATABASE \"$APP_DB_NAME\" TO \"$APP_DB_USER_NAME\";
GRANT CREATE ON DATABASE \"$APP_DB_NAME\" TO \"$APP_DB_USER_NAME\";
GRANT USAGE, CREATE ON SCHEMA public TO \"$APP_DB_USER_NAME\";
ALTER DEFAULT PRIVILEGES FOR USER \"$APP_DB_USER_NAME\" IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES ON TABLES TO \"$APP_DB_USER_NAME\";
ALTER DEFAULT PRIVILEGES FOR USER \"$APP_DB_USER_NAME\" IN SCHEMA public
  GRANT SELECT, USAGE ON SEQUENCES TO \"$APP_DB_USER_NAME\";
ALTER DEFAULT PRIVILEGES FOR USER \"$APP_DB_USER_NAME\" IN SCHEMA public
  GRANT EXECUTE ON FUNCTIONS TO \"$APP_DB_USER_NAME\";
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO \"$APP_DB_USER_NAME\";
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO \"$APP_DB_USER_NAME\";
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO \"$APP_DB_USER_NAME\";
"

docker exec "$container_id" psql -p "$POSTGRES_PORT" -U "$POSTGRES_SUPER_USER_NAME" -d "$APP_DB_NAME" -v ON_ERROR_STOP=1 \
  -c "$SQL_COMMANDS"

echo "--- create-promptchain-db-user script completed successfully ---"

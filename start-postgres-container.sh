#!/bin/bash

# start_postgres_stack.sh
# This script starts the Docker Compose Postgres stack.
# Ensure this script is in the same directory as your docker-compose.yml file.

COMPOSE_FILE="docker-compose-postgres.yml"
ENV_FILE="dev-env.properties"

echo "--- Starting Postgres container ---"

# Load environment variables from properties file
if [ ! -f "$ENV_FILE" ]; then
  echo "ERROR: Environment file '${ENV_FILE}' not found."
  exit 1
fi

# Export variables from the properties file
# This removes comments, trims whitespace, skips empty lines,
# and exports the variables for the current shell and subprocesses
export $(grep -v '^#' "$ENV_FILE" | grep -v '^$' | sed 's/ *= */=/g')

POSTGRES_PORT="${POSTGRES_PORT:-5432}"

# Check if Docker daemon is running
echo "Checking Docker daemon status..."
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker daemon is not running or not accessible. Please start Docker."
    exit 2
fi
echo "Docker daemon is running."

# Start the Docker-Compose services
echo "Starting Docker-Compose services from '${COMPOSE_FILE}'..."
if ! docker-compose -f "${COMPOSE_FILE}" up -d; then
    echo "ERROR: Failed to start Docker-Compose stack."
    exit 3
fi
echo "Docker-Compose services started."

#echo "--- Postgres container logs (last 50 lines) ---"
#container_id=$(docker ps --filter "ancestor=postgres:15" --format '{{.ID}}' | head -n1)
#if [ -n "$container_id" ]; then
#  docker logs --tail 50 "$container_id"
#else
#  echo "No Postgres container found."
#fi
#
#echo "--- Network ports listening on host ---"
#ss -tlnp || netstat -tlnp || echo "ss/netstat not available"

echo "Waiting for Postgres service to be healthy and listening on $POSTGRES_HOST:$POSTGRES_PORT..."

MAX_WAIT=300
SLEEP_INTERVAL=5
elapsed=0

while true; do
  container_id=$(docker ps --filter "ancestor=timescaledb:latest-pg15" --format '{{.ID}}')

  if [ -n "$container_id" ]; then
    container_health=$(docker inspect --format '{{.State.Health.Status}}' "$container_id" 2>/dev/null || echo "none")
    # Check if pg_isready returns OK
    if docker exec "$container_id" pg_isready -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB_NAME}" | grep -q "accepting connections"; then
      pg_ready=true
    else
      pg_ready=false
    fi
  else
    container_health="none"
    pg_ready=false
  fi

  echo "Checking if port $POSTGRES_PORT is open on $POSTGRES_HOST..."
  nc -zv $POSTGRES_HOST $POSTGRES_PORT
  port_open=$?

  if [[ "$container_health" == "healthy" ]] && [[ "$pg_ready" == true ]] && [[ $port_open -eq 0 ]]; then
    echo "Postgres service is healthy and port $POSTGRES_PORT is open."
    break
  else
    echo "Waiting for Postgres: Health='$container_health', pg_isready='$pg_ready', port open='$port_open'..."
  fi

  elapsed=$((elapsed + SLEEP_INTERVAL))
  if [ $elapsed -ge $MAX_WAIT ]; then
    echo "ERROR: Timeout waiting for Postgres to become healthy and open port."
    exit 4
  fi

  sleep $SLEEP_INTERVAL
done

# Execute the DB init script
echo "Executing DB init script: ./create-promptchain-db-user.sh"
if [ ! -x "./create-promptchain-db-user.sh" ]; then
  echo "Making DB init script executable"
  chmod +x ./create-promptchain-db-user.sh
fi

if ! ./create-promptchain-db-user.sh; then
  echo "ERROR: DB init script failed."
  exit 6
fi

echo "DB init script completed successfully."

echo "--- Docker Compose Postgres Stack Deployment Complete ---"

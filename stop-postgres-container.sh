#!/usr/bin/env bash

# stop_postgres_stack.sh
# Stops and removes the Postgres stack via docker-compose.

COMPOSE_FILE="docker-compose-postgres.yml"
ENV_FILE="dev-env.properties"

echo "--- Stopping Postgres stack ---"

# Check Docker daemon
echo "Checking Docker daemon status..."
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker daemon is not running or not accessible. Please start Docker."
    exit 1
fi
echo "Docker daemon is running."

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

# Stop and remove Docker Compose stack
echo "Stopping Docker Compose services from '${COMPOSE_FILE}'..."
if ! docker-compose -f "${COMPOSE_FILE}" down; then
    echo "ERROR: Failed to stop Docker Compose services."
    exit 2
fi
echo "Docker Compose services stopped and removed successfully."

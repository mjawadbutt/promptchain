#!/bin/bash

# start_redis_stack.sh
# This script starts the Docker Compose Redis stack.
# Ensure this script is in the same directory as your docker-compose-redis.yml file.

COMPOSE_FILE="docker-compose-redis.yml"
ENV_FILE="dev-env.properties"

echo "--- Starting Docker Compose Redis Stack ---"

# 1. Load environment variables from properties file
if [ ! -f "$ENV_FILE" ]; then
  echo "ERROR: Environment file '${ENV_FILE}' not found."
  exit 1
fi

# Export variables from the properties file
# This removes comments, trims whitespace, skips empty lines,
# and exports the variables for the current shell and subprocesses
export $(grep -v '^#' "$ENV_FILE" | grep -v '^$' | sed 's/ *= */=/g')

# Check if Docker daemon is running
echo "Checking Docker daemon status..."
if ! docker info > /dev/null 2>&1; then
  echo "ERROR: Docker daemon is not running or not accessible. Please start Docker."
  exit 2
fi
echo "Docker daemon is running."

# Start the stack using docker-compose up -d
echo "Starting Docker Compose stack from '${COMPOSE_FILE}'..."
if ! docker-compose -f "${COMPOSE_FILE}" up -d; then
  echo "ERROR: Failed to start Docker Compose stack."
  exit 3
fi
echo "Docker Compose stack started."

echo "--- Containers running ---"
docker ps

echo "--- Redis container logs (last 50 lines) ---"
container_id=$(docker ps --filter "ancestor=redis:8.0.3" --format '{{.ID}}' | head -n1)
if [ -n "$container_id" ]; then
  docker logs --tail 50 "$container_id"
else
  echo "No Redis container found."
fi

echo "--- Network ports listening on host ---"
ss -tlnp || netstat -tlnp || echo "ss/netstat not available"

echo "Waiting for Redis service to be healthy and listening on localhost:6379..."

MAX_WAIT=300
SLEEP_INTERVAL=5
elapsed=0

while true; do
  container_id=$(docker ps --filter "ancestor=redis:8.0.3" --format '{{.ID}}')

  if [ -n "$container_id" ]; then
    container_health=$(docker inspect --format '{{.State.Health.Status}}' "$container_id" 2>/dev/null || echo "none")
  else
    container_health="none"
  fi

  echo "Checking if port 6379 is open on localhost..."
  nc -zv localhost 6379
  port_open=$?

  if [[ "$container_health" == "healthy" ]] && [[ $port_open -eq 0 ]]; then
    echo "Redis service is healthy and port 6379 is open."
    break
  else
    echo "Waiting for Redis: Health='$container_health', port open='$port_open'..."
  fi

  elapsed=$((elapsed + SLEEP_INTERVAL))
  if [ $elapsed -ge $MAX_WAIT ]; then
    echo "ERROR: Timeout waiting for Redis to become healthy and open port."
    exit 1
  fi

  sleep $SLEEP_INTERVAL
done

echo "--- Docker Compose Redis Stack Deployment Complete ---"

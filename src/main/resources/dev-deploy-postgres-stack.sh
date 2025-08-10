#!/bin/bash

# start_postgres_stack.sh
# This script starts the Docker Swarm Postgres stack.
# Ensure this script is in the same directory as docker-compose-postgres.yml file.

STACK_NAME="postgres-stack"
COMPOSE_FILE="docker-compose-postgres.yml"

POSTGRES_DB_NAME=@postgres_db_name@
POSTGRES_SUPER_USER_NAME=@postgres_super_user_name@
POSTGRES_SUPER_USER_PASSWORD=@postgres_super_user_password@
APP_DB_NAME=@app_db_name@
APP_DB_USER_NAME=@app_db_user_name@
APP_DB_USER_PASSWORD=@app_db_user_password@

echo "--- Starting Docker Postgres Stack: ${STACK_NAME} ---"

# 1. Check if Docker daemon is running
echo "Checking Docker daemon status..."
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker daemon is not running or not accessible. Please start Docker."
    exit 1
fi
echo "Docker daemon is running."

# 2. Check if Swarm mode is active and initialize if not
echo "Checking Docker Swarm mode status..."
SWARM_STATUS=$(docker info --format "{{.Swarm.LocalNodeState}}")

if [ "${SWARM_STATUS}" != "active" ]; then
    echo "Docker Swarm mode is not active. Initializing Swarm..."
    if ! docker swarm init > /dev/null 2>&1; then
        echo "ERROR: Failed to initialize Docker Swarm. Ensure port 2377 is available and not blocked by firewall."
        exit 1
    fi
    echo "Docker Swarm initialized successfully."
else
    echo "Docker Swarm mode is already active."
fi

# 3. Deploy the stack
echo "Preparing to deploy Docker stack '${STACK_NAME}'..."

echo "Deploying Docker stack '${STACK_NAME}' from '${COMPOSE_FILE}'..."
if ! docker stack deploy -c "${COMPOSE_FILE}" "${STACK_NAME}"; then
    echo "ERROR: Failed to deploy stack '${STACK_NAME}'."
    exit 1
fi
echo "Stack '${STACK_NAME}' deployment initiated."

echo "--- Docker Services ---"
docker service ls

echo "--- Postgres service tasks ---"
docker service ps postgres-stack_postgres

echo "--- Postgres service logs (last 50 lines) ---"
docker service logs --tail 50 postgres-stack_postgres

echo "--- Containers running ---"
docker ps

echo "--- Container logs for Postgres ---"
container_id=$(docker ps --filter "name=postgres-stack_postgres" --format '{{.ID}}' | head -n1)
if [ -n "$container_id" ]; then
  docker logs --tail 50 "$container_id"
else
  echo "No Postgres container found."
fi

echo "--- Network ports listening on runner ---"
ss -tlnp || netstat -tlnp || echo "ss/netstat not available"

echo "Waiting for Postgres service to be healthy and listening on localhost:5432..."

MAX_WAIT=300
SLEEP_INTERVAL=5
elapsed=0

while true; do

  # Healthcheck might not be set on service task level; check container health directly:
  container_id=$(docker ps --filter "name=${STACK_NAME}_postgres" --format '{{.ID}}')

  if [ -n "$container_id" ]; then
    container_health=$(docker inspect --format '{{.State.Health.Status}}' "$container_id" 2>/dev/null || echo "none")
    docker exec "$container_id" pg_isready -U "${POSTGRES_SUPER_USER_NAME}" -d "${POSTGRES_DB_NAME}"
  else
    container_health="none"
  fi

  echo "Checking if port 5432 is open on localhost..."
  nc -zv localhost 5432
  port_open=$?

  if [[ "$container_health" == "healthy" ]] && [[ $port_open -eq 0 ]]; then
    echo "Postgres service is healthy and port 5432 is open."
    break
  else
    echo "Waiting for Postgres: Health status='$container_health', port open='$port_open'..."
  fi

  elapsed=$((elapsed + SLEEP_INTERVAL))
  if [ $elapsed -ge $MAX_WAIT ]; then
    echo "ERROR: Timeout waiting for Postgres service to be healthy and port open."
    echo "Use 'docker service ps ${STACK_NAME}_postgres' and 'docker service logs ${STACK_NAME}_postgres' to debug."
    exit 1
  fi

  sleep $SLEEP_INTERVAL
done

# Optionally wait for create-promptchain-db-user to finish (one-shot container)
echo "Waiting for DB init job 'create-promptchain-db-user' to complete..."
job_container_id=$(docker ps -a --filter "name=${STACK_NAME}_create-promptchain-db-user" --format '{{.ID}}')
if [ -n "$job_container_id" ]; then
  docker wait "$job_container_id"
  job_exit_code=$(docker inspect --format='{{.State.ExitCode}}' "$job_container_id")
  if [ "$job_exit_code" -ne 0 ]; then
    echo "ERROR: DB init job container exited with code $job_exit_code."
    echo "Logs:"
    docker logs "$job_container_id"
    exit 1
  fi
  echo "DB init job completed successfully."
fi

echo "--- Docker Postgres Stack Deployment Complete. Use 'docker stack ps ${STACK_NAME}' to check status ---"
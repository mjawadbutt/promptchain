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
echo "Stack '${STACK_NAME}' deployment initiated. Use 'docker stack ps ${STACK_NAME}' to check status."

echo "--- Docker Postgres Stack Deployment Complete ---"
#!/bin/bash

# stop_redis_stack.sh
# Stops and removes the Redis stack via docker-compose.

COMPOSE_FILE="docker-compose-redis.yml"

echo "--- Stopping Redis stack ---"

# Check Docker daemon
echo "Checking Docker daemon status..."
if ! docker info > /dev/null 2>&1; then
    echo "ERROR: Docker daemon is not running or not accessible. Please start Docker."
    exit 1
fi
echo "Docker daemon is running."

# Stop and remove Docker Compose stack
echo "Stopping Docker Compose services from '${COMPOSE_FILE}'..."
if ! docker-compose -f "${COMPOSE_FILE}" down; then
    echo "ERROR: Failed to stop Docker Compose services."
    exit 2
fi
echo "Docker Compose services stopped and removed successfully."

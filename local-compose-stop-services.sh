#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Stopping Docker Compose services for local development..."

# Bring down all services defined in the compose files
docker compose -f docker-compose.yml -f docker-compose.override.yml down

echo "Docker Compose services stopped."
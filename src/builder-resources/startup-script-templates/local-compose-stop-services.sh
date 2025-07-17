#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Stopping Docker Compose services for local development..."

export GHCRIO_USERNAME=@ghcrio.username@
export PROJECT_ARTIFACT_ID=@project.artifactId@
export PROJECT_VERSION=@project.version@
export POSTGRES_DB_NAME=@postgres.db_name_local@
export POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_local@
export POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_local@
export APP_DB_NAME=@app_db.db_name_local@
export APP_DB_USER_NAME=@app_db.user_name_local@
export APP_DB_USER_PASSWORD=@app_db.user_password_local@

# Bring down all services defined in the compose files
docker compose -f docker-compose.yml -f docker-compose.override.yml down

echo "Docker Compose services stopped."

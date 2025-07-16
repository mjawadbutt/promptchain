#!/bin/bash
#####################################################################################################################
# This script is also used by the maven execution: 'local-compose-start-services' for starting services during
# the verify (integration test execution) phase.
#####################################################################################################################

export GHCRIO_USERNAME=@ghcrio.username@
export PROJECT_ARTIFACT_ID=@project.artifactId@
export PROJECT_VERSION=@project.version@
export POSTGRES_DB_NAME=@postgres.db_name_local@
export POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_local@
export POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_local@
export APP_DB_NAME=@app_db.db_name_local@
export APP_DB_USER_NAME=@app_db.user_name_local@
export APP_DB_USER_PASSWORD=@app_db.user_password_local@

set -e # Exit immediately if a command exits with a non-zero status

echo "Starting Docker Compose services for local development..."

# The 'up' command will automatically start Postgres, Redis, and App
# It will also pick up the 'build: .' for the app if its image is not fresh
docker compose -f docker-compose.yml -f docker-compose.override.yml up --build -d postgres redis app

echo "Docker Compose services started in detached mode."

# --- Database Initialization Step ---
echo "Waiting for PostgreSQL to be healthy before running database initialization..."
# Ensure postgres is fully up and healthy before trying to run db-init
# The 'wait' command will block until the service passes its health check
docker compose -f docker-compose.yml -f docker-compose.override.yml wait postgres --health-status healthy

echo "Running Database Initialization (promptchain_db_user and mydb creation)..."
# The --profile db-setup is crucial here to include the db-init service
# --rm ensures the temporary db-init container is removed after execution
docker compose -f docker-compose.yml -f docker-compose.override.yml --profile db-setup run --rm db-init

echo "Database initialization complete (or skipped if user/db already exist)."
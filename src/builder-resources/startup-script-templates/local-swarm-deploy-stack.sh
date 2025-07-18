#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Deploying Docker Swarm stack to production..."

export DOCKER_BUILDKIT=1
export GHCRIO_USERNAME=@ghcrio.username@
export PROJECT_ARTIFACT_ID=@project.artifactId@
export PROJECT_VERSION=@project.version@
export POSTGRES_DB_NAME=@postgres.db_name_prod@
export POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_prod@
export POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_prod@
export APP_DB_NAME=@app_db.db_name_prod@
export APP_DB_USER_NAME=@app_db.user_name_prod@
export APP_DB_USER_PASSWORD=@app_db.user_password_prod@

# --- 1. Check required environment variables ---
#if [ -z "$GHCRIO_USERNAME" ]; then
#  echo "Error: GHCRIO_USERNAME environment variable is not set."
#  echo "Please set it before deploying the stack (e.g., export GHCRIO_USERNAME=your_username)."
#  exit 1
#fi

# --- 2. Deploy the main Docker Swarm stack ---
# This creates the 'promptchain-stack_app-net' overlay network and starts the core services.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml promptchain-stack

echo "Docker Swarm stack 'promptchain-stack' deployment initiated."
echo "Waiting a moment for services to settle..."
sleep 10 # Give services a moment to start and postgres to begin its healthcheck

# --- 3. Run the one-off Database Initialization ---
echo "Running Production Database Initialization (creates and configures the app's db-user)..."
# We run this using 'docker compose run' (not 'docker stack deploy') for a one-off task.
# It will connect to the existing 'promptchain-stack_app-net' network.
docker compose -f docker-compose.db-init.prod.yml run --rm db-init

echo "Production Database initialization complete (or skipped if already initialized)."
echo "You can check the status of your stack with: docker stack ps promptchain-stack"

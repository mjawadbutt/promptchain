#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Deploying Docker Swarm stack to production..."

# --- 1. Check required environment variables ---
if [ -z "$GHCRIO_USERNAME" ]; then
  echo "Error: GHCRIO_USERNAME environment variable is not set."
  echo "Please set it before deploying the stack (e.g., export GHCRIO_USERNAME=your_username)."
  exit 1
fi
if [ -z "$POSTGRES_SUPER_PASSWORD_PROD" ]; then
  echo "Error: POSTGRES_SUPER_PASSWORD_PROD environment variable is not set."
  echo "This is required for the database initialization step. Please set it securely."
  exit 1
fi

# --- 2. Deploy the main Docker Swarm stack ---
# This creates the 'myapp_stack_app-net' overlay network and starts the core services.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml myapp_stack

echo "Docker Swarm stack 'myapp_stack' deployment initiated."
echo "Waiting a moment for services to settle..."
sleep 10 # Give services a moment to start and postgres to begin its healthcheck

# --- 3. Run the one-off Database Initialization ---
echo "Running Production Database Initialization (myappuser_prod and schema setup)..."
# We run this using 'docker compose run' (not 'docker stack deploy') for a one-off task.
# It will connect to the existing 'myapp_stack_app-net' network.
# The 'depends_on: service_healthy' in db-init.prod.yml will ensure Postgres is ready.
# Note: POSTGRES_SUPER_PASSWORD_PROD is passed to the environment for this command.
POSTGRES_SUPER_PASSWORD_PROD="$POSTGRES_SUPER_PASSWORD_PROD" docker compose -f docker-compose.db-init.prod.yml run --rm db-init

echo "Production Database initialization complete (or skipped if already initialized)."
echo "You can check the status of your stack with: docker stack ps myapp_stack"
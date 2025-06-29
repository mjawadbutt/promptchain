#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Deploying Docker Swarm stack to production..."

# IMPORTANT: Ensure the GHCRIO_USERNAME environment variable is set
# e.g., export GHCRIO_USERNAME="your-actual-ghcr-username"
if [ -z "$GHCRIO_USERNAME" ]; then
  echo "Error: GHCRIO_USERNAME environment variable is not set."
  echo "Please set it before deploying the stack (e.g., export GHCRIO_USERNAME=your_username)."
  exit 1
fi

# The 'docker stack deploy' command uses the specified compose files
# and automatically manages services, networks, and volumes as defined for Swarm.
# 'myapp_stack' is the name of your Docker Swarm stack.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml myapp_stack

echo "Docker Swarm stack 'myapp_stack' deployment initiated."
echo "You can check the status with: docker stack ps myapp_stack"
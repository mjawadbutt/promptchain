#!/bin/bash
# init-env.sh
#
# This script initializes environment variables required for Maven builds
# (mvn install, mvn deploy) and Docker Compose/Swarm operations.
#
# IMPORTANT: Replace placeholder values with your actual credentials.
# For security, avoid hardcoding sensitive passwords directly in version control.
# Consider sourcing these from a secure local environment file (e.g., .env)
# or injecting them via your CI/CD pipeline's secret management.

echo "Initializing build environment variables..."

# 1. GitHub Container Registry (GHCR) Username:
# This is used by the Fabric8 Docker Maven Plugin to tag and push your Docker image.
# Your pom.xml's 'set-default-ghcrio-username' profile will pick this up if GITHUB_ACTOR is not set.
export GHCRIO_USERNAME="your-github-container-registry-username" # e.g., your-github-username

# 2. PostgreSQL Superuser Password for Local Development:
# Used by 'docker-compose.override.yml' for the 'postgres' service and 'db-init' service.
export POSTGRES_SUPER_PASSWORD_LOCAL="my_super_secret_local_password"

# 3. PostgreSQL Superuser Password for Production Deployment:
# Used by 'docker-compose.prod.yml' for the 'postgres' service and by 'docker-compose.db-init.prod.yml'
# when running the one-off db-init for production.
export POSTGRES_SUPER_PASSWORD_PROD="my_super_secret_prod_password" # Highly sensitive! Use CI/CD secrets.

echo "Environment variables set."
echo "  GHCRIO_USERNAME=${GHCRIO_USERNAME}"
echo "  POSTGRES_SUPER_PASSWORD_LOCAL=********"
echo "  POSTGRES_SUPER_PASSWORD_PROD=********"
echo ""
echo "To apply these variables to your current shell, run: "
echo "  source ./init-build-env.sh"
echo "After sourcing, you can run 'mvn install', 'mvn deploy', or 'docker stack deploy'."
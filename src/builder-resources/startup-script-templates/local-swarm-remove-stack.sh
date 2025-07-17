#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Removing Docker Swarm stack from production..."

export GHCRIO_USERNAME=@ghcrio.username@
export PROJECT_ARTIFACT_ID=@project.artifactId@
export PROJECT_VERSION=@project.version@
export POSTGRES_DB_NAME=@postgres.db_name_prod@
export POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_prod@
export POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_prod@
export APP_DB_NAME=@app_db.db_name_prod@
export APP_DB_USER_NAME=@app_db.user_name_prod@
export APP_DB_USER_PASSWORD=@app_db.user_password_prod@

# 'promptchain-stack' is the name of your Docker Swarm stack to remove.
docker stack rm promptchain-stack

echo "Docker Swarm stack 'promptchain-stack' removal initiated."
echo "You can check the status with: docker stack ls"

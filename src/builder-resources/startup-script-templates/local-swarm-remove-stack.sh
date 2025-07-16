#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Removing Docker Swarm stack from production..."

# 'promptchain-stack' is the name of your Docker Swarm stack to remove.
docker stack rm promptchain-stack

echo "Docker Swarm stack 'promptchain-stack' removal initiated."
echo "You can check the status with: docker stack ls"

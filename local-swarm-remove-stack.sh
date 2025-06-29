#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "Removing Docker Swarm stack from production..."

# 'myapp_stack' is the name of your Docker Swarm stack to remove.
docker stack rm myapp_stack

echo "Docker Swarm stack 'myapp_stack' removal initiated."
echo "You can check the status with: docker stack ls"
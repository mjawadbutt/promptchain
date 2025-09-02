#!/usr/bin/env bash

echo "========================================================================================="
echo "                            🚀 Docker Nuclear Cleanup Script                             "
echo "                                                                                         "
echo " Starting Docker clean-up process to achieve a clean slate...                            "
echo " WARNING: This script will stop and remove ALL containers, volumes, networks, and images."
echo " Press Ctrl+C to abort, or wait 5 seconds to proceed...                                  "
echo "========================================================================================="

sleep 5

# Step 1: Leave swarm mode if active
echo "Checking swarm mode..."
if docker info 2>/dev/null | grep -q "Swarm: active"; then
  echo "Swarm is active. Leaving swarm..."
  docker swarm leave --force || true
else
  echo "Swarm is already inactive."
fi

# Step 2: Kill all running containers
echo "Stopping all containers..."
docker ps -q | xargs -r docker stop

# Step 3: Remove all containers
echo "Removing all containers..."
docker ps -aq | xargs -r docker rm -f

# Step 4: Remove all volumes
echo "Removing all volumes..."
docker volume ls -q | xargs -r docker volume rm

# Step 5: Full prune (images, volumes, build cache)
echo "Pruning system..."
docker system prune -a --volumes --force

echo "============================================"
echo "  ✅ Docker environment fully cleaned!"
echo "============================================"

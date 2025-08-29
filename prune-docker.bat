#!/bin/bash
echo "============================================"
echo "  🚀 Docker Nuclear Cleanup Script"
echo "============================================"

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

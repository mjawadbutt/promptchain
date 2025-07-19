#!/bin/bash

# remove_redis_stack.sh
# This script removes the Docker Swarm Redis stack.

REDIS_STACK_NAME="redis-stack"

echo "--- Attempting to remove Redis stack: ${REDIS_STACK_NAME} ---"

if docker stack rm "${REDIS_STACK_NAME}"; then
  echo "Redis stack removal command issued. Use 'docker stack ls' to confirm removal."
  # Optional: Wait for the stack to fully disappear
  until [ -z "$(docker stack ls --format "{{.Name}}" | grep "^${REDIS_STACK_NAME}$")" ]; do
    echo "Waiting for ${REDIS_STACK_NAME} to be removed..."
    sleep 3
  done
  echo "Redis stack '${REDIS_STACK_NAME}' removed successfully."
else
  echo "Warning: Could not issue removal command for Redis stack '${REDIS_STACK_NAME}'. It might not exist or an error occurred."
fi

echo "--- Redis Stack Removal Finished ---"
echo "Verify with: docker stack ls"
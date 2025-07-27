#!/bin/bash

# remove_postgres_stack.sh
# This script removes the Docker Swarm PostgreSQL stack.

POSTGRES_STACK_NAME="postgres-stack"

echo "--- Attempting to remove PostgreSQL stack: ${POSTGRES_STACK_NAME} ---"

if docker stack rm "${POSTGRES_STACK_NAME}"; then
  echo "PostgreSQL stack removal command issued. Use 'docker stack ls' to confirm removal."
  # Optional: Wait for the stack to fully disappear
  until [ -z "$(docker stack ls --format "{{.Name}}" | grep "^${POSTGRES_STACK_NAME}$")" ]; do
    echo "Waiting for ${POSTGRES_STACK_NAME} to be removed..."
    sleep 3
  done
  echo "PostgreSQL stack '${POSTGRES_STACK_NAME}' removed successfully."
else
  echo "Warning: Could not issue removal command for PostgreSQL stack '${POSTGRES_STACK_NAME}'. It might not exist or an error occurred."
fi

echo "--- PostgreSQL Stack Removal Finished ---"
echo "Verify with: docker stack ls"
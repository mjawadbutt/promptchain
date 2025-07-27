#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "--- Starting All Infrastructure Stacks ---"

# Call the Redis stack start script
echo "Calling Redis stack start script..."
if ! ./dev-deploy-redis-stack.sh; then
  echo "ERROR: Redis stack failed to start. Aborting."
  exit 1
fi
echo "Redis stack start script completed."

echo "" # Newline for readability

# Call the PostgreSQL stack start script
echo "Calling PostgreSQL stack start script..."
if ! ./dev-deploy-postgres-stack.sh; then
  echo "ERROR: PostgreSQL stack failed to start. Aborting."
  exit 1
fi
echo "PostgreSQL stack start script completed."

echo "" # Newline for readability
echo "--- All Infrastructure Stacks Started Successfully ---"
echo "You can verify with: docker stack ls"

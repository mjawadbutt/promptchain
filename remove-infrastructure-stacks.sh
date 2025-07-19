#!/bin/bash
set -e # Exit immediately if a command exits with a non-zero status

echo "--- Starting All Infrastructure Stack Removal ---"

# Call the Redis stack removal script
echo "Calling Redis stack removal script..."
if ! ./remove-redis-stack.sh; then
  echo "WARNING: Redis stack removal encountered an issue, but continuing with PostgreSQL."
  # Do not exit here, allow PostgreSQL removal to proceed even if Redis had a warning.
fi
echo "Redis stack removal script completed."

echo "" # Newline for readability

# Call the PostgreSQL stack removal script
echo "Calling PostgreSQL stack removal script..."
if ! ./remove-postgres-stack.sh; then
  echo "WARNING: PostgreSQL stack removal encountered an issue."
  # Do not exit here, allow the script to finish.
fi
echo "PostgreSQL stack removal script completed."

echo "" # Newline for readability
echo "--- All Infrastructure Stack Removal Finished ---"
echo "You can verify with: docker stack ls"

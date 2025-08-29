#!/bin/bash

echo "Starting Docker clean-up process to achieve a clean slate..."
echo "WARNING: This script will stop and remove ALL containers, volumes, networks, and images."
echo "Press Ctrl+C to abort, or wait 5 seconds to proceed..."
sleep 5

./docker volume rm postgres-stack_postgres_data
./docker system prune --all --volumes --force

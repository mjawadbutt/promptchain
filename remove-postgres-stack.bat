:: remove_postgres_stack.bat
:: This script removes the Docker Swarm PostgreSQL stack.

@echo off
setlocal

set POSTGRES_STACK_NAME=postgres-stack

echo --- Attempting to remove PostgreSQL stack: %POSTGRES_STACK_NAME% ---

docker stack rm %POSTGRES_STACK_NAME%
if %errorlevel% neq 0 (
    echo Warning: Could not issue removal command for PostgreSQL stack '%POSTGRES_STACK_NAME%'. It might not exist or an error occurred.
) else (
    echo PostgreSQL stack removal command issued. Use 'docker stack ls' to confirm removal.
)

echo --- PostgreSQL Stack Removal Finished ---
echo Verify with: docker stack ls

endlocal
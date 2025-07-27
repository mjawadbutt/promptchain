:: remove_redis_stack.bat
:: This script removes the Docker Swarm Redis stack.

@echo off
setlocal

set REDIS_STACK_NAME=redis-stack

echo --- Attempting to remove Redis stack: %REDIS_STACK_NAME% ---

docker stack rm %REDIS_STACK_NAME%
if %errorlevel% neq 0 (
    echo Warning: Could not issue removal command for Redis stack '%REDIS_STACK_NAME%'. It might not exist or an error occurred.
) else (
    echo Redis stack removal command issued. Use 'docker stack ls' to confirm removal.
)

echo --- Redis Stack Removal Finished ---
echo Verify with: docker stack ls

endlocal
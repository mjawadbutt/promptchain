:: start_all_infra_stacks.bat
:: This script calls the individual start scripts for Redis and Postgres stacks.

@echo off
setlocal

echo --- Starting All Infrastructure Stacks ---

:: Call the Redis stack start script
echo Calling Redis stack start script...
call deploy-redis-stack.bat
if %errorlevel% neq 0 (
    echo ERROR: Redis stack failed to start. Aborting.
    exit /b 1
)
echo Redis stack start script completed.

echo. :: Newline for readability

:: Call the PostgreSQL stack start script
echo Calling PostgreSQL stack start script...
call deploy-postgres-stack.bat
if %errorlevel% neq 0 (
    echo ERROR: PostgreSQL stack failed to start. Aborting.
    exit /b 1
)
echo PostgreSQL stack start script completed.

echo. :: Newline for readability
echo --- All Infrastructure Stacks Started Successfully ---
echo You can verify with: docker stack ls

endlocal
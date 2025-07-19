:: remove_all_infra_stacks.bat
:: This script calls the individual removal scripts for Redis and Postgres stacks.

@echo off
setlocal

echo --- Starting All Infrastructure Stack Removal ---

:: Call the Redis stack removal script
echo Calling Redis stack removal script...
call remove-redis-stack.bat
if %errorlevel% neq 0 (
    echo WARNING: Redis stack removal encountered an issue, but continuing with PostgreSQL.
)
echo Redis stack removal script completed.

echo. :: Newline for readability

:: Call the PostgreSQL stack removal script
echo Calling PostgreSQL stack removal script...
call remove-postgres-stack.bat
if %errorlevel% neq 0 (
    echo WARNING: PostgreSQL stack removal encountered an issue.
)
echo PostgreSQL stack removal script completed.

echo. :: Newline for readability
echo --- All Infrastructure Stack Removal Finished ---
echo You can verify with: docker stack ls

endlocal
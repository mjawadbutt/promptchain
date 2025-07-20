:: start_postgres_stack.bat
:: This script starts the Docker Swarm Postgres stack.
:: Ensure this script is in the same directory as docker-compose-postgres.yml file.

@echo off
setlocal

set STACK_NAME=postgres-stack
set COMPOSE_FILE=docker-compose-postgres.yml

set POSTGRES_DB_NAME=@postgres_db_name@
set POSTGRES_SUPER_USER_NAME=@postgres_super_user_name@
set POSTGRES_SUPER_USER_PASSWORD=@postgres_super_user_password@
set APP_DB_NAME=@app_db_name@
set APP_DB_USER_NAME=@app_db_user_name@
set APP_DB_USER_PASSWORD=@app_db_user_password@

echo --- Starting Docker Postgres Stack: %STACK_NAME% ---

:: 1. Check if Docker daemon is running
echo Checking Docker daemon status...
docker info > nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker daemon is not running or not accessible. Please start Docker Desktop/Engine.
    exit /b 1
)
echo Docker daemon is running.

:: 2. Check if Docker Swarm mode is active and initialize if not
echo Checking Docker Swarm mode status...
for /f "tokens=*" %%i in ('docker info --format "{{.Swarm.LocalNodeState}}"') do (
    set SWARM_STATUS=%%i
)

if "%SWARM_STATUS%" neq "active" (
    echo Docker Swarm mode is not active. Initializing Swarm...
    docker swarm init > nul 2>&1
    if %errorlevel% neq 0 (
        echo ERROR: Failed to initialize Docker Swarm. Ensure port 2377 is available and not blocked by firewall.
        exit /b 1
    )
    echo Docker Swarm initialized successfully.
) else (
    echo Docker Swarm mode is already active.
)

:: 3. Deploy the stack
echo Preparing to deploy Docker stack '%STACK_NAME%'...

echo Deploying Docker stack '%STACK_NAME%' from '%COMPOSE_FILE%'...
docker stack deploy -c %COMPOSE_FILE% %STACK_NAME%
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy stack '%STACK_NAME%'.
    exit /b 1
)
echo Stack '%STACK_NAME%' deployment initiated. Use 'docker stack ps %STACK_NAME%' to check status.

REM TODO-Jawad: may add logic to wait until the create user container shutsdown
echo --- Docker Postgres Stack Deployment Complete ---
endlocal

:: start_redis_stack.bat
:: This script starts the Docker Swarm Redis stack.
:: Ensure this script is in the same directory as redis-infrastructure.yml file.

@echo off
setlocal

set STACK_NAME=redis-stack
set COMPOSE_FILE=docker-compose-redis.yml

set REDIS_PASSWORD=@redis_password@

echo --- Starting Docker Redis Stack: %STACK_NAME% ---

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

echo --- Docker Redis Stack Deployment Complete ---
endlocal
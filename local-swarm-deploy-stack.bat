@echo off
echo Deploying Docker Swarm stack to production...

REM IMPORTANT: Ensure the GHCRIO_USERNAME environment variable is set
REM e.g., set GHCRIO_USERNAME="your-actual-ghcr-username"
if "%GHCRIO_USERNAME%"=="" (
  echo Error: GHCRIO_USERNAME environment variable is not set.
  echo Please set it before deploying the stack (e.g., set GHCRIO_USERNAME=your_username).
  exit /b 1
)

REM The 'docker stack deploy' command uses the specified compose files
REM and automatically manages services, networks, and volumes as defined for Swarm.
REM 'myapp_stack' is the name of your Docker Swarm stack.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml myapp_stack
if %errorlevel% neq 0 (
    echo Error deploying stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'myapp_stack' deployment initiated.
echo You can check the status with: docker stack ps myapp_stack
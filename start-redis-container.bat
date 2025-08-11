@echo off
REM ============================================================================
REM start_redis_stack.bat
REM Starts Redis stack via docker-compose and waits until ready.
REM Requires: docker CLI, docker-compose
REM ============================================================================

setlocal enabledelayedexpansion

set COMPOSE_FILE=docker-compose-redis.yml
set ENV_FILE=dev-env.properties

echo --- Starting Redis container ---

REM ----------------------------------------------------------------------------
REM Load environment variables from properties file
REM ----------------------------------------------------------------------------
if not exist "%ENV_FILE%" (
    echo ERROR: Environment file "%ENV_FILE%" not found.
    exit /b 1
)

for /f "tokens=1,* delims==" %%A in (dev-env.properties) do (
    set "%%A=%%B"
)

REM ----------------------------------------------------------------------------
REM Check Docker daemon
REM ----------------------------------------------------------------------------
echo Checking Docker daemon status...
docker info >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker daemon is not running or not accessible. Please start Docker.
    exit /b 2
)
echo Docker daemon is running.

REM ----------------------------------------------------------------------------
REM Start the Docker-Compose services
REM ----------------------------------------------------------------------------
echo Starting Docker-Compose services from "%COMPOSE_FILE%"...
docker-compose -f "%COMPOSE_FILE%" up -d
if errorlevel 1 (
    echo ERROR: Failed to start Docker-Compose services.
    exit /b 3
)

echo Docker-Compose services started.

REM ----------------------------------------------------------------------------
REM Show Redis logs
REM ----------------------------------------------------------------------------
set CONTAINER_ID=
for /f "tokens=*" %%i in ('docker ps --filter "ancestor=redis:8.0.3" --format "{{.ID}}"') do (
    set CONTAINER_ID=%%i
)

if not defined CONTAINER_ID (
    echo ERROR: No Redis container found.
    exit /b 4
)

docker logs --tail 50 %CONTAINER_ID%
echo Found container: %CONTAINER_ID%

REM ----------------------------------------------------------------------------
REM Network ports listening
REM ----------------------------------------------------------------------------
echo --- Network ports listening on host ---
netstat -tlnp 2>nul || echo netstat not available

REM ----------------------------------------------------------------------------
REM Wait for Redis to be healthy
REM ----------------------------------------------------------------------------
echo Waiting for Redis service to be healthy and listening on localhost:6379...

set /a MAX_WAIT=300
set /a SLEEP_INTERVAL=5
set /a ELAPSED=0

:WAIT_LOOP
REM Get fresh container ID in case of restart
set CONTAINER_ID=
for /f "tokens=*" %%i in ('docker ps --filter "ancestor=redis:8.0.3" --format "{{.ID}}"') do (
    set CONTAINER_ID=%%i
)

if not defined CONTAINER_ID (
    echo ERROR: No Redis container found. Waiting for container to start...
    timeout /t 2 >nul
    set /a ELAPSED+=2
    if !ELAPSED! GEQ %MAX_WAIT% (
        echo ERROR: Redis container did not appear after %MAX_WAIT% seconds.
        exit /b 5
    )
    goto WAIT_LOOP
)

REM Get container health status
for /f "tokens=*" %%h in ('docker inspect --format "{{.State.Health.Status}}" %CONTAINER_ID% 2^>nul') do (
    set CONTAINER_HEALTH=%%h
)

if /i "!CONTAINER_HEALTH!"=="healthy" (
    echo Redis service is healthy and port 6379 is open.
    goto READY
) else (
    echo Waiting for Redis: Health="!CONTAINER_HEALTH!"...
)

set /a ELAPSED+=SLEEP_INTERVAL
if !ELAPSED! GEQ %MAX_WAIT% (
    echo ERROR: Timeout waiting for Redis to become healthy.
    exit /b 6
)

timeout /t %SLEEP_INTERVAL% >nul
goto WAIT_LOOP

:READY
echo --- Docker Compose Redis Stack Deployment Complete ---

endlocal
exit /b 0

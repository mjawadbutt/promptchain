@echo off
REM ============================================================================
REM start_postgres_stack.bat
REM Starts Postgres stack via docker-compose and waits until ready.
REM Requires: docker CLI, docker-compose
REM ============================================================================

setlocal enabledelayedexpansion

set COMPOSE_FILE=docker-compose-postgres.yml
set ENV_FILE=dev-env.properties

echo --- Starting Postgres container ---

REM ----------------------------------------------------------------------------
REM Check if Docker daemon is running
REM ----------------------------------------------------------------------------
echo Checking Docker daemon status...
docker info >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker daemon is not running or not accessible. Please start Docker.
    exit /b 2
)
echo Docker daemon is running.

REM ----------------------------------------------------------------------------
REM Load environment variables from the properties file
REM ----------------------------------------------------------------------------
if not exist "%ENV_FILE%" (
    echo ERROR: Environment file "%ENV_FILE%" not found.
    exit /b 1
)

for /f "tokens=1,* delims==" %%A in (dev-env.properties) do (
    set "%%A=%%B"
)
if "%POSTGRES_PORT%"=="" set POSTGRES_PORT=5432

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

echo "Waiting for Postgres service to be healthy and listening on %POSTGRES_HOST%:%POSTGRES_PORT%..."

REM ----------------------------------------------------------------------------
REM Wait until pg_isready inside the container reports ready
REM ----------------------------------------------------------------------------
set /a RETRIES=30
set /a COUNT=0

:WAIT_LOOP
REM Get Postgres container ID fresh each loop
set CONTAINER_ID=
for /f "tokens=*" %%i in ('docker ps --filter "ancestor=timescale/timescaledb:latest-pg15" --format "{{.ID}}"') do (
    set CONTAINER_ID=%%i
)

if not defined CONTAINER_ID (
    echo ERROR: No running timescale/timescaledb:latest-pg15 container found. Waiting for container to start...
    timeout /t 2 >nul
    set /a COUNT+=1
    if !COUNT! GEQ %RETRIES% (
        echo ERROR: No running timescale/timescaledb:latest-pg15 container found after %RETRIES% attempts! Aborting.
        exit /b 4
    )
    goto WAIT_LOOP
)

for /f "tokens=*" %%h in ('docker inspect --format "{{.State.Health.Status}}" %CONTAINER_ID% 2^>nul') do (
    set CONTAINER_HEALTH=%%h
)

docker exec %CONTAINER_ID% pg_isready -U %POSTGRES_SUPER_USER_NAME% -d %POSTGRES_DB_NAME% >nul 2>&1
set PG_READY=%ERRORLEVEL%

if "%CONTAINER_HEALTH%"=="healthy" if %PG_READY% EQU 0 (
    echo Postgres is ready and healthy.
    goto RUN_DB_INIT
)

set /a COUNT+=1
if %COUNT% GEQ %RETRIES% (
    echo ERROR: Postgres did not become ready after %RETRIES% attempts.
    exit /b 4
)

echo Waiting for Postgres to be ready... (%COUNT%/%RETRIES%)
timeout /t 2 >nul
goto WAIT_LOOP

:RUN_DB_INIT
REM ----------------------------------------------------------------------------
REM Run the idempotent DB user creation script
REM ----------------------------------------------------------------------------
if not exist create-promptchain-db-user.bat (
    echo ERROR: DB init script "create-promptchain-db-user.bat" not found.
    exit /b 5
)

echo Running DB init script: create-promptchain-db-user.bat
call create-promptchain-db-user.bat
if errorlevel 1 (
    echo ERROR: DB init script failed.
    exit /b 6
)

echo DB init script completed successfully.

echo --- Docker Compose Postgres Stack Deployment Complete ---

endlocal
exit /b 0

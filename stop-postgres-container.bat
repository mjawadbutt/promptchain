@echo off
REM ============================================================================
REM stop_postgres_stack.bat
REM Stops and removes the Postgres stack via docker-compose.
REM Requires: docker CLI, docker-compose
REM ============================================================================

setlocal enabledelayedexpansion

set COMPOSE_FILE=docker-compose-postgres.yml

echo --- Stopping Postgres stack ---

REM ----------------------------------------------------------------------------
REM Check Docker daemon
REM ----------------------------------------------------------------------------
echo Checking Docker daemon status...
docker info >nul 2>&1
if errorlevel 1 (
    echo ERROR: Docker daemon is not running or not accessible. Please start Docker.
    exit /b 1
)
echo Docker daemon is running.

REM ----------------------------------------------------------------------------
REM Stop and remove the Docker Compose stack
REM ----------------------------------------------------------------------------
echo Stopping Docker-Compose services from "%COMPOSE_FILE%"...
docker-compose -f "%COMPOSE_FILE%" down
if errorlevel 1 (
    echo ERROR: Failed to stop Docker-Compose services.
    exit /b 2
)
echo Docker-Compose services stopped and removed successfully.

endlocal
exit /b 0

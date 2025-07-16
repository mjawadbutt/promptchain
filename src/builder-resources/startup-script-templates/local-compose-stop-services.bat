@echo off
echo Stopping Docker Compose services for local development...
docker compose -f docker-compose.yml -f docker-compose.override.yml down
if %errorlevel% neq 0 (
    echo Error stopping services. Exiting.
    exit /b %errorlevel%
)
echo Docker Compose services stopped.

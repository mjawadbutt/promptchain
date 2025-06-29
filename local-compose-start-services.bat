@echo off
echo Starting Docker Compose services for local development...
docker compose -f docker-compose.yml -f docker-compose.override.yml up -d postgres redis app
if %errorlevel% neq 0 (
    echo Error starting core services. Exiting.
    exit /b %errorlevel%
)
echo Docker Compose services started in detached mode.

REM --- Database Initialization Step ---
echo Waiting for PostgreSQL to be healthy before running database initialization...
REM Ensure postgres is fully up and healthy before trying to run db-init
docker compose -f docker-compose.yml -f docker-compose.override.yml wait postgres --health-status healthy
if %errorlevel% neq 0 (
    echo Error waiting for Postgres healthcheck. Exiting.
    exit /b %errorlevel%
)

echo Running Database Initialization (myappuser_local and mydb creation)...
REM The --profile db-setup is crucial here to include the db-init service
docker compose -f docker-compose.yml -f docker-compose.override.yml --profile db-setup run --rm db-init
if %errorlevel% neq 0 (
    echo Error during database initialization. Exiting.
    exit /b %errorlevel%
)
echo Database initialization complete (or skipped if user/db already exist).
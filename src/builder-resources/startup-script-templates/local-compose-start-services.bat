@echo off
REM #################################################################################################################
REM This script is also used by the maven execution: 'local-compose-start-services' for starting services during
REM the verify (integration test execution) phase.
REM #################################################################################################################

SET GHCRIO_USERNAME=@ghcrio.username@
SET PROJECT_ARTIFACT_ID=@project.artifactId@
SET PROJECT_VERSION=@project.version@
SET POSTGRES_DB_NAME=@postgres.db_name_local@
SET POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_local@
SET POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_local@
SET APP_DB_NAME=@app_db.db_name_local@
SET APP_DB_USER_NAME=@app_db.user_name_local@
SET APP_DB_USER_PASSWORD=@app_db.user_password_local@

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

echo Running Database Initialization (promptchain_db_user and mydb creation)...
REM The --profile db-setup is crucial here to include the db-init service
docker compose -f docker-compose.yml -f docker-compose.override.yml --profile db-setup run --rm db-init
if %errorlevel% neq 0 (
    echo Error during database initialization. Exiting.
    exit /b %errorlevel%
)
echo Database initialization complete (or skipped if user/db already exist).
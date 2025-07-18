@echo off
REM #################################################################################################################
REM This script is also used by the maven execution: 'local-compose-start-services' for starting services during
REM the verify (integration test execution) phase.
REM #################################################################################################################

SET DOCKER_BUILDKIT=1
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
docker compose -f docker-compose.yml -f docker-compose.override.yml up -d --wait postgres redis
if %errorlevel% neq 0 (
    echo Error starting core services. Exiting.
    exit /b %errorlevel%
)
echo Docker Compose services started in detached mode.

echo Waiting for postgres to be healthy...
REM This will wait for the postgres container to be healthy before proceeding
docker compose -f docker-compose.yml -f docker-compose.override.yml wait postgres
if %errorlevel% neq 0 (
    echo Postgres failed health check. Exiting.
    exit /b %errorlevel%
)

REM TODO: Can also run it directly without definine a compose service:
REM docker compose exec app bash /usr/local/bin/init-db.sh
echo Running Database Initialization (promptchain_db_user and mydb creation)...
REM The --profile db-setup is crucial here to include the db-init service
docker compose -f docker-compose.yml -f docker-compose.override.yml --profile db-setup run --rm db-init
if %errorlevel% neq 0 (
    echo Error during database initialization. Exiting.
    exit /b %errorlevel%
)
echo Database initialization complete (or skipped if user/db already exist).

echo Starting application...
REM Now that DB is ready and initialized, start the app container
docker compose -f docker-compose.yml -f docker-compose.override.yml up -d --wait app
if %errorlevel% neq 0 (
    echo Failed to start application. Exiting.
    exit /b %errorlevel%
)
echo Application started successfully.

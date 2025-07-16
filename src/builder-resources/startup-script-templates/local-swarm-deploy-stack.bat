@echo off
echo Deploying the Docker Swarm stack to production...

SET GHCRIO_USERNAME=@ghcrio.username@
SET PROJECT_ARTIFACT_ID=@project.artifactId@
SET PROJECT_VERSION=@project.version@
SET POSTGRES_DB_NAME=@postgres.db_name_prod@
SET POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_prod@
SET POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_prod@
SET APP_DB_NAME=@app_db.db_name_prod@
SET APP_DB_USER_NAME=@app_db.user_name_prod@
SET APP_DB_USER_PASSWORD=@app_db.user_password_prod@

REM --- 1. Check required environment variables ---
REM if "%GHCRIO_USERNAME%"=="" (
REM   echo Error: GHCRIO_USERNAME environment variable is not set.
REM   echo Please set it before deploying the stack (e.g., set GHCRIO_USERNAME=your_username).
REM   exit /b 1
REM )

REM --- 2. Deploy the main Docker Swarm stack ---
REM This creates the 'promptchain-stack_app-net' overlay network and starts the core services.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml promptchain-stack
if %errorlevel% neq 0 (
    echo Error deploying main stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'promptchain-stack' deployment initiated.
echo Waiting a moment for services to settle...
timeout /t 10 /nobreak > nul

REM --- 3. Run the one-off Database Initialization ---
echo Running Production Database Initialization (creates and configures the app's db-user)...
REM We run this using 'docker compose run' (not 'docker stack deploy') for a one-off task.
REM It will connect to the existing 'promptchain-stack_app-net' network.
docker compose -f docker-compose.db-init.prod.yml run --rm db-init
if %errorlevel% neq 0 (
    echo Error during database initialization. Exiting.
    exit /b %errorlevel%
)

echo Production Database initialization complete (or skipped if already initialized).
echo You can check the status of your stack with: docker stack ps promptchain-stack

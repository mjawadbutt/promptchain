@echo off
echo Deploying Docker Swarm stack to production...

REM --- 1. Check required environment variables ---
if "%GHCRIO_USERNAME%"=="" (
  echo Error: GHCRIO_USERNAME environment variable is not set.
  echo Please set it before deploying the stack (e.g., set GHCRIO_USERNAME=your_username).
  exit /b 1
)
if "%POSTGRES_SUPER_PASSWORD_PROD%"=="" (
  echo Error: POSTGRES_SUPER_PASSWORD_PROD environment variable is not set.
  echo This is required for the database initialization step. Please set it securely.
  exit /b 1
)

REM --- 2. Deploy the main Docker Swarm stack ---
REM This creates the 'myapp_stack_app-net' overlay network and starts the core services.
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml myapp_stack
if %errorlevel% neq 0 (
    echo Error deploying main stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'myapp_stack' deployment initiated.
echo Waiting a moment for services to settle...
timeout /t 10 /nobreak > nul

REM --- 3. Run the one-off Database Initialization ---
echo Running Production Database Initialization (myappuser_prod and schema setup)...
REM We run this using 'docker compose run' (not 'docker stack deploy') for a one-off task.
REM It will connect to the existing 'myapp_stack_app-net' network.
REM The 'depends_on: service_healthy' in db-init.prod.yml will ensure Postgres is ready.
REM Note: POSTGRES_SUPER_PASSWORD_PROD is automatically passed to the environment for this command.
docker compose -f docker-compose.db-init.prod.yml run --rm db-init
if %errorlevel% neq 0 (
    echo Error during database initialization. Exiting.
    exit /b %errorlevel%
)

echo Production Database initialization complete (or skipped if already initialized).
echo You can check the status of your stack with: docker stack ps myapp_stack
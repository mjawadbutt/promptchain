@echo off
setlocal enabledelayedexpansion

REM --- Config ---
set REQUIRED_VARS=POSTGRES_DB_NAME POSTGRES_SUPER_USER_NAME APP_DB_NAME APP_DB_USER_NAME APP_DB_USER_PASSWORD
set POSTGRES_PORT=%POSTGRES_PORT%
if "%POSTGRES_PORT%"=="" set POSTGRES_PORT=5432

echo --- Starting create-app-db-user script inside container ---

REM --- Check required environment variables ---
for %%V in (%REQUIRED_VARS%) do (
    if "!%%V!"=="" (
        echo ERROR: Required environment variable %%V is not set
        exit /b 1
    )
)

REM --- Find container ID ---
for /f "delims=" %%i in ('docker ps --filter "ancestor=timescale/timescaledb:latest-pg15" --format "{{.ID}}"') do (
    set container_id=%%i
    goto :found_container
)
:found_container
if "%container_id%"=="" (
    echo ERROR: No running ancestor=timescale/timescaledb:latest-pg15 container found! Aborting.
    exit /b 2
)
echo Using container ID: %container_id%

REM --- Wait for Postgres readiness ---
set MAX_RETRIES=10
set RETRY_INTERVAL=3
set /a count=1
:wait_loop
docker exec %container_id% pg_isready -U %POSTGRES_SUPER_USER_NAME% -d %POSTGRES_DB_NAME% >nul 2>&1
if %errorlevel%==0 (
    echo PostgreSQL is up. Proceeding with setup.
    goto :ready
) else (
    if %count% GEQ %MAX_RETRIES% (
        echo ERROR: PostgreSQL not ready after %MAX_RETRIES% attempts! Aborting.
        exit /b 3
    )
    echo PostgreSQL unavailable — retrying in %RETRY_INTERVAL% seconds (attempt %count%/%MAX_RETRIES%)
    timeout /t %RETRY_INTERVAL% >nul
    set /a count+=1
    goto :wait_loop
)

:ready
REM --- Create DB if not exists ---
docker exec %container_id% psql -p %POSTGRES_PORT% -U %POSTGRES_SUPER_USER_NAME% -d %POSTGRES_DB_NAME% -tc "SELECT 1 FROM pg_database WHERE datname = '%APP_DB_NAME%'" | findstr /c:"1" >nul
if %errorlevel%==0 (
    echo Database '%APP_DB_NAME%' already exists. Skipping creation.
) else (
    echo Creating database '%APP_DB_NAME%'...
    docker exec %container_id% psql -p %POSTGRES_PORT% -U %POSTGRES_SUPER_USER_NAME% -d %POSTGRES_DB_NAME% -c "CREATE DATABASE \"%APP_DB_NAME%\";"
)

REM --- Create user if not exists ---
docker exec %container_id% psql -p %POSTGRES_PORT% -U %POSTGRES_SUPER_USER_NAME% -d %APP_DB_NAME% -tc "SELECT 1 FROM pg_roles WHERE rolname = '%APP_DB_USER_NAME%'" | findstr /c:"1" >nul
if %errorlevel%==0 (
    echo User '%APP_DB_USER_NAME%' already exists. Skipping creation.
) else (
    echo Creating user '%APP_DB_USER_NAME%'...
    docker exec %container_id% psql -p %POSTGRES_PORT% -U %POSTGRES_SUPER_USER_NAME% -d %APP_DB_NAME% -c "CREATE USER \"%APP_DB_USER_NAME%\" WITH PASSWORD '%APP_DB_USER_PASSWORD%';"
)

REM --- Grant privileges (always) ---
set SQL_COMMANDS=GRANT CONNECT ON DATABASE "%APP_DB_NAME%" TO "%APP_DB_USER_NAME%";^
 GRANT CREATE ON DATABASE "%APP_DB_NAME%" TO "%APP_DB_USER_NAME%";^
 GRANT USAGE, CREATE ON SCHEMA public TO "%APP_DB_USER_NAME%";^
 ALTER DEFAULT PRIVILEGES FOR USER "%APP_DB_USER_NAME%" IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE, TRUNCATE, REFERENCES ON TABLES TO "%APP_DB_USER_NAME%";^
 ALTER DEFAULT PRIVILEGES FOR USER "%APP_DB_USER_NAME%" IN SCHEMA public GRANT SELECT, USAGE ON SEQUENCES TO "%APP_DB_USER_NAME%";^
 ALTER DEFAULT PRIVILEGES FOR USER "%APP_DB_USER_NAME%" IN SCHEMA public GRANT EXECUTE ON FUNCTIONS TO "%APP_DB_USER_NAME%";^
 GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO "%APP_DB_USER_NAME%";^
 GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO "%APP_DB_USER_NAME%";^
 GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO "%APP_DB_USER_NAME%";^
 CREATE EXTENSION IF NOT EXISTS timescaledb;

docker exec %container_id% psql -p %POSTGRES_PORT% -U %POSTGRES_SUPER_USER_NAME% -d %APP_DB_NAME% -v ON_ERROR_STOP=1 -c "%SQL_COMMANDS%"

echo --- create-promptchain-db-user script completed successfully ---
exit /b 0

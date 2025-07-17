@echo off
echo Stopping Docker Compose services for local development...

SET GHCRIO_USERNAME=@ghcrio.username@
SET PROJECT_ARTIFACT_ID=@project.artifactId@
SET PROJECT_VERSION=@project.version@
SET POSTGRES_DB_NAME=@postgres.db_name_local@
SET POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_local@
SET POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_local@
SET APP_DB_NAME=@app_db.db_name_local@
SET APP_DB_USER_NAME=@app_db.user_name_local@
SET APP_DB_USER_PASSWORD=@app_db.user_password_local@

docker compose -f docker-compose.yml -f docker-compose.override.yml down
if %errorlevel% neq 0 (
    echo Error stopping services. Exiting.
    exit /b %errorlevel%
)
echo Docker Compose services stopped.

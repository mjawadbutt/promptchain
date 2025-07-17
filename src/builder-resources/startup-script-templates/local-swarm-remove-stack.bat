@echo off
echo Removing Docker Swarm stack from production...

SET GHCRIO_USERNAME=@ghcrio.username@
SET PROJECT_ARTIFACT_ID=@project.artifactId@
SET PROJECT_VERSION=@project.version@
SET POSTGRES_DB_NAME=@postgres.db_name_prod@
SET POSTGRES_SUPER_USER_NAME=@postgres.super_user_name_prod@
SET POSTGRES_SUPER_USER_PASSWORD=@postgres.super_user_password_prod@
SET APP_DB_NAME=@app_db.db_name_prod@
SET APP_DB_USER_NAME=@app_db.user_name_prod@
SET APP_DB_USER_PASSWORD=@app_db.user_password_prod@

REM 'promptchain-stack' is the name of your Docker Swarm stack to remove.
docker stack rm promptchain-stack
if %errorlevel% neq 0 (
    echo Error removing stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'promptchain-stack' removal initiated.
echo You can check the status with: docker stack ls

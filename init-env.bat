@echo off
REM init-build-env.bat
REM
REM This script initializes environment variables required for Maven builds
REM (mvn install, mvn deploy) and Docker Compose/Swarm operations.
REM
REM IMPORTANT: Replace placeholder values with your actual credentials.
REM For security, avoid hardcoding sensitive passwords directly in version control.
REM Consider sourcing these from a secure local environment file (e.g., .env)
REM or injecting them via your CI/CD pipeline's secret management.

echo Initializing build environment variables...

REM 1. GitHub Container Registry (GHCR) Username:
REM This is used by the Fabric8 Docker Maven Plugin to tag and push your Docker image.
REM Your pom.xml's 'set-default-ghcrio-username' profile will pick this up if GITHUB_ACTOR is not set.
set GHCRIO_USERNAME="your-github-container-registry-username" REM e.g., your-github-username

REM 2. PostgreSQL Superuser Password for Local Development:
REM Used by 'docker-compose.override.yml' for the 'postgres' service and 'db-init' service.
set POSTGRES_SUPER_PASSWORD_LOCAL="my_super_secret_local_password"

REM 3. PostgreSQL Superuser Password for Production Deployment:
REM Used by 'docker-compose.prod.yml' for the 'postgres' service and by 'docker-compose.db-init.prod.yml'
REM when running the one-off db-init for production.
set POSTGRES_SUPER_PASSWORD_PROD="my_super_secret_prod_password" REM Highly sensitive! Use CI/CD secrets.

echo Environment variables set.
echo   GHCRIO_USERNAME=%GHCRIO_USERNAME%
echo   POSTGRES_SUPER_PASSWORD_LOCAL=********
echo   POSTGRES_SUPER_PASSWORD_PROD=********
echo.
echo To apply these variables to your current Command Prompt/PowerShell, run:
echo   init-build-env.bat
echo After running, you can proceed with 'mvn install', 'mvn deploy', or 'docker stack deploy'.
@echo off
echo =========================================================================================
echo                            🚀 Docker Nuclear Cleanup Script
echo.
echo  Starting Docker clean-up process to achieve a clean slate...
echo  WARNING: This script will stop and remove ALL containers, volumes, networks, and images.
echo  Press Ctrl+C to abort, or wait 5 seconds to proceed...
echo =========================================================================================
timeout /t 5 >nul

:: Step 1: Leave swarm mode if active
echo Checking swarm mode...
docker info 2>nul | findstr /C:"Swarm: active" >nul
if %errorlevel%==0 (
    echo Swarm is active. Leaving swarm...
    docker swarm leave --force
) else (
    echo Swarm is already inactive.
)

:: Step 2: Kill all running containers
echo Stopping all containers...
FOR /F "tokens=*" %%i IN ('docker ps -q') DO docker stop %%i

:: Step 3: Remove all containers
echo Removing all containers...
FOR /F "tokens=*" %%i IN ('docker ps -aq') DO docker rm -f %%i

:: Step 4: Remove all volumes
echo Removing all volumes...
FOR /F "tokens=*" %%i IN ('docker volume ls -q') DO docker volume rm %%i

:: Step 5: Full prune (images, volumes, build cache)
echo Pruning system...
docker system prune -a --volumes --force

echo ============================================
echo   ✅ Docker environment fully cleaned!
echo ============================================

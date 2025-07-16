@echo off
echo Removing Docker Swarm stack from production...

REM 'promptchain-stack' is the name of your Docker Swarm stack to remove.
docker stack rm promptchain-stack
if %errorlevel% neq 0 (
    echo Error removing stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'promptchain-stack' removal initiated.
echo You can check the status with: docker stack ls

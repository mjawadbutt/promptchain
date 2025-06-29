@echo off
echo Removing Docker Swarm stack from production...

REM 'myapp_stack' is the name of your Docker Swarm stack to remove.
docker stack rm myapp_stack
if %errorlevel% neq 0 (
    echo Error removing stack. Exiting.
    exit /b %errorlevel%
)

echo Docker Swarm stack 'myapp_stack' removal initiated.
echo You can check the status with: docker stack ls
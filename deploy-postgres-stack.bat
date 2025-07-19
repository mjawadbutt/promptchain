:: start_postgres_stack.bat
:: This script starts the Docker Swarm Postgres stack.
:: Ensure this script is in the same directory as docker-compose-postgres.yml and your postgres.env file.

@echo off
setlocal

set STACK_NAME=postgres-stack
set COMPOSE_FILE=docker-compose-postgres.yml
set ENV_FILE=local-compose-postgres.env

echo --- Starting Docker Postgres Stack: %STACK_NAME% ---

:: 1. Check if Docker daemon is running
echo Checking Docker daemon status...
docker info > nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker daemon is not running or not accessible. Please start Docker Desktop/Engine.
    exit /b 1
)
echo Docker daemon is running.

:: 2. Check if Docker Swarm mode is active and initialize if not
echo Checking Docker Swarm mode status...
for /f "tokens=*" %%i in ('docker info --format "{{.Swarm.LocalNodeState}}"') do (
    set SWARM_STATUS=%%i
)

if "%SWARM_STATUS%" neq "active" (
    echo Docker Swarm mode is not active. Initializing Swarm...
    docker swarm init > nul 2>&1
    if %errorlevel% neq 0 (
        echo ERROR: Failed to initialize Docker Swarm. Ensure port 2377 is available and not blocked by firewall.
        exit /b 1
    )
    echo Docker Swarm initialized successfully.
) else (
    echo Docker Swarm mode is already active.
)

:: 3. Deploy the stack
echo Preparing to deploy Docker stack '%STACK_NAME%'...

:: Ensure the .env file exists
if not exist %ENV_FILE% (
    echo ERROR: Environment file '%ENV_FILE%' not found. Please create it with necessary variables.
    echo Example content for '%ENV_FILE%':
    echo POSTGRES_DB_NAME=postgres
    echo POSTGRES_SUPER_USER_NAME=postgres
    echo POSTGRES_SUPER_USER_PASSWORD=postgres
    echo APP_DB_NAME=postgres
    echo APP_DB_USER_NAME=promptchain
    echo APP_DB_USER_PASSWORD=promptchain
    exit /b 1
)

echo Deploying Docker stack '%STACK_NAME%' from '%COMPOSE_FILE%' using environment file '%ENV_FILE%'...
docker stack deploy -c %COMPOSE_FILE% --env-file %ENV_FILE% %STACK_NAME%
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy stack '%STACK_NAME%'.
    exit /b 1
)
echo Stack '%STACK_NAME%' deployment initiated. Use 'docker stack ps %STACK_NAME%' to check status.

echo --- Docker Postgres Stack Deployment Complete ---
endlocal





# start_postgres_stack.ps1
# This script starts the Docker Swarm Postgres stack.
# Ensure this script is in the same directory as docker-compose-postgres.yml and your postgres.env file.





$StackName = "postgres-stack" # The name for your Docker Swarm stack
$ComposeFile = "docker-compose-postgres.yml"
$EnvFile = "local-compose-postgres.env" # Name of your environment variable file

Write-Host "--- Starting Docker Postgres Stack: $StackName ---" -ForegroundColor Green

# 1. Check if Docker daemon is running
Write-Host "Checking Docker daemon status..." -ForegroundColor Cyan
try {
    docker info | Out-Null
    Write-Host "Docker daemon is running." -ForegroundColor Green
} catch {
    Write-Error "Docker daemon is not running or not accessible. Please start Docker Desktop/Engine."
    exit 1
}

# 2. Check if Docker Swarm mode is active and initialize if not
Write-Host "Checking Docker Swarm mode status..." -ForegroundColor Cyan
$SwarmStatus = docker info --format "{{.Swarm.LocalNodeState}}"
if ($SwarmStatus -ne "active") {
    Write-Host "Docker Swarm mode is not active. Initializing Swarm..." -ForegroundColor Yellow
    try {
        docker swarm init | Out-Null
        if ($LASTEXITCODE -ne 0) { throw "Docker Swarm init failed." }
        Write-Host "Docker Swarm initialized successfully." -ForegroundColor Green
    } catch {
        Write-Error "Failed to initialize Docker Swarm. Ensure port 2377 is available and not blocked by firewall."
        exit 1
    }
} else {
    Write-Host "Docker Swarm mode is already active." -ForegroundColor Green
}

# 3. Deploy the stack
Write-Host "Preparing to deploy Docker stack '$StackName'..." -ForegroundColor Cyan

# Ensure the .env file exists
if (-not (Test-Path $EnvFile)) {
    Write-Error "Environment file '$EnvFile' not found. Please create it with necessary variables."
    Write-Host "Example content for '$EnvFile':" -ForegroundColor Yellow
    Write-Host "POSTGRES_DB_NAME=postgres" -ForegroundColor Yellow
    Write-Host "POSTGRES_SUPER_USER_NAME=postgres" -ForegroundColor Yellow
    Write-Host "POSTGRES_SUPER_USER_PASSWORD=postgres" -ForegroundColor Yellow
    Write-Host "APP_DB_NAME=postgres" -ForegroundColor Yellow
    Write-Host "APP_DB_USER_NAME=promptchain" -ForegroundColor Yellow
    Write-Host "APP_DB_USER_PASSWORD=promptchain" -ForegroundColor Yellow
    exit 1
}

Write-Host "Deploying Docker stack '$StackName' from '$ComposeFile' using environment file '$EnvFile'..." -ForegroundColor Cyan
try {
    docker stack deploy -c $ComposeFile --env-file $EnvFile $StackName
    if ($LASTEXITCODE -ne 0) { throw "Docker stack deploy failed." }
    Write-Host "Stack '$StackName' deployment initiated. Use 'docker stack ps $StackName' to check status." -ForegroundColor Green
} catch {
    Write-Error "Failed to deploy stack '$StackName'. Error: $_"
    exit 1
}

Write-Host "--- Docker Postgres Stack Deployment Complete ---" -ForegroundColor Green
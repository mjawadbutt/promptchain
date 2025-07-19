Need to define 2 env vars for local setup to run in compose mode (always debug enabled):

REM 1. GitHub Container Registry (GHCR) Username:
REM This is used in POM.xml as the github username when pushing a Docker image to ghcr.io from local machine.
REM However when pushing via GITHUB-Workflow, this env.GITHUB_ACTOR is used.
set GHCRIO_USERNAME="%GHCRIO_USERNAME%"

REM 2. PostgreSQL Superuser Password for Local Development:
REM Used by 'docker-compose.override.yml' for the 'postgres' service and 'db-init' service.
REM TODO-SECURITY: Use a vault.
set POSTGRES_SUPER_PASSWORD_LOCAL="postgres"



Need the following env var only if you want to run the app in swarm mode ('prod' like but not debuggable)  
REM 3. PostgreSQL Superuser Password for Production Deployment:
REM Used by 'docker-compose.prod.yml' for the 'postgres' service and by 'docker-compose.db-init.prod.yml'
REM when running the one-off db-init for production.
REM TODO-SECURITY: Use a vault.
set POSTGRES_SUPER_PASSWORD_PROD="postgres"


to run in non-swarm mode use:

Force rebuild of image (build) and then start container in background (-d)
docker compose --project-name promptchain_instance1 --build up 

docker-compose up --build
Auto-loads docker-compose.yml + override.yml

Enables debugging, live-reload (if volume mapped), and builds from source

For Production with Swarm:
mvn install 
OR
mvn deploy

THEN to run local like prod:
docker stack deploy -c docker-compose.yml -c docker-compose.prod.yml mystack

Swarm manages orchestration features like scaling, placement, update strategy, and advanced restart policies, which are not part of local Compose's scope

Deploy the Stack
Step 1: Initialize Swarm (if not already)

docker swarm init 

Note: 
If using swarm mode then the network type must be overlay and not bridge. 
This means that now for running compose simple docker compose, the network used in compose file has to be overlay

Overlay network can be created manually also (if swarm enabled):
docker network create -d overlay my-overlay

and deleted if needed:

swarm mode and overlay network created like this will persist across restarts unless explicitly disabled/removed
docker network rm my-overlay
docker swarm leave --force

The network selection can be made smart via ${NETWORK trick so it works for both sawrm and non-swarm


Step 2: Deploy stack (last param is stack name, all services need to be prefixed with this & _ when running any docker command)
docker stack deploy -c docker-compose.yml promptchain

Step 3: Check status
docker service ls
docker service ps promptchain-instance1_app
docker service ps promptchain-instance1_redis

Step 4: Tear down (when done)
docker stack rm promptchain

To force Redis into an unhealthy state and test recovery:

bash
Copy
Edit
docker exec -it $(docker ps --filter name=redis -q) redis-cli DEBUG SEGFAULT


Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
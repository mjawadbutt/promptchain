# Dockerfile
ARG PROJECT_BUILD_DIRECTORY
ARG PROJECT_BUILD_FINAL_NAME

FROM eclipse-temurin:21-jdk-alpine

# Define a non-root user and group
# Using --system creates a system user, suitable for service accounts
# Using --no-create-home prevents creation of a home directory, often not needed for app users
# Setting explicit UIDs/GIDs (e.g., 1001) is good for consistency across builds/environments
RUN addgroup --system --gid 1001 promptchain && \
    adduser --system --uid 1001 --ingroup promptchain --no-create-home promptchain

WORKDIR /app

# Copy Spring Boot JAR and optional config
# Apply --chown to set ownership to the non-root user (appuser:appgroup)
# Permissions for the JAR are typically read-only for security (644)
COPY --chown=promptchain:promptchain --chmod=644 \
     "${PROJECT_BUILD_DIRECTORY}/${PROJECT_BUILD_FINAL_NAME}" \
     "${PROJECT_BUILD_FINAL_NAME}"

# Copy redisson.yaml with appropriate ownership and permissions
COPY --chown=appuser:appgroup --chmod=644 redisson.yaml .

# Copy and set permissions for init-db.sh
# Make it executable for the owner, and readable+executable for group/others (755)
# Also set ownership to the non-root user
COPY --chown=appuser:appgroup --chmod=755 src/main/container-image-resources/init-db.sh /usr/local/bin/init-db.sh

ENV APP_JAR_NAME=${PROJECT_BUILD_FINAL_NAME}

# Set default JVM options (can be overridden by environment variables)
# They are already defined in docker-compose.override.yml but, if needed, we can uncomment have the defaults
# built into the image. Overkill, not needed.
# ENV JAVA_OPTS="-Xms256m -Xmx512m"
# ENV DEBUG_ENABLED=false
# ENV DEBUG_PORT=5005

# Switch to the non-root user before running the application
# All subsequent RUN, CMD, and ENTRYPOINT commands will run as this user
USER appuser

# Entry point with conditional debug logic and proper signal handling
ENTRYPOINT sh -c '
  if [ "${DEBUG_ENABLED}" = "true" ]; then
    echo "Debug mode enabled on port ${DEBUG_PORT}"
    # JAVA_OPTS is left unquoted to allow shell word splitting for multiple options
    # APP_JAR_NAME is quoted as it\'s a single filename argument
    exec java ${JAVA_OPTS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} -jar "${APP_JAR_NAME}"
  else
    echo "Starting app normally"
    exec java ${JAVA_OPTS} -jar "${APP_JAR_NAME}"
  fi
'

# Dockerfile
FROM eclipse-temurin:21-jdk-alpine AS base

ARG PROJECT_BUILD_DIRECTORY
ARG PROJECT_BUILD_FINAL_NAME
ARG APP_DATA_DIR_NAME
ARG APP_LOGS_DIR_NAME

RUN apk add --no-cache postgresql-client

# Define a non-root user and group
# Using --system creates a system user, suitable for service accounts
# Using --no-create-home prevents creation of a home directory, often not needed for app users
# Setting explicit UIDs/GIDs (e.g., 1001) is good for consistency across builds/environments
RUN addgroup --system --gid 1001 promptchain && \
    adduser --system --uid 1001 --ingroup promptchain --no-create-home promptchain

WORKDIR /app

RUN mkdir -p /${APP_LOGS_DIR_NAME} /${APP_DATA_DIR_NAME} && \
    chown -R promptchain:promptchain /${APP_LOGS_DIR_NAME} /${APP_DATA_DIR_NAME} && \
    chmod -R 755 /${APP_LOGS_DIR_NAME} /${APP_DATA_DIR_NAME}

# Copy Spring Boot JAR and optional config
# Apply --chown to set ownership to the non-root user (promptchain:promptchain)
# Permissions for the JAR are typically read-only for security (644)
COPY --chown=promptchain:promptchain --chmod=644 \
     "${PROJECT_BUILD_DIRECTORY}/${PROJECT_BUILD_FINAL_NAME}" \
     /app/"${PROJECT_BUILD_FINAL_NAME}"

# Copy the entrypoint script into the container
# Make it executable and set ownership
COPY --chown=promptchain:promptchain --chmod=755 src/main/container-resources/entrypoint.sh /app/entrypoint.sh

# --- NEW: Convert line endings if coming from a Windows host ---
# Option 1: Using dos2unix (recommended for clarity)
RUN apk add --no-cache dos2unix  \
    && dos2unix /app/entrypoint.sh

ENV APP_JAR_NAME=${PROJECT_BUILD_FINAL_NAME}

# Set default JVM options (can be overridden by environment variables)
# They are already defined in docker-compose.override.yml but, if needed, we can uncomment have the defaults
# built into the image. Overkill, not needed.
# ENV JAVA_OPTS="-Xms256m -Xmx512m"
# ENV DEBUG_ENABLED=false
# ENV DEBUG_PORT=5005

# Switch to the non-root user before running the application
# All subsequent RUN, CMD, and ENTRYPOINT commands will run as this user
USER promptchain

# --- NEW: Use the copied entrypoint script ---
# Use the exec form of ENTRYPOINT for proper signal handling
ENTRYPOINT ["/app/entrypoint.sh"]

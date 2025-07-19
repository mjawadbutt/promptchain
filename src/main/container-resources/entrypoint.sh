#!/bin/sh

# This script handles the conditional debug logic for the Spring Boot application.

if [ "${RUN_IN_DEBUG_MODE}" = "true" ]; then
  echo "Debug mode enabled on port ${DEBUG_PORT}"
  # Use 'exec' to replace the current shell process with the Java process,
  # ensuring proper signal handling (e.g., SIGTERM from Docker stop).
  # JAVA_OPTS is left unquoted to allow shell word splitting for multiple options.
  # APP_JAR_NAME is quoted as it's a single filename argument.
  exec java "${JAVA_OPTS}" -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:${DEBUG_PORT} -jar "${APP_JAR_NAME}"
else
  echo "Starting app normally"
  # Use 'exec' for normal startup as well, for consistent signal handling.
  exec java "${JAVA_OPTS}" -jar "${APP_JAR_NAME}"
fi
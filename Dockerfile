# Dockerfile
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Spring Boot JAR and optional config
COPY target/my-app.jar app.jar
COPY redisson.yaml .

# # Set default JVM options (can be overridden by environment variables)
# ENV JAVA_OPTS="-Xms256m -Xmx512m"
#
# # Enable remote debugging conditionally
# ENV DEBUG_ENABLED=false
# ENV DEBUG_PORT=5005

# Entry point with conditional debug logic and proper signal handling
ENTRYPOINT sh -c '
  if [ "$DEBUG_ENABLED" = "true" ]; then
    echo "Debug mode enabled on port $DEBUG_PORT"
    exec java $JAVA_OPTS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$DEBUG_PORT -jar app.jar
  else
    echo "Starting app normally"
    exec java $JAVA_OPTS -jar app.jar
  fi
'

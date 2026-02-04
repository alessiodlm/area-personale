
# Base image
FROM eclipse-temurin:17-jre


# Environment variables
ENV APP_NAME=area-personale \
    APP_HOME=/app \
    JAVA_OPTS="-Xms256m -Xmx512m"


# Working directory
WORKDIR ${APP_HOME}


# Copy application artifact
COPY target/area-personale-0.0.1-SNAPSHOT.jar app.jar


# Runtime setup (optional but good practice)
RUN useradd -ms /bin/bash appuser

USER appuser


# Expose application port
EXPOSE 8080


# Container entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

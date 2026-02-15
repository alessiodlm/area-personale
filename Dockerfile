FROM eclipse-temurin:17-jre

ENV APP_NAME=area-personale \
    APP_HOME=/app

WORKDIR ${APP_HOME}

COPY target/area-personale-0.0.1-SNAPSHOT.jar app.jar

RUN useradd appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

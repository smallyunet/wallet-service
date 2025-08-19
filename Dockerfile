# ===== Build stage =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package

# ===== Run stage =====
FROM eclipse-temurin:21-jre-jammy
ENV APP_HOME=/opt/app
WORKDIR ${APP_HOME}

# Install curl for healthcheck
USER root
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN useradd -ms /bin/bash appuser

# Copy fat jar
COPY --from=build /app/target/wallet-server-0.0.1-SNAPSHOT.jar app.jar

# Prepare log dir and switch user
ENV APP_LOG_DIR=${APP_HOME}/logs
RUN mkdir -p ${APP_LOG_DIR} && chown -R appuser:appuser ${APP_HOME}
USER appuser

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s   CMD curl -fs http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-XX:+UseZGC","-Xms256m","-Xmx512m","-jar","app.jar"]

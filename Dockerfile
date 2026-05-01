# Stage 1: Build
FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY backend/ ./backend/
RUN gradle -p backend build -x test

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Revised COPY command:
# This looks for any jar in the libs folder and renames it to app.jar
COPY --from=builder /app/backend/build/libs/*.jar ./app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

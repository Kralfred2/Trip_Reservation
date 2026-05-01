FROM gradle:8-jdk21 AS builder
WORKDIR /app
COPY backend/ ./backend/
RUN gradle -p backend build -x test

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/backend/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# Dockerfile для приложения Java# Используем базовый образ OpenJDK 17 с минимальным размером
FROM openjdk:17-jdk-slim AS build

COPY src/Effortly-Time-Tracker/target/Effortly-Time-Tracker-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]


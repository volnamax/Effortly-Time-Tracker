FROM openjdk:17-jdk-slim AS build

COPY Effortly-Time-Tracker-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app.jar"]

# Используем официальный образ Gradle для сборки проекта
FROM openjdk:17-jdk-slim AS build

# Копируем JAR файл из локальной папки в образ
COPY ./src/Effortly-Time-Tracker/target/Effortly-Time-Tracker-0.0.1-SNAPSHOT.jar /app.jar

# Открываем порт
EXPOSE 8088

# Команда для запуска JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]

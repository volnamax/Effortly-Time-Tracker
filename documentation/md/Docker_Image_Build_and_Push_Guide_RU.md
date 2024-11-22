
# Инструкция по созданию и загрузке образа Docker в Docker Hub

Эта инструкция поможет вам создать Docker-образ и загрузить его в Docker Hub.

## Шаг 1: Подготовка Dockerfile

Убедитесь, что у вас есть файл `Dockerfile`, который описывает, как собирать ваш Docker-образ. Пример файла `Dockerfile`:

```Dockerfile
FROM openjdk:17-jdk-slim AS build

COPY ../../src/Effortly-Time-Tracker/target/Effortly-Time-Tracker-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8088

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## Шаг 2: Создание Docker-образа

Выполните следующую команду в терминале из корневой директории проекта, где находится `Dockerfile`:

```bash
docker build -t volnamax1/effortly-time-tracker:latest -f docker/Dockerfile .
```

Эта команда создаёт образ с тегом `latest` для вашего проекта.

## Шаг 3: Вход в Docker Hub

Перед загрузкой образа на Docker Hub вам нужно войти в свою учётную запись:

```bash
docker login
```

Введите свои логин и пароль от Docker Hub, когда будет предложено.

## Шаг 4: Загрузка Docker-образа в Docker Hub

После успешного входа в систему, вы можете загрузить образ в Docker Hub, используя следующую команду:

```bash
docker push volnamax1/effortly-time-tracker:latest



 docker tag 4660496684b8 volnamax1/docker-spring-boot-postgres:latest
PS D:\service\docker> docker push volnamax1/docker-spring-boot-postgres:latest

```

Эта команда загрузит образ с тегом `latest` в ваш репозиторий на Docker Hub.

## Завершение

Теперь ваш Docker-образ доступен в Docker Hub, и вы можете использовать его для развертывания в облачных сервисах или на других серверах.

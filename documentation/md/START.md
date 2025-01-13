
# Инструкция по запуску проекта

Этот проект использует Docker для управления базой данных PostgreSQL и приложением Spring Boot. Также настроен запуск тестов с использованием Testcontainers и генерация отчетов Allure.

## Предварительные требования

Убедитесь, что установлены следующие компоненты:

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- [Maven](https://maven.apache.org/install.html)
- [Allure Commandline](https://docs.qameta.io/allure/#_installing_a_commandline)

## Структура проекта

- **Docker**: используется для запуска PostgreSQL и Spring Boot приложения.
- **Maven**: управляет зависимостями и выполняет запуск приложения и тестов.
- **Allure**: генерирует и отображает отчеты по тестам.

---

## Запуск проекта

### 1. Запуск контейнеров Docker

Чтобы запустить базу данных PostgreSQL и приложение Spring Boot с помощью Docker, выполните следующие шаги:

```bash
mvn clean package -DskipTests
```



1. **Перейдите в директорию с Docker-файлами**:

   ```bash
   cd docker
   ```

2. **Соберите и запустите Docker-контейнеры**:

   **Команда**  
   `docker-compose up --build` 

   Эта команда создаст и запустит контейнеры для базы данных PostgreSQL и приложения Spring Boot.

   Чтобы остановить контейнеры, выполните команду:

   **Команда**  
   `docker-compose down`

---

### 2. Запуск тестов

Проект настроен на выполнение тестов с использованием `maven-surefire-plugin` и `Testcontainers` для работы с базой данных PostgreSQL.

**Включение повторного использования Testcontainers** (опционально)

Включите повторное использование контейнеров PostgreSQL, добавив следующую строку в файл `~/.testcontainers.properties`:

**Настройка**  
`testcontainers.reuse.enable=true`

**Запуск тестов**

Для запуска тестов и генерации результатов Allure выполните команду:

**Команда**  
`mvn clean test`

Тесты выполняются параллельно в несколько потоков согласно настройкам Maven.

```bash
PS D:\service\src\Effortly-Time-Tracker\.allure\allure-2.30.0\bin> ./allure serve D:\service\src\Effortly-Time-Tracker\target\allure-results  --host localhost --port 9999
```

---

### 3. Генерация отчетов Allure

Проект настроен для вывода отчетов тестирования в формате Allure. Следуйте инструкциям ниже для генерации и просмотра отчета.

1. **Создайте отчет Allure**

   Сначала выполните команду для очистки предыдущих результатов и запуска тестов:

   **Команда**  
   `mvn clean test`

2. **Генерация отчета Allure**

   После выполнения тестов создайте отчет с помощью следующей команды:

   **Команда**  
   `mvn allure:report`

резултат будет  в target/allure-report 

3. **Запустите сервер для отображения отчета Allure**

   Для локального просмотра отчета в браузере выполните команду:

   **Команда**  
   `mvn allure:serve`

   Эта команда откроет локальный сервер с отчетом Allure.

---

### Конфигурация Maven для тестов

В `maven-surefire-plugin` настроено параллельное выполнение тестов и оптимизация памяти JVM:

- **Параллельное выполнение**: тесты выполняются на уровне классов с фиксированным количеством потоков (4).
- **Настройки памяти**: JVM конфигурируется с параметрами `-Xms256m -Xmx1024m` и сборщиком мусора G1.
- **Случайный порядок тестов**: тесты запускаются в случайном порядке для повышения устойчивости.

---

### Переменные окружения

При необходимости измените переменные окружения в `docker-compose.yml` или файле свойств приложения:

- **URL базы данных**: `spring.datasource.url=jdbc:postgresql://localhost:5432/postgres`
- **Учетные данные для базы данных**:
  - `spring.datasource.username=postgres`
  - `spring.datasource.password=1564`
- **MongoDB URI (если используется)**: `spring.data.mongodb.uri=mongodb://localhost:27017/effortlyTimeTrackerDB`

---

### Отключение MongoDB при ненадобности

Для предотвращения автоматической загрузки MongoDB при работе с PostgreSQL, в файле `EffortlyTimeTrackerApplication.java` следует исключить автоматическую конфигурацию MongoDB:

```java
@SpringBootApplication(exclude = {
    MongoAutoConfiguration.class,
    MongoDataAutoConfiguration.class
})
public class EffortlyTimeTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EffortlyTimeTrackerApplication.class, args);
    }
}
```

флаг в application.properties

spring.profiles.active=postgres


---

### Устранение неполадок

- **Проблемы с Docker**: если контейнеры Docker не запускаются, убедитесь, что Docker запущен и порты не заняты.
- **Ошибки при запуске тестов**: проверьте настройки Testcontainers и убедитесь, что повторное использование контейнеров включено.
- **Проблемы с отчетом Allure**: убедитесь, что Allure Commandline установлен и доступен в системном PATH.

---

Это завершает инструкцию по настройке Docker, запуску тестов и отчетности для данного проекта.

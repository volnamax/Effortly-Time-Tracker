
# Инструкция по настройке Swagger UI с Docker и интеграцией с Spring Boot

Эта инструкция описывает шаги по настройке Swagger UI с использованием Docker и автоматической генерацией Swagger JSON с помощью Maven-плагина `springdoc-openapi`. Кроме того, мы добавим конфигурацию CORS для работы с порта `8081` в Spring Boot приложении.

## Шаг 1: Настройка Docker для Swagger UI

Создайте `docker-compose.yml` в папке проекта и добавьте следующую конфигурацию:

```yaml
version: '3.8'

services:
  swagger-ui:
    image: nginx:1.27
    container_name: swagger-ui
    volumes:
      - ./swagger:/usr/share/nginx/html  # Подключаем папку swagger с JSON файлом
    ports:
      - "8081:80"  # Поднимаем Swagger UI на порту 8081
```

Эта конфигурация поднимает контейнер с NGINX, который будет обслуживать Swagger UI, подключенный к локальной папке `swagger`, где будет находиться сгенерированный `swagger.json`.

## Шаг 2: Настройка Maven для автоматической генерации Swagger JSON

В `pom.xml` добавьте плагин `springdoc-openapi-maven-plugin`, который будет генерировать файл `swagger.json` при запуске команды Maven.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-maven-plugin</artifactId>
            <version>1.4</version>
            <executions>
                <execution>
                    <id>generate-openapi-docs</id>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <apiDocsUrl>http://localhost:8080/v3/api-docs</apiDocsUrl>
                <outputDir>${project.basedir}/../../docker/swagger</outputDir>
                <outputFileName>swagger.json</outputFileName>
            </configuration>
        </plugin>
    </plugins>
</build>
```

> **Примечание**: Путь `${project.basedir}/../../docker/swagger` настроен так, чтобы `swagger.json` генерировался в нужную папку относительно текущего проекта. Подстройте путь при необходимости, если ваша структура директорий отличается.

## Шаг 3: Настройка CORS и разрешений в Spring Boot

Для работы Swagger UI на другом порту (8081) добавьте поддержку CORS в конфигурацию безопасности Spring Boot.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("http://localhost:8081")); // Разрешаем запросы с порта 8081
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/register", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/todo/**").hasAnyRole("MANAGER", "USER", "ADMIN")
                        .requestMatchers("/api/groups/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers("/api/projects/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tables/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tasks/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tags/get-all").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // Конфигурация JwtAuthenticationConverter для JWT
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }
}
```

Этот код:
- Отключает CSRF (Cross-Site Request Forgery).
- Включает CORS для порта 8081, с разрешенными методами `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS`.
- Устанавливает правила доступа для различных API-эндпоинтов.

## Шаг 4: Генерация и запуск

1. **Генерация Swagger JSON**: Запустите команду Maven для генерации файла `swagger.json`:
   ```bash
   mvn springdoc-openapi:generate
   ```

   После выполнения этой команды `swagger.json` должен появиться в папке `docker/swagger`.

2. **Запуск Docker**: Поднимите контейнер с Swagger UI с помощью `docker-compose`:
   ```bash
   docker-compose up --build
   ```

3. **Доступ к Swagger UI**: Откройте браузер и перейдите на [http://localhost:8081](http://localhost:8081). Swagger UI должен отображать сгенерированную документацию вашего API.

## Шаг 5: Проверка

- **Swagger UI** должен быть доступен по адресу `http://localhost:8081`.
- **Spring Boot приложение** по-прежнему работает на `http://localhost:8080`, и `/v3/api-docs` доступен для Swagger JSON.
- Убедитесь, что все запросы к API могут выполняться через Swagger UI, проверяя правильность CORS-конфигурации.

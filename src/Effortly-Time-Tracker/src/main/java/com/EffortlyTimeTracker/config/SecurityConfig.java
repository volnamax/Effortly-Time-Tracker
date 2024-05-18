package com.EffortlyTimeTracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean // Определяет метод как источник бина, который управляется контейнером Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        auth -> auth.requestMatchers("/api/auth").permitAll()   // Разрешает доступ к пути /api/auth без аутентификации
                                .requestMatchers("/api/user/get-all").hasRole("ADMIN") // Доступ к /api/user/get-all только для пользователей с ролью ADMIN
                                .anyRequest().authenticated()

                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Устанавливает политику управления сессиями как STATELESS, что означает отсутствие хранения состояния сессий на сервере.
                ).oauth2ResourceServer(
                        resourceServer -> resourceServer.jwt(
                                jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(
                                        keycloalAuthConvertre() // Использование пользовательского конвертера для преобразования JWT в объект аутентификации
                                )
                        )
                )
                .build();
    }

    private Converter<Jwt, ? extends AbstractAuthenticationToken> keycloalAuthConvertre() {
        return null;
    }
}

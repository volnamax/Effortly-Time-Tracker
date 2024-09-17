package com.EffortlyTimeTracker.config;
// todo define user  = group owner

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Slf4j
@Configuration
public class SecurityConfig {
    //это класс, содержащий публичный и приватный RSA-ключи, которые используются для подписания и верификации JWT токенов.
    private final RsaKeyProperties rsaKeyProperties;

    public SecurityConfig(RsaKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }
    //Компонент SecurityFilterChain: Настраивает цепочку фильтров безопасности для отключения CSRF, авторизации запросов и настройки управления сеансами без состояния.
    //                        .requestMatchers("/api/**").authenticated())

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/login", "/api/register", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/todo/**").hasAnyRole("MANAGER", "USER", "ADMIN")
                        .requestMatchers("/api/groups/**").hasAnyRole("MANAGER", "ADMIN")

                        //  .requestMatchers("/api/todo/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/projects/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tables/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tasks/get-all").hasRole("ADMIN")
                        .requestMatchers("/api/tags/get-all").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    //Компонент JwtDecoder: Настраивает декодирование JWT с использованием открытого ключа RSA.
    @Bean
    public JwtDecoder jwtDecoder() {
        try {
            return NimbusJwtDecoder.withPublicKey(rsaKeyProperties.getPublicKey()).build();
        } catch (Exception e) {
            log.error("Error creating JwtDecoder", e);
            throw new RuntimeException("Failed to create JwtDecoder", e);
        }
    }


    //Компонент JwtEncoder: Настраивает кодировку JWT с использованием открытого и закрытого ключей RSA.
    @Bean
    public JwtEncoder jwtEncoder() {
        try {
            RSAPublicKey publicKey = rsaKeyProperties.getPublicKey();
            RSAPrivateKey privateKey = rsaKeyProperties.getPrivateKey();

            if (publicKey == null || privateKey == null) {
                throw new IllegalArgumentException("RSA public or private key is null. Ensure the keys are correctly configured.");
            }

            JWKSource<SecurityContext> jwkSource = createJwkSource(publicKey, privateKey);

            return new NimbusJwtEncoder(jwkSource);
        } catch (IllegalArgumentException e) {
            log.error("Error while creating JwtEncoder: RSA keys are not properly configured", e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while creating JwtEncoder", e);
            throw new RuntimeException("Failed to create JwtEncoder", e);
        }
    }

    // Приватный метод для создания JWKSource из RSAKey
    private JWKSource<SecurityContext> createJwkSource(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    //Компонент AuthenticationManager: Настраивает диспетчер аутентификации, который будет использоваться для аутентификации.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //    Компонент PasswordEncoder: Настраивает кодировщик паролей с помощью BCrypt.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }
}

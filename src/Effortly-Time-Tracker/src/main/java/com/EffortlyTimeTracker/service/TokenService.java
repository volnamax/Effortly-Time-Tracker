package com.EffortlyTimeTracker.service;

import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.mapper.UserMongoMapper;
import com.EffortlyTimeTracker.repository.IUserRepository;
import com.EffortlyTimeTracker.repository.mongo.IMongoUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;
@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;

    private final IUserRepository userRepository;
    private final IMongoUserRepository userMongoRepository;
    private final String activeDb;

    @Autowired
    public TokenService(JwtEncoder jwtEncoder,
                        AuthenticationManager authenticationManager,
                        @Qualifier("userPostgresRepository") IUserRepository userPostgresRepository,
                        @Qualifier("userMongoRepository") IMongoUserRepository userMongoRepository,
                        @Value("${app.active-db}") String activeDb) {

        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
        this.activeDb = activeDb;

        // Программный выбор репозитория на основе конфигурации
        if ("postgres".equalsIgnoreCase(activeDb)) {
            this.userRepository = userPostgresRepository;
            this.userMongoRepository = null; // Mongo не используется
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            this.userMongoRepository = userMongoRepository;
            this.userRepository = null; // Postgres не используется
        } else {
            throw new IllegalArgumentException("Unknown database type: " + activeDb);
        }
    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .claim("role", scope) // Добавляем роль в токен
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String getTokenByLoginAndPassword(String login, String password) {
        var auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login, password));
        return generateToken(auth);
    }

    public String refreshToken(Authentication authentication, Role role) {
        Instant now = Instant.now();
        return jwtEncoder.encode(JwtEncoderParameters.from(JwtClaimsSet.builder()
                        .issuer("self")
                        .issuedAt(now)
                        .expiresAt(now.plus(1, ChronoUnit.DAYS))
                        .subject(authentication.getName())
                        .claim("scope", role.toString())
                        .claim("role", role.toString()) // Добавляем роль в токен
                        .build()))
                .getTokenValue();
    }

    public UserEntity getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if ("postgres".equalsIgnoreCase(activeDb)) {
            return userRepository
                    .findByEmail(principal.getSubject())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + principal.getSubject()));
        } else if ("mongo".equalsIgnoreCase(activeDb)) {
            // Пример получения из Mongo
            return userMongoRepository
                    .findByEmail(principal.getSubject())
                    .map(UserMongoMapper::toUserEntity)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + principal.getSubject()));
        } else {
            throw new IllegalStateException("Unknown database type: " + activeDb);
        }
    }
}

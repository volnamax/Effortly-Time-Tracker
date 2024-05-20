package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.auth.LoginRequestDto;
import com.EffortlyTimeTracker.DTO.auth.RegisterRequestDto;
import com.EffortlyTimeTracker.DTO.auth.TokenResponse;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.service.TokenService;

import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final TokenService tokenService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Получение токена авторизации")
    @PostMapping("/login")
        public TokenResponse loginHandler(@RequestBody LoginRequestDto request) {
        log.info("Got request on /api/v1/login");
        log.info("Got login request for '{}'", request.login());
        log.info("Request body: {}", request);
        var token = getTokenByLoginAndPassword(request.login(), request.password());
        log.info("Generated token: {}", token);


        Optional<UserEntity> userEntityOpt = userService.getUserByEmail(request.login());
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            return new TokenResponse(
                    token,
                    userMapper.toDTOResponse(userEntity)
            );
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    public TokenResponse registrationHandler(@RequestBody RegisterRequestDto request) {
        log.info("Got request on /api/v1/register");
        log.info("Got registration request for '{}'", request.login());
        log.info("Request body: {}", request);
        var user = userService.addUser(userMapper.toEntity(request.user()));
        var token = getTokenByLoginAndPassword(request.login(), request.password());
        log.info("Generated token: {}", token);
        return new TokenResponse(token, userMapper.toDTOResponse(user));
    }

    //todo
//
//    @Operation(summary = "Обновление токена аутентификации", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/token/refresh")
//    @PreAuthorize("isAuthenticated()")
//    public TokenResponse refreshToken(Authentication authentication) {
//        var user = tokenService.getCurrentUser();
//        var token = tokenService.refreshToken(authentication, user.role());
//        return new TokenResponse(token, userMapper.mapModelToSlimResponse(user));
//    }
//
    private String getTokenByLoginAndPassword(String login, String password) {
        var auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login, password));
        return tokenService.generateToken(auth);
    }

}

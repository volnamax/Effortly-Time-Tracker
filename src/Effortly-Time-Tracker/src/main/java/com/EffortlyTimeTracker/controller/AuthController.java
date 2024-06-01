package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.auth.LoginRequestDto;
import com.EffortlyTimeTracker.DTO.auth.RegisterRequestDto;
import com.EffortlyTimeTracker.DTO.auth.TokenResponse;
import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.service.TokenService;

import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "registration new user", description = "need name , sname, email, password, role (ADMIN, MANAGER, USER)")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TokenResponse> registrationHandler(@Valid @RequestBody UserCreateDTO request) {
        log.info("Got request on /api/v1/register");
        log.info("Got registration request for '{}'", request.getEmail());
        log.info("Request body: {}", request);
        UserEntity userEntity  = userMapper.toEntity(request);
        log.info("Userentity = {}", userEntity);

        UserEntity user = userService.addUser(userEntity);
        log.info("added user in  db = {}", user);

        var token = getTokenByLoginAndPassword(request.getEmail(), request.getPasswordHash());
        log.info("Generated token: {}", token);

        UserResponseDTO responseDTO= userMapper.toDTOResponse(user);
        log.info("Generated responseDTO = {}", responseDTO);

        return new ResponseEntity<>(new TokenResponse(token, responseDTO), HttpStatus.CREATED);
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

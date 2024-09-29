package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.auth.LoginRequestDto;
import com.EffortlyTimeTracker.DTO.auth.TokenResponse;
import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.service.TokenService;
import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @Operation(summary = "Get token auth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginHandler(@Valid @RequestBody LoginRequestDto request) {
        log.info("Received login request for '{}'", request.login());

        try {
            var token = getTokenByLoginAndPassword(request.login(), request.password());
            log.info("Generated token: {}", token);

            Optional<UserEntity> userEntityOpt = userService.getUserByEmail(request.login());
            if (userEntityOpt.isPresent()) {
                UserEntity userEntity = userEntityOpt.get();
                TokenResponse tokenResponse = new TokenResponse(token, userMapper.toDTOResponse(userEntity));
                return ResponseEntity.ok(tokenResponse);
            } else {
                log.warn("User not found for login '{}'", request.login());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (BadCredentialsException e) {
            log.error("Invalid login or password for '{}'", request.login());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(summary = "Register new user", description = "name, sname, email, password, role (ADMIN, MANAGER, USER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already in use",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error - Registration failed",
                    content = @Content)
    })    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> registrationHandler(@Valid @RequestBody UserCreateDTO request) {
        log.info("Received registration request for '{}'", request.getEmail());

        // Проверка на существующего пользователя с таким email
        if (userService.getUserByEmail(request.getEmail()).isPresent()) {
            log.warn("Email '{}' already in use", request.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }

        try {
            UserEntity userEntity = userMapper.toEntity(request);
            log.info("UserEntity = {}", userEntity);

            UserEntity user = userService.addUser(userEntity);
            log.info("Added user to the database: {}", user);

            String token = getTokenByLoginAndPassword(request.getEmail(), request.getPasswordHash());
            log.info("Generated token: {}", token);

            UserResponseDTO responseDTO = userMapper.toDTOResponse(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new TokenResponse(token, responseDTO));
        } catch (Exception e) {
            log.error("Error occurred during user registration for '{}'", request.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed");
        }
    }

    private String getTokenByLoginAndPassword(String login, String password) {
        try {
            var auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(login, password));
            return tokenService.generateToken(auth);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid login or password");
        }
    }

    @GetMapping("/get-user-id")
    public ResponseEntity<Integer> getUserIdByEmail(@RequestParam String email) {
        Optional<UserEntity> userEntity = userService.getUserByEmail(email);
        return userEntity.map(entity -> ResponseEntity.ok(entity.getUserId()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
}

package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.unit.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Tag(name = "User-controller")
@Slf4j //loginig
@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;


    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add user for dto obj",
            description = "need:  name , sname, email, password, role (ADMIN, MANAGER, USER)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully added",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error - Failed to add user",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserResponseDTO> addUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        log.info("Add user: {}", userCreateDTO);

        UserEntity userEntity = userMapper.toEntity(userCreateDTO);
        log.info("UserEntity: {}", userEntity);

        UserEntity newUser = userService.addUser(userEntity);
        UserResponseDTO responsUserDto = userMapper.toDTOResponse(newUser);
        log.info("Respons: {}", responsUserDto);
        return new ResponseEntity<>(responsUserDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Dell user by id",
            description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User successfully deleted",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(@RequestParam(required = true) Integer id) {
        log.info("Delete user by id: {}", id);
        userService.delUserById(id);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id",
            description = "need id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO getUser(@RequestParam(required = true) Integer id) {
        log.info("Get user by id: {}", id);

        UserResponseDTO userResponseDTO = userMapper.toDTOResponse(userService.getUserById(id));
        log.info("UserResponseDTO: {}", userResponseDTO);
        return userResponseDTO;
    }


    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions",
                    content = @Content)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserResponseDTO> getUser() {
        log.info("Get all user");
        List<UserResponseDTO> userResponseDTOS = userMapper.toDtoListResponse(userService.getAllUsers());
        log.info("UserResponseDTOS: {}", userResponseDTOS);
        return userResponseDTOS;
    }


    @GetMapping("/get-by-email")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully retrieved",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    public UserResponseDTO getUserByEmail(@RequestParam String email) {
        log.info("Get user by email: {}", email);

        Optional<UserEntity> userEntity = userService.getUserByEmail(email);
        if (userEntity.isEmpty()) {
            log.warn("User with email {} not found", email);
            throw new RuntimeException("User not found");
        }

        UserResponseDTO userResponseDTO = userMapper.toDTOResponse(userEntity.get());
        log.info("UserResponseDTO: {}", userResponseDTO);
        return userResponseDTO;
    }
}


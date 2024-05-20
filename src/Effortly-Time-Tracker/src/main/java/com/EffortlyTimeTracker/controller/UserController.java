package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.user.UserCreateDTO;
import com.EffortlyTimeTracker.DTO.user.UserResponseDTO;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("api/user")
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
            description = "need name , sname, email, password, role (ADMIN, USER, GUEST)")
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
    public void deleteUser(@RequestParam(required = true) Integer id) {
        log.info("Delete user by id: {}", id);
        userService.delUserById(id);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id",
            description = "need id")
    public UserResponseDTO getUser(@RequestParam(required = true) Integer id) {
        log.info("Get user by id: {}", id);

        UserResponseDTO userResponseDTO = userMapper.toDTOResponse(userService.getUserById(id));
        log.info("UserResponseDTO: {}", userResponseDTO);
        return userResponseDTO;
    }


    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all user")
    public List<UserResponseDTO> getUser() {
        log.info("Get all user");
        List<UserResponseDTO> userResponseDTOS = userMapper.toDtoListResponse(userService.getAllUsers());
        log.info("UserResponseDTOS: {}", userResponseDTOS);
        return userResponseDTOS;
    }


    //todo
    @GetMapping("/api/user")
    public ResponseEntity<UserEntity> getUserByEmail(@RequestParam String email) {
        Optional<UserEntity> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}


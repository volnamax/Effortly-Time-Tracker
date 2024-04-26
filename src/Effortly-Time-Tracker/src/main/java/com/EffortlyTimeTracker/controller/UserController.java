package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.UserCreateDTO;
import com.EffortlyTimeTracker.entity.RoleEntity;
import com.EffortlyTimeTracker.entity.UserEntity;
import com.EffortlyTimeTracker.enums.Role;
import com.EffortlyTimeTracker.mapper.UserMapper;
import com.EffortlyTimeTracker.repository.RoleRepository;
import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    public ResponseEntity<UserCreateDTO> addUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        RoleEntity roleEntity = userService.getRoleByName(userCreateDTO.getRole());
        UserEntity userEntity = userMapper.dtoToEntityCreate(userCreateDTO, roleEntity);
        UserEntity newUser = userService.addUser(userEntity);
        UserCreateDTO responsUserDto = userMapper.entityToCreateDto(newUser);

        return new ResponseEntity<>(responsUserDto, HttpStatus.CREATED);
    }


    @DeleteMapping("/del")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Dell user by id",
            description = "need id")
    public void deleteUser(@RequestParam(required = true) Integer id) {
        userService.delUserById(id);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get user by id",
            description = "need id")
    public UserEntity getUser(@RequestParam(required = true) Integer id) {
        return userService.getUserById(id);
    }


    @GetMapping("/get-all")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all user")
    public List<UserEntity> getUser() {
        return userService.getAllUsers();
    }

}


///*
//   @GetMapping("/api/get/user")
//    public User getUser(@RequestParam(required = true) Integer id_user) {
//        return userRep.findById(id_user).orElseThrow(() -> new RuntimeException("User not found with id: " + id_user));
//
//    //изменение знаяения
//    @PutMapping("/api/change/user")
//    // @RequestBody - запросить какой то обхект  автомачическая сереализация (получили как json oбъект )
//    public String changeUser(@RequestBody User user) {
//        if (!userRep.existsById(user.getUserId())) {
//            return "not user id ";
//        }
//        return userRep.save(user).toString();}
//    */



package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.User;
import com.EffortlyTimeTracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Tag(name = "User-controller") // documentation
@Slf4j //loginig
@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add user for dto obj",
            description = "need name , sname, email, role (ADMIN, USER, GUEST)")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.addUser(userDTO);
    }
    @Operation(summary = "Dell user by id",
            description = "need id")
    @DeleteMapping("/del")
    public void deleteUser(@RequestParam(required = true) Integer id_user) {
        userService.delUserById(id_user);
    }


/*
    // magic throws
    @SneakyThrows
    @GetMapping("/api/get/users")
    public List<User> getAll() {
        return userRep.findAll();
    }


    @GetMapping("/api/get/user")
    public User getUser(@RequestParam(required = true) Integer id_user) {
        return userRep.findById(id_user).orElseThrow(() -> new RuntimeException("User not found with id: " + id_user));
    }

    @DeleteMapping("/api/del/user")
    public void deleteUser(@RequestParam(required = true) Integer id_user) {
        userRep.deleteById(id_user);
    }



    //изменение знаяения
    @PutMapping("/api/change/user")
    // @RequestBody - запросить какой то обхект  автомачическая сереализация (получили как json oбъект )
    public String changeUser(@RequestBody User user) {
        if (!userRep.existsById(user.getUserId())) {
            return "not user id ";
        }
        return userRep.save(user).toString();

    }
    */
}


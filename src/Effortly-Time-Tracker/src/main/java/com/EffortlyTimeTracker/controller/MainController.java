package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.UserDTO;
import com.EffortlyTimeTracker.entity.User;
import com.EffortlyTimeTracker.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "main-method") // documentation
@Slf4j //loginig
@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserRepository userRep;

    @PostMapping("/api/add/user")
    // @RequestBody - запросить какой то обхект  автомачическая сереализация (получили как json oбъект )
    public void addUser(@RequestBody User user) {
        log.info("New row+ " + userRep.save(user));

    }
    @Operation(summary = "добаляет нового пользователя через дто",
    description = "more info")
    @PostMapping("/api/user/add-dto")
    // @RequestBody - запросить какой то обхект  автомачическая сереализация (получили как json oбъект )
    public void addUser(@RequestBody UserDTO userDTO) {
        log.info("New row+ " +
                userRep.save(User.builder()
                        .user_name(userDTO.getUser_name())
                        .user_secondname(userDTO.getUser_secondname())
                        .build()));
    }

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
        if (!userRep.existsById(user.getId_user())) {
            return "not user id ";
        }
        return userRep.save(user).toString();

    }
    /*
        @Autowired
        //  сереализация
        private ObjectMapper objectMapper;

      @GetMapping("/api/main")
    public String mainListener(){
        return  "hello";
    }

    @GetMapping("/api/user")
    public String givetUser() {
        User user = new User(1, "max", "Vol");
        String jsonData  = null;
        try {
            jsonData = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e){
           System.out.println("Error user");
        }
        return jsonData;
    }

    @PostMapping("/api/spec")
    public String giveSpecialUser(@RequestParam String name){
        User user = new User(1, name, "Vol");
        String jsonData  = null;
        try {
            jsonData = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e){
            System.out.println("Error user");
        }
        return jsonData;
    }*/
}


package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.entity.User;
import com.EffortlyTimeTracker.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//loginig
@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final UserRepository userRep;
    private  final  ObjectMapper objectMapper;

    @PostMapping("/api/add/user")
    // @RequestBody - запросить какой то обхект  автомачическая сереализация (получили как json oбъект )
    public void addUser(@RequestBody User user){
       log.info("New row+ " + userRep.save(user)) ;

    }
    // magic throws
    @SneakyThrows
    @GetMapping("/api/get/users")
    public List<User> getAll (){
        return userRep.findAll();
    }

    @GetMapping("/api/get/user")
    public User getUser(@RequestParam int id){
        return userRep.findBy(id).orElseThrow(); // userRep.findBy(id) - возратит контейнер который может иметь значение а может и нет если у нес есть увереность можем юзать get
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


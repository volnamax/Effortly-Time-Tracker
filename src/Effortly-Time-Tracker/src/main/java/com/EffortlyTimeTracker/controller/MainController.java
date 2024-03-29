package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.controller.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @Autowired
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
    }
}


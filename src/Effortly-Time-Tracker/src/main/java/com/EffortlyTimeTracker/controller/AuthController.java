package com.EffortlyTimeTracker.controller;

import com.EffortlyTimeTracker.DTO.auth.AuthDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class AuthController {
@PostMapping("/auth")
public String auth (@RequestParam AuthDTO authDTO) {
    return null;
}
}

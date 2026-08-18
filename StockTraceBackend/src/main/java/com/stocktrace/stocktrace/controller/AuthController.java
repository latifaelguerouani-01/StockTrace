package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.LoginRequest;
import com.stocktrace.stocktrace.entity.User;
import com.stocktrace.stocktrace.repository.UserRepository;
import com.stocktrace.stocktrace.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(
            AuthService authService,
            UserRepository userRepository) {

        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequest loginRequest) {

        String token = authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );

        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow();

        Map<String, String> response = new HashMap<>();

        response.put("token", token);
        response.put("email", user.getEmail());
        response.put("role", user.getRole().name());

        return ResponseEntity.ok(response);
    }
}
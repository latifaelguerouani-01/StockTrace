package com.stocktrace.stocktrace.controller;

import com.stocktrace.stocktrace.dto.ChangeCredentialsRequest;
import com.stocktrace.stocktrace.dto.UserDto;
import com.stocktrace.stocktrace.entity.User;
import com.stocktrace.stocktrace.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
            Authentication authentication) {

        User user = userService.getUserByEmail(
                authentication.getName()
        );

        return ResponseEntity.ok(mapToDto(user));
    }

    @PutMapping("/me/credentials")
    public ResponseEntity<UserDto> updateCredentials(
            @RequestBody ChangeCredentialsRequest request,
            Authentication authentication) {

        User updatedUser =
                userService.updateMyCredentials(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(mapToDto(updatedUser));
    }

    private UserDto mapToDto(User user) {

        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }
}
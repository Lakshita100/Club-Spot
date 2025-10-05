package com.clubspot.controller;

import com.clubspot.entity.User;
import com.clubspot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        User user = userService.authenticate(request.get("username"), request.get("password"));
        if (user != null) {
            // In prod, generate JWT here (see Security section)
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "userId", user.getId(),
                    "role", user.getRole(),
                    "token", "fake-jwt-for-now" // Replace with real JWT
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User saved = userService.createUser(user);
            return ResponseEntity.ok(Map.of("success", true, "userId", saved.getId()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}

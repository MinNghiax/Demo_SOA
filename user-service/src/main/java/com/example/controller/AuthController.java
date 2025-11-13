package com.example.controller;

import com.example.dto.AuthRequest;
import com.example.dto.AuthResponse;
import com.example.model.User;
import com.example.service.UserService;
import com.example.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        User existing = userService.getUserByEmail(user.getEmail());
        if (existing != null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Email already in use"));

        }

        User saved = userService.saveUser(user);
        String token = jwtUtil.generateToken(saved.getEmail());

        return ResponseEntity.ok(new AuthResponse(token, saved));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        User user = userService.getUserByEmail(request.getEmail());
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid email or password"));

        }

        if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Invalid email or password"));

        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user));
    }
}

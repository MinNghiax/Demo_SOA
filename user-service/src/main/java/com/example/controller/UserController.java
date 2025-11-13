package com.example.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.model.User;
import com.example.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // GET all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.findUserById(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body("User not found with ID: " + id);
        }
    }

    // POST create new user
    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // ✅ PUT update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        User existingUser = userService.findUserById(id);

        if (existingUser == null) {
            return ResponseEntity.status(404).body("User not found with ID: " + id);
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());

        return ResponseEntity.ok(userService.saveUser(existingUser));
    }

    // ✅ DELETE remove user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User existingUser = userService.findUserById(id);

        if (existingUser == null) {
            return ResponseEntity.status(404).body(Map.of("message", "User not found"));
        }

        userService.deleteUser(id);

        // ✔ Trả JSON hợp lệ để Angular parse được
        return ResponseEntity.ok(Map.of("message", "User with ID " + id + " deleted successfully"));
    }

}

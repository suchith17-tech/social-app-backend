package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Create (signup)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User saved = userService.registerUser(user);
        saved.setPassword(null); // don’t leak password in responses
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ✅ Read all
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAllUsers();
        users.forEach(u -> u.setPassword(null));
        return ResponseEntity.ok(users);
    }

    // ✅ Read one
    @GetMapping("/{id}")
    public ResponseEntity<User> getOne(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(u -> {
                    u.setPassword(null);
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody User payload
    ) {
        User saved = userService.updateUser(id, payload);
        saved.setPassword(null); // hide password defensively
        return ResponseEntity.ok(saved);
    }

    // ✅ Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Get logged-in user (profile)
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();   // comes from JWT
        return userService.findByUsername(username)
                .map(u -> {
                    u.setPassword(null);  // hide password
                    return ResponseEntity.ok(u);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

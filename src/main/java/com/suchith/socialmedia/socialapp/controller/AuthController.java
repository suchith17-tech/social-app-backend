package com.suchith.socialmedia.socialapp.controller;

import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.payload.AuthResponse;
import com.suchith.socialmedia.socialapp.security.JwtUtil;
import com.suchith.socialmedia.socialapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import com.suchith.socialmedia.socialapp.payload.AuthRequest;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // Signup
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User saved = userService.registerUser(user);
        saved.setPassword(null); // don’t send back password
        return ResponseEntity.ok(saved);
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // If authentication passed
            if (authentication.isAuthenticated()) {
                String token = jwtUtil.generateToken(request.getUsername());
                return ResponseEntity.ok(new AuthResponse(token));
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

        } catch (AuthenticationException e) {
            // If login fails
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }



}

package com.suchith.socialmedia.socialapp.service;

import com.suchith.socialmedia.socialapp.model.User;
import com.suchith.socialmedia.socialapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;



@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Signup: Save a new user (with uniqueness checks)
    public User registerUser(User user) {
        // Username must be unique
        userRepository.findByUsername(user.getUsername()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
        });

        // Email must be unique
        userRepository.findByEmail(user.getEmail()).ifPresent(existing -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        });


        user.setPassword(passwordEncoder.encode(user.getPassword()));


        return userRepository.save(user);
    }

    // ✅ Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ✅ Find user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // ✅ Delete a user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User updated) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Update username (check uniqueness)
        if (updated.getUsername() != null && !updated.getUsername().equals(existing.getUsername())) {
            userRepository.findByUsername(updated.getUsername()).ifPresent(conflict -> {
                if (!conflict.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
                }
            });
            existing.setUsername(updated.getUsername());
        }

        // Update email (check uniqueness)
        if (updated.getEmail() != null && !updated.getEmail().equals(existing.getEmail())) {
            userRepository.findByEmail(updated.getEmail()).ifPresent(conflict -> {
                if (!conflict.getId().equals(id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }
            });
            existing.setEmail(updated.getEmail());
        }

        // ❌ not touching password here
        return userRepository.save(existing);
    }

    public User login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


}

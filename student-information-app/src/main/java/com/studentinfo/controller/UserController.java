package com.studentinfo.controller;

import com.studentinfo.data.entity.User;
import com.studentinfo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Inject PasswordEncoder through the constructor
    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return ResponseEntity.ok(users); // Return 200 OK with the list of users
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.get(id)
                .map(ResponseEntity::ok) // Return 200 OK with the found user
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 Not Found if user not found
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser); // Return 201 Created with the saved user
    }

    // Update user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUserOptional = userService.get(id);

        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if user not found
        }

        User existingUser = existingUserOptional.get();

        // Update username
        if (user.getUsername() != null) {
            existingUser.setUsername(user.getUsername());
        }

        // Update email
        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        // Update first name
        if (user.getFirstName() != null) {
            existingUser.setFirstName(user.getFirstName());
        }

        // Update last name
        if (user.getLastName() != null) {
            existingUser.setLastName(user.getLastName());
        }

        // Update phone number
        if (user.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(user.getPhoneNumber());
        }

        // Update password if provided (you should hash the password before saving it)
        if (user.getHashedPassword() != null && !user.getHashedPassword().isEmpty()) {
            String hashedPassword = passwordEncoder.encode(user.getHashedPassword());
            existingUser.setHashedPassword(hashedPassword);
        }

        // Save the updated user
        User updatedUser = userService.save(existingUser);

        return ResponseEntity.ok(updatedUser); // Return 200 OK with the updated user
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = userService.get(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 if user not found
        }
        userService.delete(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content after successful deletion
    }
}

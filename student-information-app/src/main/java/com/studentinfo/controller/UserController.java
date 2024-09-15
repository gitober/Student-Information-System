package com.studentinfo.controller;

import com.studentinfo.data.dto.EditProfileDTO;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.UserService;
import com.studentinfo.security.AuthenticatedUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public UserController(UserService userService, AuthenticatedUser authenticatedUser) {
        this.userService = userService;
        this.authenticatedUser = authenticatedUser;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.get(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.get(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/edit")
    public ResponseEntity<User> updateUserProfile(@RequestBody @Valid EditProfileDTO editProfileDTO) {
        // Fetch the currently authenticated user
        User currentUser = authenticatedUser.get()
                .orElseThrow(() -> new IllegalStateException("No authenticated user found"));

        // Validate DTO
        if (editProfileDTO.getId() == null || editProfileDTO.getEmail() == null || editProfileDTO.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Ensure ID and email are provided
        }

        // Ensure the user is updating their own profile
        if (!currentUser.getId().equals(editProfileDTO.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if trying to update another user's profile
        }

        // Fetch existing user by ID
        Optional<User> existingUserOptional = userService.get(editProfileDTO.getId());
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Return 404 if user does not exist
        }

        User existingUser = existingUserOptional.get();

        // Check if the new email is already used by another user
        User userWithSameEmail = userService.findByEmail(editProfileDTO.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(existingUser.getId())) {
            return ResponseEntity.badRequest().build(); // Return 400 if email is not unique
        }

        // Map DTO fields to existing user
        existingUser.setFirstName(editProfileDTO.getFirstName());
        existingUser.setLastName(editProfileDTO.getLastName());
        existingUser.setPhoneNumber(editProfileDTO.getPhoneNumber());
        existingUser.setEmail(editProfileDTO.getEmail());

        // Handle password update
        if (editProfileDTO.getPassword() != null && !editProfileDTO.getPassword().isEmpty()) {
            // Use UserService to encode the password
            existingUser.setHashedPassword(userService.encodePassword(editProfileDTO.getPassword()));
        }

        // Handle additional fields for specific user types
        if (existingUser instanceof Student) {
            Student student = (Student) existingUser;
            student.setGrade(editProfileDTO.getGrade());
            student.setStudentClass(editProfileDTO.getStudentClass());
        }
        if (existingUser instanceof Teacher) {
            Teacher teacher = (Teacher) existingUser;
            teacher.setDepartment(editProfileDTO.getDepartment());
        }

        // Save updated user
        User updatedUser = userService.save(existingUser);

        return ResponseEntity.ok(updatedUser);
    }
}

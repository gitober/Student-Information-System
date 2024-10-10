package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.studentinfo.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUser authenticatedUser;
    private final Environment environment;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticatedUser authenticatedUser, Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUser = authenticatedUser;
        this.environment = environment;
    }

    // Check if the current environment is test
    private boolean isTestEnvironment() {
        return environment != null && List.of(environment.getActiveProfiles()).contains("test");
    }

    // Get the current authenticated user's student number
    public Long getCurrentStudentNumber() {
        Optional<User> currentUser = authenticatedUser.get();
        if (currentUser.isPresent() && currentUser.get() instanceof Student) {
            return ((Student) currentUser.get()).getStudentNumber();
        }
        return null;
    }

    // Authenticate user based on email and password
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getHashedPassword())) {
            return Optional.of(userOptional.get());
        }
        return Optional.empty();
    }

    // Find a user by email
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    // Get the current authenticated user
    public User getCurrentUser() {
        return authenticatedUser.get().orElse(null);
    }

    // List all users
    public List<User> list() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public Optional<User> get(Long id) {
        return userRepository.findById(id);
    }

    // Save or update a user
    public User save(User user) {
        return userRepository.save(user);
    }

    // Delete a user by ID
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    // Find a user by email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update the user's password in the database
    public void updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setHashedPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            // Clear the current authentication to force re-login with the new password
            SecurityContextHolder.clearContext();

            System.out.println("Password updated for user: " + email); // Debugging log
        } else {
            System.out.println("User with email " + email + " not found."); // Debugging log
        }
    }


    // Update both email and username for a user
    public void updateUserEmailAndUsername(Long userId, String newEmail, String newUsername) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setUsername(newUsername);
            user.setEmail(newEmail);
            userRepository.save(user);
        }
    }
}

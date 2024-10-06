package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.studentinfo.security.AuthenticatedUser;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticatedUser authenticatedUser;
    private final Environment environment; // Inject the Environment

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, AuthenticatedUser authenticatedUser,
                       Environment environment) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authenticatedUser = authenticatedUser;
        this.environment = environment; // Assign the Environment
    }

    // Check if the current environment is test
    private boolean isTestEnvironment() {
        return environment != null && List.of(environment.getActiveProfiles()).contains("test");
    }

    // Public Methods

    // Get the current authenticated user's student number
    public Long getCurrentStudentNumber() {
        Optional<User> currentUser = authenticatedUser.get();

        if (currentUser.isPresent()) {
            User user = currentUser.get();
            System.out.println("Debug: Current user is present, user type: " + user.getClass().getSimpleName());

            if (user instanceof Student) {
                Long studentNumber = ((Student) user).getStudentNumber();
                System.out.println("Debug: Retrieved student number: " + studentNumber);
                return studentNumber;
            } else {
                System.out.println("Debug: Current user is not a student.");
            }
        } else {
            System.out.println("Debug: No current user found.");
        }

        return null; // Return null if no student is found
    }

    // Authenticate user based on email and password
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Use PasswordEncoder to compare the provided password with the hashed password
            if (passwordEncoder.matches(password, user.getHashedPassword())) {
                return Optional.of(user);
            }
        }
        return Optional.empty(); // Return empty if authentication fails
    }

    // CRUD Operations

    // Find a user by email
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElse(null); // Return the user if found, or null if not found
    }

    // Get the current authenticated user
    public User getCurrentUser() {
        Optional<User> currentUser = authenticatedUser.get(); // Assuming authenticatedUser is set up correctly
        return currentUser.orElse(null);
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

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Method to update the user's password in the database
    public void updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Hash the new password using the PasswordEncoder
            String hashedPassword = passwordEncoder.encode(newPassword);
            System.out.println("Hashed Password: " + hashedPassword); // Debugging
            user.setHashedPassword(hashedPassword);
            userRepository.save(user); // Save the user with the updated password

            // Show notification if not in the test environment
            if (!isTestEnvironment()) {
                Notification.show("Password updated successfully!", 3000, Notification.Position.MIDDLE);
            }
        } else {
            if (!isTestEnvironment()) {
                Notification.show("User not found with this email.", 3000, Notification.Position.MIDDLE);
            }
        }
    }
}

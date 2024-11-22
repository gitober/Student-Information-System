package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.studentinfo.security.AuthenticatedUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticatedUser authenticatedUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticatedUser = authenticatedUser;
    }

    // Get the current authenticated user's student number
    public Long getCurrentStudentNumber() {
        Optional<User> currentUser = authenticatedUser.get();

        if (currentUser.isPresent() && currentUser.get() instanceof Student student) {
            return student.getStudentNumber();
        }
        return null;
    }

    // Authenticate user based on email and password
    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent() && passwordEncoder.matches(password, userOptional.get().getHashedPassword())) {
            return userOptional;
        }
        return Optional.empty();
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

            logger.info("Password updated for user: {}", email); // Logging instead of System.out
        } else {
            logger.warn("User with email {} not found.", email); // Logging instead of System.out
        }
    }

}

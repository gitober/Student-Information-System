package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.studentinfo.security.AuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    // Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, AuthenticatedUser authenticatedUser) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.authenticatedUser = authenticatedUser;
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
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userRepository.findByEmail(email);
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

    // Private Helper Methods

    // Get the current HttpServletRequest
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No current request available", e);
        }
    }

    // Get the current HttpServletResponse
    private HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            if (response == null) {
                throw new IllegalStateException("No response available");
            }
            return response;
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No current response available", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager; // Use Spring Security's AuthenticationManager

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // Authenticate user based on username and password
    public Optional<User> authenticate(String username, String password) {
        // Perform authentication using Spring Security's AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Set the authenticated user in the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Fetch the user from the repository after authentication is successful
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));

        // Logging authenticated roles (for debugging)
        userOptional.ifPresent(user -> System.out.println("Authenticated user roles: " + authentication.getAuthorities()));

        return userOptional;
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

    // Helper method to get the current HttpServletRequest
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return attributes.getRequest();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("No current request available", e);
        }
    }

    // Helper method to get the current HttpServletResponse
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
}
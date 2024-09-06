package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> get() {
        Optional<UserDetails> userDetailsOpt = authenticationContext.getAuthenticatedUser(UserDetails.class);

        // Debug: Log the authentication status
        System.out.println("Checking authentication status...");
        userDetailsOpt.ifPresentOrElse(
                userDetails -> System.out.println("Authenticated username: " + userDetails.getUsername()),
                () -> System.out.println("No authenticated user found.")
        );

        // Fetch the user entity from the repository
        return userDetailsOpt.map(userDetails -> {
            User user = userRepository.findByUsername(userDetails.getUsername());
            // Debug: Log the retrieved user and roles
            if (user != null) {
                System.out.println("User found: " + user.getUsername() + ", Roles: " + user.getRoles());
            } else {
                System.out.println("User not found in repository.");
            }
            return user;
        });
    }

    public void logout() {
        authenticationContext.logout();
        System.out.println("User logged out successfully.");
    }
}

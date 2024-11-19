package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private static final Logger logger = LoggerFactory.getLogger(AuthenticatedUser.class);

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public Optional<User> get() {
        // Fetch the authenticated user details
        Optional<UserDetails> userDetailsOpt = authenticationContext.getAuthenticatedUser(UserDetails.class);

        logger.info("Checking authentication status...");

        // Log the authentication status
        userDetailsOpt.ifPresentOrElse(
                userDetails -> logger.debug("Authenticated username: {}", userDetails.getUsername()),
                () -> logger.warn("No authenticated user found.")
        );

        // Map UserDetails to your custom User entity by fetching it from the repository
        return userDetailsOpt.flatMap(userDetails -> {
            String email = userDetails.getUsername();
            logger.debug("Fetching user with email: {}", email);
            return userRepository.findByEmail(email);
        });
    }

    // Logout the user
    public void logout() {
        authenticationContext.logout();
        logger.info("User logged out successfully.");
    }
}

package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

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
        Optional<UserDetails> userDetailsOpt = authenticationContext.getAuthenticatedUser(UserDetails.class);

        logger.info("Checking authentication status...");
        userDetailsOpt.ifPresentOrElse(
                userDetails -> logger.debug("Authenticated username: {}", userDetails.getUsername()),
                () -> logger.warn("No authenticated user found.")
        );

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info("SecurityContextHolder contains: User - {}, Authorities - {}", auth.getName(), auth.getAuthorities());
        } else {
            logger.warn("SecurityContextHolder is empty.");
        }

        return userDetailsOpt.flatMap(userDetails -> {
            User user = userRepository.findByEmail(userDetails.getUsername());
            if (user != null) {
                logger.info("User found: {}, Roles: {}", user.getEmail(), user.getRoles());
            } else {
                logger.warn("User not found in repository for username: {}", userDetails.getUsername());
            }
            return Optional.ofNullable(user);
        });
    }



    public void logout() {
        authenticationContext.logout();
        logger.info("User logged out successfully.");
    }
}

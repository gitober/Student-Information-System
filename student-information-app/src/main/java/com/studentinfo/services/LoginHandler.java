package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class LoginHandler {

    // Dependencies
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginHandler(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // Login Method
    public void login(String email, String password) {
        // Authenticate the user using the user service
        Optional<User> authenticatedUserOpt = userService.authenticate(email, password);

        authenticatedUserOpt.ifPresentOrElse(
                user -> handleSuccessfulLogin(user, email, password),
                () -> handleFailedLogin(email)
        );
    }

    // Handle Successful Login
    private void handleSuccessfulLogin(User user, String email, String password) {
        try {
            // Create authentication token
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

            // Authenticate the token
            Authentication authentication = authenticationManager.authenticate(authToken);

            // Set authentication in SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Persist security context to session
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);

            // Check the user's role and navigate accordingly
            navigateUserBasedOnRole(user);

            Notification.show("Logged in successfully!");
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            Notification.show("Error during authentication.");
        }
    }

    // Handle Failed Login
    private void handleFailedLogin(String email) {
        System.out.println("Authentication failed for user: " + email);
        Notification.show("Authentication failed. Please check your credentials.");
    }

    // Navigate User Based on Role
    private void navigateUserBasedOnRole(User user) {
        if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_TEACHER"))) {
            // Redirect teachers to the teacher dashboard or profile
            UI.getCurrent().navigate("profile"); // Change to "teacher-dashboard" if needed
        } else if (user.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STUDENT"))) {
            // Redirect students to their profile
            UI.getCurrent().navigate("profile");
        } else {
            // Fallback option if role is not recognized
            UI.getCurrent().navigate("profile");
        }
    }
}

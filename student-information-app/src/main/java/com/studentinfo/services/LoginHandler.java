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

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LoginHandler(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public void login(String email, String password) {
        // Authenticate the user using the authentication manager
        Optional<User> authenticatedUserOpt = userService.authenticate(email, password);

        authenticatedUserOpt.ifPresentOrElse(
                user -> {
                    try {
                        // Create authentication token using the user credentials
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

                        // Authenticate the token using the AuthenticationManager
                        Authentication authentication = authenticationManager.authenticate(authToken);

                        // Set the authentication in SecurityContextHolder
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // Persist the security context to the session
                        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
                        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
                        new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);

                        // Logging authentication success
                        System.out.println("Authentication successful for user: " + email);
                        System.out.println("Authenticated user roles: " + authentication.getAuthorities());

                        // Navigate to the profile page
                        UI.getCurrent().navigate("profile");
                        Notification.show("Navigating to profile page...");
                    } catch (Exception e) {
                        System.err.println("Error during authentication or navigation: " + e.getMessage());
                        Notification.show("Error during authentication or navigation.");
                    }
                },
                () -> {
                    System.out.println("Authentication failed for user: " + email);
                    Notification.show("Authentication failed. Please check your credentials.");
                }
        );
    }
}

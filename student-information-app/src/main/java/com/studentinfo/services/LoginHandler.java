package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {

    private final UserService userService;

    @Autowired
    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public void login(String email, String password) {
        userService.authenticate(email, password).ifPresentOrElse(
                user -> {
                    System.out.println("Authentication successful for user: " + email);
                    System.out.println("User roles: " + user.getRoles());

                    // Ensure the roles are correctly formatted with ROLE_ prefix
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    // Debug: Check if authorities are correctly prefixed with "ROLE_"
                    authentication.getAuthorities().forEach(auth ->
                            System.out.println("Authority: " + auth.getAuthority())
                    );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // Debug: Print authenticated user roles
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    System.out.println("Authenticated user roles: " + auth.getAuthorities());

                    // Verify SecurityContextHolder settings
                    System.out.println("SecurityContextHolder user: " + SecurityContextHolder.getContext().getAuthentication().getName());
                    System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

                    // Navigate to the profile page
                    try {
                        UI.getCurrent().navigate("profile");
                        System.out.println("Navigating to profile page...");
                        Notification.show("Navigating to profile page...");
                    } catch (Exception e) {
                        System.err.println("Error navigating to profile page: " + e.getMessage());
                        Notification.show("Error navigating to profile page.");
                    }
                },
                () -> {
                    System.out.println("Authentication failed for user: " + email);
                    Notification.show("Authentication failed. Please check your credentials.");
                }
        );
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LoginHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoginHandler.class);
    private static final String PROFILE_ROUTE = "profile";

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public LoginHandler(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    // Login Method
    public boolean login(String email, String password, boolean rememberMe) {
        try {
            // Authenticate the user
            Optional<User> authenticatedUserOpt = userService.authenticate(email, password);

            if (authenticatedUserOpt.isPresent()) {
                User user = authenticatedUserOpt.get();
                handleSuccessfulLogin(user, email, password, rememberMe);
                return true; // Successful login
            } else {
                handleFailedLogin(email);
                return false; // Failed login
            }
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            return false; // Handle login error
        }
    }

    // Handle Successful Login
    private void handleSuccessfulLogin(User user, String email, String password, boolean rememberMe) {
        try {
            // Authenticate and set security context
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Persist security context to session
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            new HttpSessionSecurityContextRepository().saveContext(SecurityContextHolder.getContext(), request, response);

            // Handle "Remember me" cookie
            if (response != null) {
                handleRememberMeCookie(rememberMe, email, response);
            }

            // Navigate based on user role
            navigateUserBasedOnRole(user);

            // Show login success notification
            showNotification("Logged in successfully!");
        } catch (Exception e) {
            logger.error("Error during authentication: {}", e.getMessage(), e);
            showNotification("Error during authentication.");
        }
    }

    // Handle Failed Login
    private void handleFailedLogin(String email) {
        logger.warn("Authentication failed for user: {}", email);
        showNotification("Authentication failed. Please check your credentials.");
    }


    // Set or remove "Remember me" cookie
    private void handleRememberMeCookie(boolean rememberMe, String email, HttpServletResponse response) {
        Cookie cookie = new Cookie("rememberMe", rememberMe ? email : "");
        cookie.setMaxAge(rememberMe ? 60 * 60 * 24 * 30 : 0); // 30 days or delete cookie
        cookie.setPath("/");
        cookie.setHttpOnly(true); // Makes the cookie HttpOnly
        cookie.setSecure(true); // Ensures the cookie is only sent over HTTPS

        response.addCookie(cookie);
    }

    // Navigate User Based on Role
    private void navigateUserBasedOnRole(User user) {
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                String route = user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .filter(role -> role.equals("ROLE_TEACHER") || role.equals("ROLE_STUDENT"))
                        .findFirst()
                        .map(role -> PROFILE_ROUTE) // Assign route based on the role if matched
                        .orElse(PROFILE_ROUTE); // Fallback navigation if no role is matched

                ui.navigate(route);
            });
        } else {
            logger.error("No UI instance available for navigation.");
        }
    }

    // Show notification safely within the UI context
    private void showNotification(String message) {
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> Notification.show(message));
        } else {
            logger.error("No UI instance available to show notification.");
        }
    }
}

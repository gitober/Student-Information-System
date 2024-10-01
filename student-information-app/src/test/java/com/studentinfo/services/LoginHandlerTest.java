package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.entity.Role;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private LoginHandler loginHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));
    }

    @Test
    void testSuccessfulLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setUsername(email);

        // Initialize roles set and add a role to the user
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

        when(userService.authenticate(email, password)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(authToken)).thenReturn(mock(Authentication.class));

        // Mock UI and Notification
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        try (var mockedNotification = Mockito.mockStatic(Notification.class)) {
            // Act
            loginHandler.login(email, password);

            // Assert
            verify(userService, times(1)).authenticate(email, password);
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());

            // Verify Notification.show was called
            mockedNotification.verify(() -> Notification.show("Logged in successfully!"), times(1));
        }
    }

    @Test
    void testFailedLogin() {
        // Arrange
        String email = "invalid@example.com";
        String password = "wrongpassword";

        when(userService.authenticate(email, password)).thenReturn(Optional.empty());

        // Mock UI and Notification
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        try (var mockedNotification = Mockito.mockStatic(Notification.class)) {
            // Act
            loginHandler.login(email, password);

            // Assert
            verify(userService, times(1)).authenticate(email, password);
            verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
            assertNull(SecurityContextHolder.getContext().getAuthentication());

            // Verify Notification.show was called with failed message
            mockedNotification.verify(() -> Notification.show("Authentication failed. Please check your credentials."), times(1));
        }
    }
}

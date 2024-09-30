package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginHandler loginHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setUsername(email);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

        when(userService.authenticate(email, password)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(authToken)).thenReturn(mock(Authentication.class));

        // Act
        loginHandler.login(email, password);

        // Assert
        verify(userService, times(1)).authenticate(email, password);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testFailedLogin() {
        // Arrange
        String email = "invalid@example.com";
        String password = "wrongpassword";

        when(userService.authenticate(email, password)).thenReturn(Optional.empty());

        // Act
        loginHandler.login(email, password);

        // Assert
        verify(userService, times(1)).authenticate(email, password);
        verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;

class SecurityServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock for request and response
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));

        // Mock the authentication manager to return a valid authentication token
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getName()).thenReturn("test@example.com"); // Set the name to match the email
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
    }

    @AfterEach
    void tearDown() {
        // Clear the security context and reset mocks after each test
        SecurityContextHolder.clearContext();
        reset(authenticationManager, request, response);
    }

    @Test
    void testAuthenticateUser() {
        // Arrange
        User mockUser = mock(User.class);
        when(mockUser.getEmail()).thenReturn("test@example.com");

        // Create a mock Role
        Role mockRole = mock(Role.class);
        when(mockRole.name()).thenReturn("USER");
        when(mockUser.getRoles()).thenReturn(Collections.singleton(mockRole)); // Mock the roles to return a Set<Role>

        String rawPassword = "password";

        // Act
        securityService.authenticateUser(mockUser, rawPassword);

        // Assert
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication, "Authentication should not be null");
        assertTrue(authentication.isAuthenticated(), "User should be authenticated");
        assertEquals("test@example.com", authentication.getName(), "Authentication name should match the user's email");

        // Verify that authenticationManager.authenticate() was called
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Verify that the security context was saved to the session
        verify(request, atLeastOnce()).getSession(anyBoolean()); // Use argument matcher for session call
    }
}

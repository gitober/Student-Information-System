package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticatedUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationContext authenticationContext;

    @InjectMocks
    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @AfterEach
    void tearDown() {
        reset(userRepository, authenticationContext); // Reset mocks after each test
    }

    @Test
    void testGetAuthenticatedUser() {
        // Arrange
        String testEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(testEmail);

        UserDetails mockUserDetails = org.springframework.security.core.userdetails.User
                .withUsername(testEmail)
                .password("password") // Password doesn't matter in this context
                .roles("USER") // Roles if necessary
                .build();

        // Mock the AuthenticationContext to return the UserDetails
        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(Optional.of(mockUserDetails));

        // Mock the user repository to return the custom User entity
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = authenticatedUser.get();

        // Assert
        assertTrue(result.isPresent(), "Expected user should be present.");
        assertEquals(testEmail, result.get().getEmail(), "Email should match the mock data.");
    }

    @Test
    void testGetAuthenticatedUserNotFound() {
        // Arrange: No user is authenticated
        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = authenticatedUser.get();

        // Assert: Result should be empty
        assertFalse(result.isPresent(), "Expected no user to be returned.");
    }

    @Test
    void testLogout() {
        // Act
        authenticatedUser.logout();

        // Assert that the logout method was called on the AuthenticationContext
        verify(authenticationContext).logout();
    }
}

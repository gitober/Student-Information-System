package com.studentinfo.security;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class AuthenticatedUserTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationContext authenticationContext;

    @InjectMocks
    @Autowired
    private AuthenticatedUser authenticatedUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(userRepository, authenticationContext);
    }

    @Test
    void testGet() {
        // Arrange
        String testEmail = "test@example.com";
        User user = new User();
        user.setEmail(testEmail);

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(testEmail)
                .password("password") // Password not needed for the mock
                .roles("USER") // Add roles if needed
                .build();

        // Mock the AuthenticationContext to return a UserDetails object
        when(authenticationContext.getAuthenticatedUser(UserDetails.class)).thenReturn(Optional.of(userDetails));

        // Mock the user repository to return the user
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = authenticatedUser.get();

        // Assert
        assertTrue(result.isPresent(), "Expected user should be present.");
        assertEquals(result.get().getEmail(), testEmail, "Expected email should match.");
    }

    @Test
    void testLogout() {
        // Act
        authenticatedUser.logout();

        // Verify that the logout method was called on the AuthenticationContext
        verify(authenticationContext).logout();

        // Assert that the security context is cleared (if necessary)
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Expected security context to be cleared after logout.");
    }
}
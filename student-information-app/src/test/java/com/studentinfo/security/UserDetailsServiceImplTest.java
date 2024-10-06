package com.studentinfo.security;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset any changes or state after each test if necessary
        userRepository.deleteAll();
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Arrange
        String testEmail = "test@example.com";
        String testHashedPassword = "hashedPassword";
        User user = new User();
        user.setEmail(testEmail);
        user.setHashedPassword(testHashedPassword);
        // Set roles as needed for your User class
        user.setRoles(Set.of(Role.USER));

        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.of(user));

        // Act
        UserDetails result = userDetailsService.loadUserByUsername(testEmail);

        // Assert
        assertEquals(testEmail, result.getUsername());
        assertEquals(testHashedPassword, result.getPassword());
        // Optionally check authorities if your implementation allows it
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String testEmail = "notfound@example.com";
        when(userRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(testEmail);
        });

        assertEquals("No user present with email: " + testEmail, exception.getMessage());
    }
}

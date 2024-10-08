package com.studentinfo.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class SecurityConfigurationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    @BeforeAll
    static void initAll() {
        System.out.println("Starting SecurityConfigurationTest class");
    }

    @AfterEach
    void tearDown() {
        // Clear the security context after each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void testPasswordEncoder() {
        // Arrange
        String rawPassword = "password";

        // Act
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Assert
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void testAuthenticationManager() {
        // Assert
        assertThat(authenticationManager).isNotNull();
    }

    @Test
    void testSecurityContextRepository() {
        // Assert
        assertThat(securityContextRepository).isNotNull();
    }
}

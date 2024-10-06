package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        // Create a User object with some roles
        user = User.builder()
                .id(1L)
                .username("testuser")
                .hashedPassword("hashedPassword")
                .roles(Set.of(Role.ADMIN, Role.USER))
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .userType("STUDENT")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @AfterEach
    void tearDown() {
        user = null;
    }


    @Test
    void testGetAuthorities() {
        // Retrieve authorities from the user
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // Check if the correct authorities are present
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetFullName() {
        // Check if toString returns the correct full name
        assertEquals("John Doe", user.toString());
    }
}

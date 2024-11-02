package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserTranslationTest {

    private UserTranslation userTranslation;
    private User user;

    @BeforeEach
    void setUp() {
        // Create a User instance for testing
        user = new User();
        user.setId(1L);
        user.setFirstName("Alice");
        user.setLastName("Smith");

        // Create a UserTranslation instance with sample data
        userTranslation = new UserTranslation(user, Language.EN, "firstName", "Alice Translation");
        userTranslation.setId(1L);
    }

    @AfterEach
    void tearDown() {
        userTranslation = null;
        user = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, userTranslation.getId());
    }

    @Test
    void testGetUser() {
        // Check if getUser() returns the correct User object
        assertNotNull(userTranslation.getUser());
        assertEquals(1L, userTranslation.getUser().getId());
        assertEquals("Alice", userTranslation.getUser().getFirstName());
        assertEquals("Smith", userTranslation.getUser().getLastName());
    }

    @Test
    void testGetLocale() {
        // Check if getLocale() returns the correct locale
        assertEquals(Language.EN, userTranslation.getLocale());
    }

    @Test
    void testGetFieldName() {
        // Check if getFieldName() returns the correct field name
        assertEquals("firstName", userTranslation.getFieldName());
    }

    @Test
    void testGetTranslatedValue() {
        // Check if getTranslatedValue() returns the correct translated value
        assertEquals("Alice Translation", userTranslation.getTranslatedValue());
    }

    @Test
    void testSetters() {
        // Test the setters
        userTranslation.setLocale(Language.FI);
        userTranslation.setFieldName("lastName");
        userTranslation.setTranslatedValue("Alice Smith käännös");

        assertEquals(Language.FI, userTranslation.getLocale());
        assertEquals("lastName", userTranslation.getFieldName());
        assertEquals("Alice Smith käännös", userTranslation.getTranslatedValue());
    }
}

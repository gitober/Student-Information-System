package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubjectTranslationTest {

    private SubjectTranslation subjectTranslation;
    private Subject subject;

    @BeforeEach
    void setUp() {
        // Create a Subject instance for testing
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");

        // Create a SubjectTranslation instance with sample data
        subjectTranslation = new SubjectTranslation("EN", "subjectName", "Mathematics Translation");
        subjectTranslation.setId(1L);
        subjectTranslation.setSubject(subject);
    }

    @AfterEach
    void tearDown() {
        subjectTranslation = null;
        subject = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, subjectTranslation.getId());
    }

    @Test
    void testGetSubject() {
        // Check if getSubject() returns the correct Subject object
        assertNotNull(subjectTranslation.getSubject());
        assertEquals(1L, subjectTranslation.getSubject().getId());
        assertEquals("Mathematics", subjectTranslation.getSubject().getName());
    }

    @Test
    void testGetLocale() {
        // Check if getLocale() returns the correct locale
        assertEquals("EN", subjectTranslation.getLocale());
    }

    @Test
    void testGetFieldName() {
        // Check if getFieldName() returns the correct field name
        assertEquals("subjectName", subjectTranslation.getFieldName());
    }

    @Test
    void testGetTranslatedValue() {
        // Check if getTranslatedValue() returns the correct translated value
        assertEquals("Mathematics Translation", subjectTranslation.getTranslatedValue());
    }

    @Test
    void testSetters() {
        // Test the setters
        subjectTranslation.setLocale("FI");
        subjectTranslation.setFieldName("description");
        subjectTranslation.setTranslatedValue("Matematiikan kuvaus");

        assertEquals("FI", subjectTranslation.getLocale());
        assertEquals("description", subjectTranslation.getFieldName());
        assertEquals("Matematiikan kuvaus", subjectTranslation.getTranslatedValue());
    }
}

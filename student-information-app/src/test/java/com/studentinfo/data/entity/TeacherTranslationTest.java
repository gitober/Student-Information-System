package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeacherTranslationTest {

    private TeacherTranslation teacherTranslation;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Create a Teacher instance for testing
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        // Create a TeacherTranslation instance with sample data
        teacherTranslation = new TeacherTranslation("EN", "teacherName", "John Doe Translation");
        teacherTranslation.setId(1L);
        teacherTranslation.setTeacher(teacher);
    }

    @AfterEach
    void tearDown() {
        teacherTranslation = null;
        teacher = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, teacherTranslation.getId());
    }

    @Test
    void testGetTeacher() {
        // Check if getTeacher() returns the correct Teacher object
        assertNotNull(teacherTranslation.getTeacher());
        assertEquals(1L, teacherTranslation.getTeacher().getId());
        assertEquals("John", teacherTranslation.getTeacher().getFirstName());
        assertEquals("Doe", teacherTranslation.getTeacher().getLastName());
    }

    @Test
    void testGetLocale() {
        // Check if getLocale() returns the correct locale
        assertEquals("EN", teacherTranslation.getLocale());
    }

    @Test
    void testGetFieldName() {
        // Check if getFieldName() returns the correct field name
        assertEquals("teacherName", teacherTranslation.getFieldName());
    }

    @Test
    void testGetTranslatedValue() {
        // Check if getTranslatedValue() returns the correct translated value
        assertEquals("John Doe Translation", teacherTranslation.getTranslatedValue());
    }

    @Test
    void testSetters() {
        // Test the setters
        teacherTranslation.setLocale("FI");
        teacherTranslation.setFieldName("description");
        teacherTranslation.setTranslatedValue("John Doe kuvaus");

        assertEquals("FI", teacherTranslation.getLocale());
        assertEquals("description", teacherTranslation.getFieldName());
        assertEquals("John Doe kuvaus", teacherTranslation.getTranslatedValue());
    }
}

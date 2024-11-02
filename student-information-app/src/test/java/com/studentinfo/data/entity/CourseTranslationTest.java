package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseTranslationTest {

    private CourseTranslation courseTranslation;
    private Course course;

    @BeforeEach
    void setUp() {
        // Create a Course instance for testing
        course = new Course();
        course.setCourseId(1L);
        course.setCourseName("Mathematics");

        // Create a CourseTranslation instance with sample data
        courseTranslation = new CourseTranslation("EN", "courseName", "Mathematics Translation");
        courseTranslation.setId(1L);
        courseTranslation.setCourse(course);
    }

    @AfterEach
    void tearDown() {
        courseTranslation = null;
        course = null;
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, courseTranslation.getId());
    }

    @Test
    void testGetCourse() {
        // Check if getCourse() returns the correct Course object
        assertNotNull(courseTranslation.getCourse());
        assertEquals(1L, courseTranslation.getCourse().getCourseId());
        assertEquals("Mathematics", courseTranslation.getCourse().getCourseName());
    }

    @Test
    void testGetLocale() {
        // Check if getLocale() returns the correct locale
        assertEquals("EN", courseTranslation.getLocale());
    }

    @Test
    void testGetFieldName() {
        // Check if getFieldName() returns the correct field name
        assertEquals("courseName", courseTranslation.getFieldName());
    }

    @Test
    void testGetTranslatedValue() {
        // Check if getTranslatedValue() returns the correct translated value
        assertEquals("Mathematics Translation", courseTranslation.getTranslatedValue());
    }

    @Test
    void testSetters() {
        // Test the setters
        courseTranslation.setLocale("FI");
        courseTranslation.setFieldName("description");
        courseTranslation.setTranslatedValue("Matematiikan kuvaus");

        assertEquals("FI", courseTranslation.getLocale());
        assertEquals("description", courseTranslation.getFieldName());
        assertEquals("Matematiikan kuvaus", courseTranslation.getTranslatedValue());
    }
}

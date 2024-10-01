package com.studentinfo.data.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrollmentTest {

    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        // Create an Enrollment object with sample data
        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudentId(1001L);
        enrollment.setCourseId(2002L);
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, enrollment.getId());
    }

    @Test
    void testGetStudentId() {
        // Check if getStudentId() returns the correct student ID
        assertEquals(1001L, enrollment.getStudentId());
    }

    @Test
    void testGetCourseId() {
        // Check if getCourseId() returns the correct course ID
        assertEquals(2002L, enrollment.getCourseId());
    }
}

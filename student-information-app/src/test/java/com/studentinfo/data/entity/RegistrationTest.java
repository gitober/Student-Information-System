package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RegistrationTest {

    private Registration registration;

    @BeforeEach
    void setUp() {
        // Create a User object
        User user = new User();
        user.setId(1L);
        user.setFirstName("Alice");
        user.setLastName("Johnson");

        // Create a Registration object with sample data
        registration = new Registration();
        registration.setRegistrationId(1L);
        registration.setRegistrationDay(LocalDate.of(2024, 10, 1));
        registration.setCoursePayment(500.0);
        registration.setStudentNumber(12345L);
        registration.setBatchId(2L);
        registration.setCourseId(3L);
        registration.setUser(user);
    }

    @AfterEach
    void tearDown() {
        registration = null;
    }

    @Test
    void testGetRegistrationDay() {
        // Check if getRegistrationDay() returns the correct registration date
        assertEquals(LocalDate.of(2024, 10, 1), registration.getRegistrationDay());
    }

    @Test
    void testGetCoursePayment() {
        // Check if getCoursePayment() returns the correct payment amount
        assertEquals(500.0, registration.getCoursePayment());
    }

    @Test
    void testGetStudentNumber() {
        // Check if getStudentNumber() returns the correct student number
        assertEquals(12345L, registration.getStudentNumber());
    }

    @Test
    void testGetBatchId() {
        // Check if getBatchId() returns the correct batch ID
        assertEquals(2L, registration.getBatchId());
    }

    @Test
    void testGetCourseId() {
        // Check if getCourseId() returns the correct course ID
        assertEquals(3L, registration.getCourseId());
    }

    @Test
    void testGetUser() {
        // Check if getUser() returns the correct user
        assertNotNull(registration.getUser());
        assertEquals("Alice", registration.getUser().getFirstName());
        assertEquals("Johnson", registration.getUser().getLastName());
    }

    @Test
    void testGetStudent() {
        // Since we haven't set a Student object, the getStudent() should return null
        assertNull(registration.getStudent());
    }
}

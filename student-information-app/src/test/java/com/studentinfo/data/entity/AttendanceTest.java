package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AttendanceTest {

    private Attendance attendance;

    @BeforeEach
    void setUp() {
        // Create a sample Student
        Student student = new Student();
        student.setFirstName("John");
        student.setLastName("Doe");

        // Create a sample Course
        Course course = new Course();
        course.setCourseName("Mathematics");

        // Create an Attendance object with sample data
        attendance = new Attendance("Present", LocalDate.of(2024, 10, 1), student, course);
        attendance.setAttendanceId(1L);
    }

    @AfterEach
    void tearDown() {
        attendance = null;
    }

    @Test
    void testGetAttendanceStatus() {
        // Check if getAttendanceStatus() returns the correct attendance status
        assertEquals("Present", attendance.getAttendanceStatus());
    }

    @Test
    void testGetAttendanceDate() {
        // Check if getAttendanceDate() returns the correct attendance date
        assertEquals(LocalDate.of(2024, 10, 1), attendance.getAttendanceDate());
    }

    @Test
    void testGetStudent() {
        // Check if getStudent() returns the correct student
        assertNotNull(attendance.getStudent());
        assertEquals("John", attendance.getStudent().getFirstName());
        assertEquals("Doe", attendance.getStudent().getLastName());
    }

    @Test
    void testGetCourse() {
        // Check if getCourse() returns the correct course
        assertNotNull(attendance.getCourse());
        assertEquals("Mathematics", attendance.getCourse().getCourseName());
    }
}

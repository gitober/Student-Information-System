package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentTest {

    private Student student;

    @BeforeEach
    void setUp() {
        // Create a Student object with sample data
        student = new Student();
        student.setId(1L);
        student.setFirstName("Alice");
        student.setLastName("Johnson");
        student.setAddress("123 Main St");
        student.setGrade("A");
        student.setStudentClass("10th Grade");

        // Set sample courses
        Course course1 = new Course();
        course1.setCourseName("Mathematics");
        Course course2 = new Course();
        course2.setCourseName("History");

        Set<Course> courses = new HashSet<>();
        courses.add(course1);
        courses.add(course2);
        student.setCourses(courses);
    }

    @AfterEach
    void tearDown() {
        student = null;
    }

    @Test
    void testGetAddress() {
        // Check if getAddress() returns the correct address
        assertEquals("123 Main St", student.getAddress());
    }

    @Test
    void testGetGrade() {
        // Check if getGrade() returns the correct grade
        assertEquals("A", student.getGrade());
    }

    @Test
    void testGetStudentClass() {
        // Check if getStudentClass() returns the correct student class
        assertEquals("10th Grade", student.getStudentClass());
    }

    @Test
    void testGetCourses() {
        // Check if the courses list is not null and contains the expected number of courses
        assertNotNull(student.getCourses());
        assertEquals(2, student.getCourses().size());
        assertTrue(student.getCourses().stream().anyMatch(course -> "Mathematics".equals(course.getCourseName())));
        assertTrue(student.getCourses().stream().anyMatch(course -> "History".equals(course.getCourseName())));
    }
}

package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        // Create a Teacher object with sample data
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");

        // Set sample courses
        Course course1 = new Course();
        course1.setCourseName("Mathematics");
        Course course2 = new Course();
        course2.setCourseName("Physics");

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);

        teacher.setCourses(courses);
    }

    @AfterEach
    void tearDown() {
        teacher = null;
    }


    @Test
    void testGetTeacherId() {
        // Check if getTeacherId() returns the correct ID
        assertEquals(1L, teacher.getTeacherId());
    }

    @Test
    void testGetFullName() {
        // Check if getFullName() returns the correct full name
        assertEquals("Jane Smith", teacher.getFullName());
    }

    @Test
    void testGetCourses() {
        // Check if the courses list is not null and contains the expected number of courses
        assertNotNull(teacher.getCourses());
        assertEquals(2, teacher.getCourses().size());
    }
}

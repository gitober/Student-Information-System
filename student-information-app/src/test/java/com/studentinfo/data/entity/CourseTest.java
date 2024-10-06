package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        // Create sample teachers
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        // Create a list of teachers
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        // Create a Course object with sample data
        course = new Course("Mathematics", "Basic Math Concepts", 30);
        course.setCourseId(1L);
        course.setTeachers(teachers);
    }

    @AfterEach
    void tearDown() {
        course = null;
    }


    @Test
    void testGetCourseName() {
        // Check if getCourseName() returns the correct course name
        assertEquals("Mathematics", course.getCourseName());
    }

    @Test
    void testGetCoursePlan() {
        // Check if getCoursePlan() returns the correct course plan
        assertEquals("Basic Math Concepts", course.getCoursePlan());
    }

    @Test
    void testGetDuration() {
        // Check if getDuration() returns the correct duration
        assertEquals(30, course.getDuration());
    }

    @Test
    void testGetTeachers() {
        // Check if getTeachers() returns the correct list of teachers
        assertNotNull(course.getTeachers());
        assertEquals(2, course.getTeachers().size());
    }

    @Test
    void testGetFormattedDateRange() {
        // Check if getFormattedDateRange() returns the correct date range
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(course.getDuration());
        String expectedDateRange = startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        assertEquals(expectedDateRange, course.getFormattedDateRange());
    }
}

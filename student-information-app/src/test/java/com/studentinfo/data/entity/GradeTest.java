package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GradeTest {

    private Grade grade;

    @BeforeEach
    void setUp() {
        // Create a Course object
        Course course = new Course();
        course.setCourseName("Mathematics");

        // Create a Grade object with sample data
        grade = new Grade();
        grade.setGradeId(1);
        grade.setGradeValue("A");
        grade.setGradingDay(LocalDate.of(2024, 10, 1));
        grade.setStudentNumber(12345L);
        grade.setCourse(course);
    }

    @AfterEach
    void tearDown() {
        grade = null;
    }

    @Test
    void testGetGrade() {
        // Check if getGrade() returns the correct grade
        assertEquals("A", grade.getGradeValue());
    }

    @Test
    void testGetGradingDay() {
        // Check if getGradingDay() returns the correct grading day
        assertEquals(LocalDate.of(2024, 10, 1), grade.getGradingDay());
    }

    @Test
    void testGetStudentNumber() {
        // Check if getStudentNumber() returns the correct student number
        assertEquals(12345L, grade.getStudentNumber());
    }

    @Test
    void testGetCourse() {
        // Check if getCourse() returns the correct course
        assertNotNull(grade.getCourse());
        assertEquals("Mathematics", grade.getCourse().getCourseName());
    }
}

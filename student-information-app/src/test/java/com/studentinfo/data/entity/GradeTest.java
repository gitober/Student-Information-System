package com.studentinfo.data.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GradeTest {

    private Grade grade;
    private Course course;

    @BeforeEach
    void setUp() {
        // Create a Course object
        course = new Course();
        course.setCourseName("Mathematics");

        // Create a Grade object with sample data
        grade = new Grade();
        grade.setGradeId(1);
        grade.setGrade("A");
        grade.setGradingDay(LocalDate.of(2024, 10, 1));
        grade.setStudentNumber(12345L);
        grade.setCourse(course);
    }

    @Test
    void testGetGrade() {
        // Check if getGrade() returns the correct grade
        assertEquals("A", grade.getGrade());
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

package com.studentinfo.data.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DepartmentTest {

    private Department department;
    private Set<Teacher> teachers;
    private Set<Subject> subjects;

    @BeforeEach
    void setUp() {
        // Create a Department object with sample data
        department = new Department("Science");

        // Create a set of Teachers
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        teachers = new HashSet<>();
        teachers.add(teacher1);
        teachers.add(teacher2);

        // Create a set of Subjects
        Subject subject1 = new Subject();
        subject1.setName("Physics");

        Subject subject2 = new Subject();
        subject2.setName("Chemistry");

        subjects = new HashSet<>();
        subjects.add(subject1);
        subjects.add(subject2);

        department.setTeachers(teachers);
        department.setSubjects(subjects);
    }

    @Test
    void testGetDepartmentName() {
        // Check if getDepartmentName() returns the correct name
        assertEquals("Science", department.getDepartmentName());
    }

    @Test
    void testGetTeachers() {
        // Check if getTeachers() returns the correct set of teachers
        assertNotNull(department.getTeachers());
        assertEquals(2, department.getTeachers().size());
    }

    @Test
    void testGetSubjects() {
        // Check if getSubjects() returns the correct set of subjects
        assertNotNull(department.getSubjects());
        assertEquals(2, department.getSubjects().size());
    }
}

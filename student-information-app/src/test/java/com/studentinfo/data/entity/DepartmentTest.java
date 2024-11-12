package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DepartmentTest {

    private Department department;
    private Set<Subject> subjects;

    @BeforeEach
    void setUp() {
        // Create a Department object with sample data
        department = new Department("Science");

        // Create Subjects and set the department for each subject
        Subject subject1 = new Subject();
        subject1.setName("Physics");
        subject1.setDepartment(department); // Ensure subject is linked to department

        Subject subject2 = new Subject();
        subject2.setName("Chemistry");
        subject2.setDepartment(department); // Ensure subject is linked to department

        subjects = new HashSet<>();
        subjects.add(subject1);
        subjects.add(subject2);

        department.setSubjects(subjects);
    }


    @AfterEach
    void tearDown() {
        department = null;
        subjects = null;
    }


    @Test
    void testGetDepartmentName() {
        // Check if getDepartmentName() returns the correct name
        assertEquals("Science", department.getDepartmentName());
    }

    @Test
    void testGetSubjects() {
        // Check if getSubjects() returns the correct set of subjects
        assertNotNull(department.getSubjects());
        assertEquals(2, department.getSubjects().size());
    }
}

package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SubjectTest {

    private Subject subject;

    @BeforeEach
    void setUp() {
        // Create a sample Department
        Department department = new Department();
        department.setDepartmentName("Science");

        // Create a Subject object with sample data
        subject = new Subject("Physics", department);
        subject.setId(1L);
    }

    @AfterEach
    void tearDown() {
        subject = null;
    }


    @Test
    void testGetName() {
        // Check if getName() returns the correct name
        assertEquals("Physics", subject.getName());
    }

    @Test
    void testGetDepartment() {
        // Check if getDepartment() returns the correct department
        assertNotNull(subject.getDepartment());
        assertEquals("Science", subject.getDepartment().getDepartmentName());
    }

    @Test
    void testGetId() {
        // Check if getId() returns the correct ID
        assertEquals(1L, subject.getId());
    }
}

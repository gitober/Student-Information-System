package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDepartment = new Department();
        testDepartment.setName("Science");

        // Save the department to the database
        departmentRepository.save(testDepartment);
    }

    // Test the findByName method
    @Test
    void findByName() {
        Department department = departmentRepository.findByName("Science").orElse(null);

        assertNotNull(department);
        assertEquals("Science", department.getName());
    }

}
package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach
    void setUp() {
        // Create a test department
        Department testDepartment = new Department();
        testDepartment.setDepartmentName("Science");

        // Save the department to the database
        departmentRepository.save(testDepartment);
    }

    @AfterEach
    void tearDown() {
        departmentRepository.deleteAll();
    }


    // Test the findByName method
    @Test
    void findByName() {
        Department department = departmentRepository.findByName("Science").orElse(null);

        assertNotNull(department);
        assertEquals("Science", department.getDepartmentName());
    }

}
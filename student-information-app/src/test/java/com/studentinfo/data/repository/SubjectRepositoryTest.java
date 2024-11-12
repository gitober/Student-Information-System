package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test") // Ensure the test profile is used for test-specific configurations
class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        Department testDepartment = new Department();
        testDepartment.setDepartmentName("Science");

        // Save department and retrieve the auto-generated ID
        testDepartment = departmentRepository.save(testDepartment);

        // Create and save a test subject
        Subject testSubject = new Subject();
        testSubject.setName("Mathematics");
        testSubject.setDepartment(testDepartment); // Set the department relationship
        subjectRepository.save(testSubject);
    }

    @AfterEach
    void tearDown() {
        subjectRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void testFindName() {
        // Test repository query
        Subject subject = subjectRepository.findByName("Mathematics").orElse(null);

        assertNotNull(subject);
        assertEquals("Mathematics", subject.getName());
    }
}

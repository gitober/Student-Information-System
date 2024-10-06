package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class SubjectRepositoryTest {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() {
        Department testDepartment = new Department();
        testDepartment.setDepartmentName("Science");

        // Save the department to the database and retrieve the auto-generated ID
        testDepartment = departmentRepository.save(testDepartment);

        // Create a test subject
        Subject testSubject = new Subject();
        testSubject.setName("Mathematics");
        testSubject.setDepartment(testDepartment); // Set the department relationship

        // Save the subject to the database
        subjectRepository.save(testSubject);
    }

    @AfterEach
    void tearDown() {
        subjectRepository.deleteAll();
        departmentRepository.deleteAll();
    }


    @Test
    public void testFindName() {
        Subject subject = subjectRepository.findByName("Mathematics").orElse(null);

        assertNotNull(subject);
        assertEquals("Mathematics", subject.getName());
    }
}
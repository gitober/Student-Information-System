package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
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

    private Subject testSubject;

    private Department testDepartment;

    @BeforeEach
    public void setUp() {
        testDepartment = new Department();
        testDepartment.setDepartmentId(1L);
        testDepartment.setDepartmentName("Science");

        // Save the department to the database
        departmentRepository.save(testDepartment);

        // Create a test subject
        testSubject = new Subject();
        testSubject.setId(1L);
        testSubject.setName("Mathematics");
        testSubject.setDepartment(testDepartment);

        // Save the subject to the database
        subjectRepository.save(testSubject);
    }

    @Test
    public void testFindName() {
        Subject subject = subjectRepository.findByName("Mathematics").orElse(null);

        assertNotNull(subject);
        assertEquals("Mathematics", subject.getName());
    }
}
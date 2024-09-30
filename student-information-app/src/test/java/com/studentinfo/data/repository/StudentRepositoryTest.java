package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student testStudent;

    @BeforeEach
    public void setUp() {
        testStudent = new Student();
        testStudent.setUsername("John_doe");
        testStudent.setFirstName("John");
        testStudent.setEmail("john.doe.email.com");
        testStudent.setUserType("STUDENT");

        // Save the student to the database
        studentRepository.save(testStudent);
    }


    @Test
    public void testFindByUsername() {
        Student student = studentRepository.findByUsername("John_doe");

        assertNotNull(student);
        assertEquals("John_doe", student.getUsername());
        assertEquals("John", student.getFirstName());
        assertEquals("john.doe.email.com", student.getEmail());
        assertEquals("STUDENT", student.getUserType());
    }
}

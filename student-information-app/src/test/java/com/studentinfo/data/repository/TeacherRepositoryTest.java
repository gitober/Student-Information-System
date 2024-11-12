package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.entity.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @BeforeEach
    public void setUp() {
        // Setup department
        Department department = new Department("Science");
        department = departmentRepository.save(department); // Save the department to the database

        // Setup subject and link it to the department
        Subject subject = new Subject("Mathematics", department);
        subject = subjectRepository.save(subject); // Save the subject to the database

        // Create and set up the teacher with subject and department
        Teacher testTeacher = new Teacher();
        testTeacher.setUsername("John_doe");
        testTeacher.setFirstName("John");
        testTeacher.setEmail("john.doe.email.com");
        testTeacher.setUserType("TEACHER");
        testTeacher.setSubject(subject);  // Set the subject for the teacher
        testTeacher.setDepartment(department); // Set the department for the teacher

        // Save the teacher to the database
        teacherRepository.save(testTeacher);
    }

    @AfterEach
    void tearDown() {
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
        departmentRepository.deleteAll();
    }

    @Test
    void testFindByUsername() {
        // Find the teacher by username
        Optional<Teacher> optionalTeacher = teacherRepository.findByUsername("John_doe");

        // Assertions
        assertTrue(optionalTeacher.isPresent(), "Teacher should be present");
        Teacher teacher = optionalTeacher.get();

        assertNotNull(teacher);
        assertEquals("John_doe", teacher.getUsername());
        assertEquals("John", teacher.getFirstName());
        assertEquals("john.doe.email.com", teacher.getEmail());
        assertEquals("TEACHER", teacher.getUserType());

        // Assert that the subject and department are correctly set
        assertNotNull(teacher.getSubject());
        assertEquals("Mathematics", teacher.getSubject().getName());
        assertNotNull(teacher.getDepartment());
        assertEquals("Science", teacher.getDepartment().getDepartmentName());
    }
}

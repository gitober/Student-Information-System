package com.studentinfo.data.repository;


import com.studentinfo.data.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher testTeacher;

    @BeforeEach
    public void setUp() {
        testTeacher = new Teacher();
        testTeacher.setUsername("John_doe");
        testTeacher.setFirstName("John");
        testTeacher.setEmail("john.doe.email.com");

        // Save the teacher to the database
        teacherRepository.save(testTeacher);
    }

    @Test
    public void testFindByUsername() {
        Teacher teacher = teacherRepository.findByUsername("John_doe");

        assertNotNull(teacher);
        assertEquals("John_doe", teacher.getUsername());
        assertEquals("John Doe", teacher.getFirstName());
        assertEquals("john.doe.email.com", teacher.getEmail());
    }

}
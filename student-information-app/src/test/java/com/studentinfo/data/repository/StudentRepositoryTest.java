package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    private Student student;

    @BeforeEach
    public void setUp() {
        student = new Student();
        student.setUsername("John");
    }

    @Test
    public void testStudentGettersAndSetters() {
        // given
        student.setGrade("A");
        student.setStudentClass("10");

        // when
        String grade = student.getGrade();
        String studentClass = student.getStudentClass();

        // then
        assertEquals("A", grade, "Grade should be A");
        assertEquals("10", studentClass, "Student class should be 10");
    }

    @Test
    public void testStudentSaved() {
        // when
        Student savedStudent = studentRepository.save(student);

        // then
        assertNotNull(savedStudent, "Student should be saved");
        assertEquals("John", savedStudent.getFirstName(), "First name should be John");
        assertEquals("Doe", savedStudent.getLastName(), "Last name should be Doe");
    }

    @Test
    public void testFindByUsername() {
        // given
        studentRepository.save(student);

        // when
        Student foundStudent = studentRepository.findByUsername("John");

        // then
        assertNotNull(foundStudent, "Student should be found");
        assertEquals("John", foundStudent.getFirstName(), "First name should be John");
        assertEquals("Doe", foundStudent.getLastName(), "Last name should be Doe");
    }
}

package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.entity.Student;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
class GradeRepositoryTest {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        // Create and save a student
        student = new Student();
        student.setBirthday(LocalDate.of(2000, 1, 1));
        student.setEmail("test@example.com");
        student.setFirstName("John");
        student.setHashedPassword("hashed_password");
        student.setLastName("Doe");
        student.setPhoneNumber("1234567890");
        student.setStudentNumber(12345L);
        student.setUserType("STUDENT");
        student.setUsername("johndoe");
        studentRepository.saveAndFlush(student);

        // Create and save a course
        course = new Course();
        course.setCourseName("Sample Course");
        course.setCoursePlan("Sample Plan");
        course.setDuration(30);
        courseRepository.saveAndFlush(course);

        // Save sample grades for the student
        Grade grade1 = new Grade();
        grade1.setStudentNumber(student.getStudentNumber());
        grade1.setCourse(course);
        grade1.setGradeValue("A");
        grade1.setGradingDay(LocalDate.now());
        gradeRepository.save(grade1);

        Grade grade2 = new Grade();
        grade2.setStudentNumber(student.getStudentNumber());
        grade2.setCourse(course);
        grade2.setGradeValue("B");
        grade2.setGradingDay(LocalDate.now());
        gradeRepository.save(grade2);
    }

    @AfterEach
    void tearDown() {
        gradeRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }


    @Test
    void testFindByStudentNumber() {
        List<Grade> grades = gradeRepository.findByStudentNumber(student.getStudentNumber());
        assertNotNull(grades);
        assertEquals(2, grades.size());
    }

    @Test
    void testFindByCourse_CourseId() {
        List<Grade> grades = gradeRepository.findByCourse_CourseId(course.getCourseId()); // Use correct Course ID
        assertNotNull(grades);
        assertEquals(2, grades.size());
    }
}

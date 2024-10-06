package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Registration;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.User; // Ensure to import the User entity
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
class RegistrationRepositoryTest {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository; // Add the user repository

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        // Create and save a test user
        // Add a user variable
        User user = new User();
        user.setUsername("john_doe");
        user.setEmail("john.doe@example.com"); // Ensure the email is set
        user.setHashedPassword("hashed_password");
        user.setUserType("STUDENT"); // Ensure userType is set
        userRepository.saveAndFlush(user);

        // Create and save a test student
        student = new Student();
        student.setBirthday(LocalDate.of(2000, 1, 1));
        student.setEmail("test@example.com");
        student.setFirstName("John");
        student.setHashedPassword("hashed_password");
        student.setLastName("Doe");
        student.setPhoneNumber("1234567890");
        student.setStudentNumber(12345L);
        student.setUserType("STUDENT");
        student.setUsername("johndoe"); // Ensure this line is present
        studentRepository.saveAndFlush(student);

        // Create and save a test course
        course = new Course();
        course.setCourseName("Mathematics");
        course.setCoursePlan("Math Plan");
        course.setDuration(30);
        courseRepository.saveAndFlush(course);

        // Create and save a test registration
        // Add a registration variable
        Registration registration = new Registration();
        registration.setRegistrationDay(LocalDate.now());
        registration.setStudentNumber(student.getStudentNumber());
        registration.setCourseId(course.getCourseId());
        registration.setStudent(student);
        registration.setUser(user); // Associate user with registration
        registrationRepository.saveAndFlush(registration);
    }

    @AfterEach
    void tearDown() {
        registrationRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    void testFindCoursesByStudentNumber() {
        List<Course> courses = registrationRepository.findCoursesByStudentNumber(student.getStudentNumber());
        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course.getCourseName(), courses.getFirst().getCourseName());
    }

    @Test
    void testFindStudentsByCourseId() {
        List<Student> students = registrationRepository.findStudentsByCourseId(course.getCourseId());
        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals(student.getStudentNumber(), students.getFirst().getStudentNumber());
    }
}

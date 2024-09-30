package com.studentinfo.controller;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    Student createdStudent, updatedStudent, newStudent;


    @BeforeAll
    static void setUp() {
        System.out.println("Starting the studentControllerTest class");
    }

    @BeforeEach
    void init() {
        // create a mock instance of StudentService
        MockitoAnnotations.openMocks(this);
        studentController = new StudentController(studentService);
        newStudent = new Student();
        createdStudent = new Student();
        updatedStudent = new Student();

        newStudent.setId(1L);
        newStudent.setFirstName("John");
        newStudent.setLastName("Doe");

        updatedStudent = newStudent;
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Wayne");

    }

    @AfterEach
    void tearDownEach() {
        studentController = null;
        newStudent = null;
        createdStudent = null;
        updatedStudent = null;
    }


    // unit test for StudentController.getAllStudents()
    @Test
    void testGetAllStudents() {
        List<Student> studentList = List.of(newStudent);

        // Mock the studentService.list() method
        when(studentService.list()).thenReturn(studentList);

        // Act
        List<Student> students = studentController.getAllStudents();

        // Assert
        assertNotNull(students);
        assertEquals(students, studentController.getAllStudents());
        assertEquals(studentService.list(), studentController.getAllStudents());
    }



    @AfterAll
    static void tearDown() {
        System.out.println("Finishing the studentControllerTest class");
    }

}
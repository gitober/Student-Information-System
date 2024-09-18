package com.studentinfo.controller;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    Student student;


    @BeforeAll
    static void setUp() {
        System.out.println("Starting the studentControllerTest class");
    }

    @BeforeEach
    void init() {
        // create a mock instance of StudentService
        MockitoAnnotations.openMocks(this);
        studentController = new StudentController(studentService);
        student = new Student();
    }

    @AfterEach
    void tearDownEach() {
        studentController = null;
        student = null;
    }


    // unit test for StudentController.getAllStudents()
    @Test
    void testGetAllStudents() {
        // call the getAllStudents() method
        List<Student> students = studentController.getAllStudents();
        // check if the list of students is not null
        assertNotNull(students);
    }

    // unit test for StudentController.getStudentById()
    @Test
    void testGetStudentById() {
        // call the getStudentById() method
        Optional<Student> student = studentController.getStudentById(1L);
        // check if the student is not null
        assertNotNull(student);
    }

    // unit test for StudentController.createStudent()
    @Test
    void testCreateStudent() {
        // create a new student
        student.setFirstName("John");
        student.setLastName("Doe");

        // mock the save() method of StudentService
        when(studentService.save(student)).thenReturn(student);

        // call the createStudent() method
        Student newStudent = studentController.createStudent(student);
        // check if the new student is not null
        assertNotNull(newStudent);
        assertEquals("John", newStudent.getFirstName());
        assertEquals("Doe", newStudent.getLastName());
    }

    // unit test for StudentController.updateStudent()
    @Test
    void testUpdateStudent() {
        // create a new student
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");

        // mock the save() method of StudentService
        when(studentService.save(student)).thenReturn(student);

        // call the updateStudent() method
        Student updatedStudent = studentController.updateStudent(1L, student);
        // check if the updated student is not null
        assertNotNull(updatedStudent);
    }

    // unit test for StudentController.deleteStudent()
    @Test
    void testDeleteStudent() {
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");

        // mock the save() method of StudentService
        when(studentService.save(student)).thenReturn(student);

        // call the deleteStudent() method
        studentController.deleteStudent(1L);
        // check if the student is deleted
        //assertNull(studentController.getStudentById(1L));
        assertEquals(Optional.empty(), studentController.getStudentById(1L));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Finishing the studentControllerTest class");
    }

}
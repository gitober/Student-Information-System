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

    // unit test for StudentController.getStudentById()
    @Test
    void testGetStudentById() {
        List<Student> studentList = List.of(newStudent);

        // Mock the studentService.list() method
        when(studentService.list()).thenReturn(studentList);

        // Act
        Optional<Student> student = studentController.getStudentById(1L);

        // check if the student is not null
        assertNotNull(student);
        System.out.println("Student: " + student);
    }

    // unit test for StudentController.createStudent()
    @Test
    void testCreateStudent() {
        List<Student> studentList = List.of(newStudent);
        // save the new student by calling createStudent() method
        when(studentService.list()).thenReturn(studentList);

        // call the createStudent() method
        createdStudent = studentController.createStudent(newStudent);

        // check if the new student is created
        assertNotNull(createdStudent);
        assertEquals("John", createdStudent.getStudentClass());
        assertEquals("Doe", createdStudent.getLastName());



        // check if the new student is not null
        assertNotNull(studentList);
        assertEquals(studentList, studentController.getAllStudents());


    }

    // unit test for StudentController.updateStudent()
    @Test
    void testUpdateStudent() {
        // create a new student
        newStudent.setFirstName("John");
        newStudent.setLastName("Doe");

        // save the new student by calling createStudent() method
        when(studentController.createStudent(newStudent)).thenReturn(newStudent);

        // call the createStudent() method
        createdStudent = studentController.createStudent(newStudent);

        // test if the new student is created
        assertEquals("John", newStudent.getFirstName());
        assertEquals("Doe", newStudent.getLastName());

        // update the student
        newStudent.setFirstName("Jane");
        newStudent.setLastName("Wayne");

        // call the updateStudent() method
        updatedStudent = studentController.updateStudent(1L, newStudent);

        // check if the updated student is not null
        assertNotNull(updatedStudent);
        assertEquals("Jane", updatedStudent.getFirstName());
        assertEquals("Wayne", updatedStudent.getLastName());
    }

    // unit test for StudentController.deleteStudent()
    @Test
    void testDeleteStudent() {
        newStudent.setId(1L);
        newStudent.setFirstName("John");
        newStudent.setLastName("Doe");

        // mock the save() method of StudentService
        when(studentController.createStudent(newStudent)).thenReturn(newStudent);

        // call the createStudent() method
        createdStudent = studentController.createStudent(newStudent);

        // test if the new student is created
        assertEquals("John", newStudent.getFirstName());
        assertEquals("Doe", newStudent.getLastName());
        assertEquals(1L, newStudent.getId());

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
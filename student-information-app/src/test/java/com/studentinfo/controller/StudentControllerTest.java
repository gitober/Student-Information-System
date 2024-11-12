package com.studentinfo.controller;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private Student newStudent;
    private Student updatedStudent;

    @BeforeAll
    static void setUpAll() {
        System.out.println("Starting the StudentControllerTest class");
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        newStudent = new Student();
        updatedStudent = new Student();

        newStudent.setId(1L);
        newStudent.setFirstName("John");
        newStudent.setLastName("Doe");

        updatedStudent.setId(1L);
        updatedStudent.setFirstName("Jane");
        updatedStudent.setLastName("Wayne");
    }

    @AfterEach
    void tearDownEach() {
        // Reset mocks after each test to avoid shared state
        reset(studentService);
    }

    // Unit test for StudentController.getAllStudents()
    @Test
    void testGetAllStudents() {
        List<Student> studentList = List.of(newStudent);

        when(studentService.list()).thenReturn(studentList);

        List<Student> students = studentController.getAllStudents();

        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals(studentList, students);
    }

    // Unit test for StudentController.getStudentById()
    @Test
    void testGetStudentById() {
        when(studentService.get(1L)).thenReturn(Optional.of(newStudent));

        ResponseEntity<Student> response = studentController.getStudentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(newStudent, response.getBody());
    }

    // Unit test for StudentController.createStudent()
    @Test
    void testCreateStudent() {
        when(studentService.save(newStudent)).thenReturn(newStudent);

        ResponseEntity<Student> response = studentController.createStudent(newStudent);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("John", Objects.requireNonNull(response.getBody()).getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }

    // Unit test for StudentController.updateStudent()
    @Test
    void testUpdateStudent() {
        when(studentService.get(1L)).thenReturn(Optional.of(newStudent));
        when(studentService.save(updatedStudent)).thenReturn(updatedStudent);

        ResponseEntity<Student> response = studentController.updateStudent(1L, updatedStudent);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals("Jane", Objects.requireNonNull(response.getBody()).getFirstName());
        assertEquals("Wayne", response.getBody().getLastName());
    }

    // Unit test for StudentController.deleteStudent()
    @Test
    void testDeleteStudent() {
        when(studentService.get(1L)).thenReturn(Optional.of(newStudent));

        ResponseEntity<Void> response = studentController.deleteStudent(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finishing the StudentControllerTest class");
    }
}

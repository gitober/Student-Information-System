package com.studentinfo.controller;

import com.studentinfo.data.entity.Student;
import org.junit.jupiter.api.*;
import org.springframework.test.annotation.IfProfileValue;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerTest {

    private StudentController studentController;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting the studentControllerTest class");
    }

    @BeforeEach
    void init() {
        // create an instance of StudentController
        studentController = new StudentController(/* tähän pitäis jotain keksii :/ */);
    }

    @AfterEach
    void tearDownEach() {
        studentController = null;
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
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        // call the createStudent() method
        Student newStudent = studentController.createStudent(student);
        // check if the new student is not null
        assertNotNull(newStudent);
    }

    // unit test for StudentController.updateStudent()
    @Test
    void testUpdateStudent() {
        // create a new student
        Student student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        // call the updateStudent() method
        Student updatedStudent = studentController.updateStudent(1L, student);
        // check if the updated student is not null
        assertNotNull(updatedStudent);
    }

    // unit test for StudentController.deleteStudent()
    @Test
    void testDeleteStudent() {
        // call the deleteStudent() method
        studentController.deleteStudent(1L);
        // check if the student is deleted
        assertNull(studentController.getStudentById(1L));
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Finishing the studentControllerTest class");
    }

}
package com.studentinfo.controller;

import com.studentinfo.data.entity.Teacher;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TeacherControllerTest {

    private TeacherController teacherController;

    @BeforeAll
    static void setUp() {
        System.out.println("Starting the teacherControllerTest class");
    }

    @BeforeEach
    void init() {
        // create an instance of TeacherController
        teacherController = new TeacherController();
    }

    @AfterEach
    void tearDownEach() {
        teacherController = null;
    }

    // unit test for TeacherController.getAllTeachers()
    @Test
    void testGetAllTeachers() {
        // call the getAllTeachers() method
        List<Teacher> teachers = teacherController.getAllTeachers();
        // check if the list of teachers is not null
        assertNotNull(teachers);
    }

    // unit test for TeacherController.getTeacherById()
    @Test
    void testGetTeacherById() {
        // call the getTeacherById() method
        Optional<Teacher> teacher = teacherController.getTeacherById(1L);
        // check if the teacher is not null
        assertNotNull(teacher);
    }

    // unit test for TeacherController.createTeacher()
    @Test
    void testCreateTeacher() {
        // create a new teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        // call the createTeacher() method
        Teacher newTeacher = teacherController.createTeacher(teacher);
        // check if the new teacher is not null
        assertNotNull(newTeacher);
    }

    // unit test for TeacherController.updateTeacher()
    @Test
    void testUpdateTeacher() {
        // create a new teacher
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        // call the updateTeacher() method
        Teacher updatedTeacher = teacherController.updateTeacher(1L, teacher);
        // check if the updated teacher is not null
        assertNotNull(updatedTeacher);
    }

    @Test
    void testDeleteTeacher() {
        // call the deleteTeacher() method
        teacherController.deleteTeacher(1L);
        // check if the method runs without errors
        assertTrue(true);
    }

    @AfterAll
    static void tearDown() {
        System.out.println("Completed the teacherControllerTest class");
    }

}
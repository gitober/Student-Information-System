package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.repository.StudentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(studentRepository);
    }

    @Test
    void testGetStudentById() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.get(id);

        assertTrue(result.isPresent());
        assertEquals(student, result.get());
        verify(studentRepository, times(1)).findById(id);
    }

    @Test
    void testSaveStudent() {
        Student student = new Student();

        when(studentRepository.save(student)).thenReturn(student);

        Student result = studentService.save(student);

        assertEquals(student, result);
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void testDeleteStudent() {
        Long id = 1L;

        doNothing().when(studentRepository).deleteById(id);

        studentService.delete(id);

        verify(studentRepository, times(1)).deleteById(id);
    }

    @Test
    void testListAllStudents() {
        Student student1 = new Student();
        Student student2 = new Student();
        List<Student> students = List.of(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.list();

        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student2));
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetStudentsByCourseId() {
        Long courseId = 1L;
        Student student1 = new Student();
        Student student2 = new Student();
        List<Student> students = List.of(student1, student2);

        when(studentRepository.findByCourses_CourseId(courseId)).thenReturn(students);

        List<Student> result = studentService.getStudentsByCourseId(courseId);

        assertEquals(2, result.size());
        assertTrue(result.contains(student1));
        assertTrue(result.contains(student2));
        verify(studentRepository, times(1)).findByCourses_CourseId(courseId);
    }

    @Test
    void testGetStudentByNumber() {
        Long studentNumber = 12345L;
        Student student = new Student();
        student.setStudentNumber(studentNumber);

        // Mock the return value for the repository method to return an Optional containing the student
        when(studentRepository.findByStudentNumber(studentNumber)).thenReturn(Optional.of(student));

        // Call the service method which now returns an Optional
        Optional<Student> result = studentService.getStudentByNumber(studentNumber);

        // Assert that the result is present
        assertTrue(result.isPresent(), "The student should be present.");

        // Assert that the student returned is the same as the mocked one
        assertEquals(student, result.get(), "The student returned should be the same as the mocked student.");

        // Verify that the repository method was called once with the correct student number
        verify(studentRepository, times(1)).findByStudentNumber(studentNumber);
    }

}

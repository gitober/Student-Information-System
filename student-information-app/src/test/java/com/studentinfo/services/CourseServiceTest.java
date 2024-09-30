package com.studentinfo.services;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Registration;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.RegistrationRepository;
import com.studentinfo.data.repository.StudentRepository;
import com.studentinfo.data.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCourseWithTeachers() {
        // Arrange
        Course course = new Course("Math 101", null, null);
        Teacher teacher = new Teacher();
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(teacherRepository.findById(teacher.getTeacherId())).thenReturn(Optional.of(teacher));

        // Act
        Course savedCourse = courseService.saveCourse(course, List.of(teacher));

        // Assert
        assertNotNull(savedCourse);
        verify(courseRepository, times(1)).save(course);
        verify(teacherRepository, times(1)).findById(teacher.getTeacherId());
        verify(teacherRepository, times(1)).save(teacher);
    }

    @Test
    void testEnrollInCourse() {
        // Arrange
        Student student = new Student();
        student.setStudentNumber(12345L);
        when(studentRepository.findByStudentNumber(12345L)).thenReturn(Optional.of(student));

        // Act
        courseService.enrollInCourse(12345L, 1L, 2L, 500.0);

        // Assert
        verify(studentRepository, times(1)).findByStudentNumber(12345L);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void testGetEnrolledCourses() {
        // Arrange
        Course course1 = new Course("Math 101", null, null);
        Course course2 = new Course("Physics 101", null, null);
        when(registrationRepository.findCoursesByStudentNumber(12345L))
                .thenReturn(Arrays.asList(course1, course2));

        // Act
        List<Course> courses = courseService.getEnrolledCourses(12345L);

        // Assert
        assertEquals(2, courses.size());
        assertEquals("Math 101", courses.get(0).getCourseName());
        assertEquals("Physics 101", courses.get(1).getCourseName());
        verify(registrationRepository, times(1)).findCoursesByStudentNumber(12345L);
    }

    @Test
    void testDeleteCourse() {
        // Arrange
        when(courseRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = courseService.deleteCourse(1L);

        // Assert
        assertTrue(result);
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).deleteById(1L);
    }
}

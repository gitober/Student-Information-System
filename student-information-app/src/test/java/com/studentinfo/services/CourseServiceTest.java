package com.studentinfo.services;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.repository.CourseRepository;
import com.studentinfo.data.repository.RegistrationRepository;
import com.studentinfo.data.repository.StudentRepository;
import com.studentinfo.data.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private TranslationService translationService; // Add mock for TranslationService

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(courseRepository, registrationRepository, teacherRepository, studentRepository, translationService);
    }

    @Test
    void testSaveCourseWithTeachers() {
        // Arrange
        Course course = new Course("Math 101", null, 30); // Adjusted for constructor arguments
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        // Create mock translations
        List<CourseTranslation> mockTranslations = List.of(new CourseTranslation("EN", "course_name", "Math 101"));

        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(teacherRepository.findById(teacher.getId())).thenReturn(Optional.of(teacher));

        // Act
        Course savedCourse = courseService.saveCourse(course, List.of(teacher), mockTranslations);

        // Assert
        assertNotNull(savedCourse);
        verify(courseRepository, times(1)).save(course);
        verify(teacherRepository, times(1)).findById(teacher.getId());
        verify(teacherRepository, times(1)).save(teacher);
        verify(translationService, times(1)).saveCourseTranslations(mockTranslations); // Verify interaction with TranslationService
    }
}

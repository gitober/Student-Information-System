package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.studentinfo.data.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TranslationServiceTest {

    @Mock
    private CourseTranslationRepository courseTranslationRepository;

    @Mock
    private DepartmentTranslationRepository departmentTranslationRepository;

    @Mock
    private SubjectTranslationRepository subjectTranslationRepository;

    @Mock
    private TeacherTranslationRepository teacherTranslationRepository;

    @Mock
    private UserTranslationRepository userTranslationRepository;

    @InjectMocks
    private TranslationService translationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(courseTranslationRepository, departmentTranslationRepository, subjectTranslationRepository,
                teacherTranslationRepository, userTranslationRepository);
    }

    @Test
    void testGetUserTranslationsByLocale() {
        Language locale = Language.EN;
        List<UserTranslation> mockTranslations = List.of(new UserTranslation());
        when(userTranslationRepository.findByLocale(locale)).thenReturn(mockTranslations);

        List<UserTranslation> result = translationService.getUserTranslationsByLocale(locale);

        assertThat(result).isNotEmpty();
        verify(userTranslationRepository, times(1)).findByLocale(locale);
    }

    @Test
    void testGetUserTranslations() {
        Long userId = 1L;
        Language locale = Language.EN;
        List<UserTranslation> mockTranslations = List.of(new UserTranslation());
        when(userTranslationRepository.findByUser_IdAndLocale(userId, locale)).thenReturn(mockTranslations);

        List<UserTranslation> result = translationService.getUserTranslations(userId, locale);

        assertThat(result).isNotEmpty();
        verify(userTranslationRepository, times(1)).findByUser_IdAndLocale(userId, locale);
    }

    @Test
    void testGetCourseTranslationsByLocale() {
        String locale = "EN";
        List<CourseTranslation> mockTranslations = List.of(new CourseTranslation());
        when(courseTranslationRepository.findByLocale(locale)).thenReturn(mockTranslations);

        List<CourseTranslation> result = translationService.getCourseTranslationsByLocale(locale);

        assertThat(result).isNotEmpty();
        verify(courseTranslationRepository, times(1)).findByLocale(locale);
    }
}
package com.studentinfo.data.repository;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
class CourseTranslationRepositoryTest {

    @Autowired
    private CourseTranslationRepository courseTranslationRepository;

    @Autowired
    private CourseRepository courseRepository;

    private Course course;

    @BeforeEach
    void setUp() {
        // Create and save a Course instance
        course = new Course();
        course.setCourseName("Test Course");
        course.setCoursePlan("Test Plan");
        course.setDuration(30);
        courseRepository.saveAndFlush(course);

        // Create and save CourseTranslation instances
        CourseTranslation translation1 = new CourseTranslation("EN", "courseName", "Test Course Translation");
        translation1.setCourse(course);
        courseTranslationRepository.saveAndFlush(translation1);

        CourseTranslation translation2 = new CourseTranslation("FI", "courseName", "Testikurssi Käännös");
        translation2.setCourse(course);
        courseTranslationRepository.saveAndFlush(translation2);
    }

    @AfterEach
    void tearDown() {
        courseTranslationRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    void testFindByCourse_CourseIdAndLocale() {
        List<CourseTranslation> translations = courseTranslationRepository.findByCourse_CourseIdAndLocale(course.getCourseId(), "EN");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Test Course Translation", translations.get(0).getTranslatedValue());
    }

    @Test
    void testFindByCourse_CourseIdAndLocaleAndFieldName() {
        Optional<CourseTranslation> translation = courseTranslationRepository.findByCourse_CourseIdAndLocaleAndFieldName(course.getCourseId(), "FI", "courseName");
        assertTrue(translation.isPresent());
        assertEquals("Testikurssi Käännös", translation.get().getTranslatedValue());
    }

    @Test
    void testFindByLocale() {
        List<CourseTranslation> translations = courseTranslationRepository.findByLocale("EN");
        assertNotNull(translations);
        assertEquals(1, translations.size());
        assertEquals("Test Course Translation", translations.get(0).getTranslatedValue());
    }
}

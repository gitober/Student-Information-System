package com.studentinfo.data.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseTest {

    private Course course;

    @BeforeEach
    void setUp() {
        // Create sample teachers
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        course = new Course("Mathematics", "Basic Math Concepts", 30);
        course.setCourseId(1L);
        course.setTeachers(Arrays.asList(teacher1, teacher2));
    }

    @AfterEach
    void tearDown() {
        course = null;
    }

    @ParameterizedTest
    @MethodSource("provideLocaleAndExpectedPattern")
    void testGetFormattedDateRange(Locale locale, String pattern) {
        LocaleContextHolder.setLocale(locale);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(course.getDuration());

        String expectedDateRange = startDate.format(DateTimeFormatter.ofPattern(pattern, locale)) +
                " - " + endDate.format(DateTimeFormatter.ofPattern(pattern, locale));

        assertEquals(expectedDateRange, course.getFormattedDateRange());
    }

    private static Stream<Object[]> provideLocaleAndExpectedPattern() {
        return Stream.of(
                new Object[]{Locale.ENGLISH, "dd/MM/yyyy"},
                new Object[]{Locale.forLanguageTag("zh"), "yyyy年MM月dd日"},  // Chinese date pattern
                new Object[]{Locale.forLanguageTag("fi-FI"), "dd.MM.yyyy"}, // Finnish date pattern
                new Object[]{Locale.forLanguageTag("ru-RU"), "dd.MM.yyyy"}  // Russian date pattern
        );
    }
}

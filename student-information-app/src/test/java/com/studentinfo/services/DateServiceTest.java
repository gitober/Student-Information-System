package com.studentinfo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateServiceTest {

    private DateService dateService;

    @BeforeEach
    void setUp() {
        dateService = new DateService();
    }

    @ParameterizedTest
    @MethodSource("provideLocalesAndExpectedFormats")
    void testFormatDateRange(Locale locale, String expectedFormat) {
        // Set the locale
        LocaleContextHolder.setLocale(locale);

        // Define the start and end dates
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        // Get the formatted date range
        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        // Define the expected date range format based on the input
        String expectedDateRange = startDate.format(DateTimeFormatter.ofPattern(expectedFormat, locale)) +
                " - " + endDate.format(DateTimeFormatter.ofPattern(expectedFormat, locale));

        // Assert that the actual formatted range matches the expected format
        assertEquals(expectedDateRange, formattedDateRange);
    }

    // Method source providing different locales and expected patterns for each
    private static Stream<Object[]> provideLocalesAndExpectedFormats() {
        return Stream.of(
                new Object[]{Locale.ENGLISH, "dd/MM/yyyy"},
                new Object[]{Locale.UK, "dd/MM/yyyy"},
                new Object[]{Locale.forLanguageTag("fi-FI"), "dd.MM.yyyy"},
                new Object[]{Locale.forLanguageTag("ch"), "yyyy年MM月dd日"},
                new Object[]{Locale.forLanguageTag("ru-RU"), "dd.MM.yyyy"}
        );
    }
}

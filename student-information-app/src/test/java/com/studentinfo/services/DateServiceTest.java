package com.studentinfo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DateServiceTest {

    private DateService dateService;

    @BeforeEach
    void setUp() {
        dateService = new DateService();
    }

    @Test
    void testFormatDateRange_EnglishLocale() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        assertEquals("01/11/2024 - 10/11/2024", formattedDateRange);
    }

    @Test
    void testFormatDateRange_UKLocale() {
        LocaleContextHolder.setLocale(Locale.UK);
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        assertEquals("01/11/2024 - 10/11/2024", formattedDateRange);
    }

    @Test
    void testFormatDateRange_FinnishLocale() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("fi-FI"));
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        assertEquals("01.11.2024 - 10.11.2024", formattedDateRange);
    }

    @Test
    void testFormatDateRange_ChineseLocale() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ch"));
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        assertEquals("2024年11月01日 - 2024年11月10日", formattedDateRange);
    }

    @Test
    void testFormatDateRange_RussianLocale() {
        LocaleContextHolder.setLocale(Locale.forLanguageTag("ru-RU"));
        LocalDate startDate = LocalDate.of(2024, 11, 1);
        LocalDate endDate = LocalDate.of(2024, 11, 10);

        String formattedDateRange = dateService.formatDateRange(startDate, endDate);

        assertEquals("01.11.2024 - 10.11.2024", formattedDateRange);
    }
}

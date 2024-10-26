package com.studentinfo.services;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class DateService {

    public String formatDateRange(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = getFormatterForCurrentLocale();
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

    private DateTimeFormatter getFormatterForCurrentLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.CHINA.equals(locale)) {
            return DateTimeFormatter.ofPattern("yyyy年MM月dd日", Locale.CHINA);
        } else if (Locale.UK.equals(locale)) {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.UK);
        } else if (Locale.forLanguageTag("fi-FI").equals(locale)) {
            return DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("fi-FI"));
        } else if (Locale.forLanguageTag("ru-RU").equals(locale)) {
            return DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.forLanguageTag("ru-RU"));
        } else {
            return DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
        }
    }
}

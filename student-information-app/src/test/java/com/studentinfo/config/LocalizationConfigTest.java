package com.studentinfo.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest
@ContextConfiguration(classes = LocalizationConfig.class)
@ExtendWith(MockitoExtension.class)
public class LocalizationConfigTest {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @BeforeEach
    public void setUp() {
        if (localeResolver instanceof CookieLocaleResolver cookieLocaleResolver) {
            cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        }
    }

    @AfterEach
    public void tearDown() {
        // Reset locale or perform any cleanup if needed
        if (localeResolver instanceof CookieLocaleResolver cookieLocaleResolver) {
            cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH); // Reset to default after each test
        }
    }

    @Test
    public void testDefaultLocaleIsEnglish() {
        String message = messageSource.getMessage("forgot.email", null, Locale.ENGLISH);
        assertEquals("Email", message); // Replace with actual English message
    }

    @Test
    public void testLocaleChangeToFinnish() {
        String message = messageSource.getMessage("forgot.email", null, Locale.forLanguageTag("fi"));
        assertEquals("Sähköpostiosoite", message); // Replace with actual Finnish message
    }

    @Test
    public void testLocaleChangeToChinese() {
        String message = messageSource.getMessage("forgot.email", null, Locale.forLanguageTag("ch"));
        assertEquals("电子邮件", message); // Replace with actual Chinese message
    }

    @Test
    public void testLocaleChangeToRussian() {
        String message = messageSource.getMessage("forgot.email", null, Locale.forLanguageTag("ru"));
        assertEquals("Электронная почта", message); // Replace with actual Russian message
    }
}

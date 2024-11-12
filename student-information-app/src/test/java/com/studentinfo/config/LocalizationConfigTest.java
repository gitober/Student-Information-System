package com.studentinfo.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@WebMvcTest
@ContextConfiguration(classes = LocalizationConfig.class)
@ExtendWith(MockitoExtension.class)
class LocalizationConfigTest {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    private final List<String> messageKeys = Arrays.asList(
            "app.logo", "header.title", "header.home", "header.courses", "header.grades", "header.attendance",
            "header.students", "header.profile", "header.logout", "forgot.title", "forgot.instructions",
            "forgot.email", "forgot.submit", "forgot.back", "reset.title", "reset.newpassword", "reset.button",
            "reset.close", "email.notification.success", "email.notification.failure", "reset.notification.success",
            "reset.notification.failure", "reset.token.invalid", "reset.newpassword.empty", "login.title",
            "login.email", "login.email.placeholder", "login.password", "login.password.placeholder",
            "login.signin", "login.signup", "login.forgotPassword", "login.rememberMe", "login.welcome",
            "grades.title", "grades.description", "grades.search", "grades.course", "grades.grade",
            "grades.date", "grades.view.error.student.not.found", "student.edit.profile.title",
            "student.edit.profile.currentDetails", "student.edit.profile.firstName", "student.edit.profile.lastName",
            "student.edit.profile.phoneNumber", "student.edit.profile.email", "student.edit.profile.newPassword",
            "student.edit.profile.save", "student.edit.profile.name", "student.edit.profile.success"
    );

    private final List<Locale> locales = List.of(
            Locale.ENGLISH,
            Locale.forLanguageTag("fi"),
            Locale.forLanguageTag("ch"),
            Locale.forLanguageTag("ru")
    );

    @BeforeEach
    public void setUp() {
        if (localeResolver instanceof CookieLocaleResolver cookieLocaleResolver) {
            cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        }
    }

    @AfterEach
    public void tearDown() {
        if (localeResolver instanceof CookieLocaleResolver cookieLocaleResolver) {
            cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        }
    }

    @Test
    void testLocalizationCoverage() {
        for (String key : messageKeys) {
            for (Locale locale : locales) {
                String message = messageSource.getMessage(key, null, locale);
                assertNotNull(message, "Message for key '" + key + "' should not be null for locale: " + locale);
            }
        }
    }
}

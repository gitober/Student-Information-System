package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.TeacherAttendanceView.TeacherAttendanceView;
import com.studentinfo.views.TeacherUpdateStudentProfileView.TeacherUpdateStudentProfileView;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.homeprofilepage.ProfilePageView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CssImport("./themes/studentinformationapp/views/header-view.css")
public class HeaderView extends HorizontalLayout {

    private final MessageSource messageSource;

    // Constructor for logged-in users (full header with links)
    public HeaderView(AuthenticatedUser authenticatedUser, MessageSource messageSource) {
        this.messageSource = messageSource;
        this.setWidthFull();
        this.setHeight("60px");
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.addClassName("header");

        // Logo and title
        Image logo = new Image("images/bird.png", getTranslation("app.logo"));
        logo.addClassName("logo");

        String appTitle = getTranslation("header.title");
        Span appName = new Span(appTitle);
        appName.addClassName("app-name");

        HorizontalLayout logoAndTitle = new HorizontalLayout(logo, appName);
        logoAndTitle.setAlignItems(Alignment.CENTER);
        logoAndTitle.setSpacing(false);
        logoAndTitle.addClassName("logo-title-container");

        // Add logo and title to the header
        this.add(logoAndTitle);

        // Navigation links for logged-in users
        authenticatedUser.get().ifPresent(user -> {
            RouterLink homeLink = new RouterLink(getTranslation("header.home"), ProfilePageView.class);
            homeLink.addClassName("router-link");

            RouterLink coursesLink = new RouterLink(getTranslation("header.courses"), CoursesView.class);
            coursesLink.addClassName("router-link");

            RouterLink gradesLink = new RouterLink(getTranslation("header.grades"), GradesView.class);
            gradesLink.addClassName("router-link");

            RouterLink attendanceTrackingLink = null;
            RouterLink updateStudentProfilesLink = null;

            if (user instanceof com.studentinfo.data.entity.Teacher) {
                attendanceTrackingLink = new RouterLink(getTranslation("header.attendance"), TeacherAttendanceView.class);
                attendanceTrackingLink.addClassName("router-link");

                updateStudentProfilesLink = new RouterLink(getTranslation("header.students"), TeacherUpdateStudentProfileView.class);
                updateStudentProfilesLink.addClassName("router-link");
            }

            RouterLink editProfileLink = new RouterLink(getTranslation("header.profile"), EditProfileView.class);
            editProfileLink.addClassName("router-link");

            // Create a layout for the language dropdown and logout button
            HorizontalLayout userActionsLayout = new HorizontalLayout();
            userActionsLayout.setAlignItems(Alignment.CENTER);

            // Language selector
            ComboBox<LanguageOption> languageDropdown = createLanguageDropdown();
            languageDropdown.addClassName("language-dropdown");

            Button logoutButton = new Button(getTranslation("header.logout"), click -> {
                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> ui.getPage().setLocation("/logout"));
                }
            });
            logoutButton.setId("logout-button");
            logoutButton.addClassName("logout-button");

            // Add components to the user actions layout (language dropdown + logout button)
            userActionsLayout.add(languageDropdown, logoutButton);

            // Add navigation links and user actions to the header
            this.add(homeLink, coursesLink, gradesLink);
            if (attendanceTrackingLink != null) {
                this.add(attendanceTrackingLink);
            }
            if (updateStudentProfilesLink != null) {
                this.add(updateStudentProfilesLink);
            }
            this.add(editProfileLink, userActionsLayout);
        });
    }

    // Constructor for public pages (minimal header with language dropdown)
    public HeaderView(MessageSource messageSource) {
        this.messageSource = messageSource;
        this.setWidthFull();
        this.setHeight("60px");
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.addClassName("header");

        // Logo and title
        Image logo = new Image("images/bird.png", getTranslation("app.logo"));
        logo.addClassName("logo");

        String appTitle = getTranslation("header.title");
        Span appName = new Span(appTitle);
        appName.addClassName("app-name");

        HorizontalLayout logoAndTitle = new HorizontalLayout(logo, appName);
        logoAndTitle.setAlignItems(Alignment.CENTER);
        logoAndTitle.setSpacing(false);
        logoAndTitle.addClassName("logo-title-container");

        // Language dropdown for non-logged-in users
        ComboBox<LanguageOption> languageDropdown = createLanguageDropdown();
        languageDropdown.addClassName("language-dropdown");

        // Add logo, title, and language dropdown to the header
        this.add(logoAndTitle, languageDropdown);
    }

    private ComboBox<LanguageOption> createLanguageDropdown() {
        List<LanguageOption> languageOptions = new ArrayList<>();
        languageOptions.add(new LanguageOption("English", "images/flags/uk.png", Locale.ENGLISH));
        languageOptions.add(new LanguageOption("Suomi", "images/flags/fi.png", Locale.forLanguageTag("fi")));
        languageOptions.add(new LanguageOption("中文", "images/flags/ch.png", Locale.forLanguageTag("ch")));
        languageOptions.add(new LanguageOption("Русский", "images/flags/ru.png", Locale.forLanguageTag("ru")));

        ComboBox<LanguageOption> languageDropdown = new ComboBox<>();
        languageDropdown.setItems(languageOptions);

        // Customize the dropdown to show flag and language name
        languageDropdown.setRenderer(new ComponentRenderer<>(option -> {
            HorizontalLayout layout = new HorizontalLayout(new Image(option.getFlagPath(), option.getLanguage()), new Span(option.getLanguage()));
            layout.setSpacing(true);
            return layout;
        }));

        // Set default value based on the current locale
        Locale currentLocale = UI.getCurrent().getLocale();
        languageOptions.stream()
                .filter(option -> option.getLocale().equals(currentLocale))
                .findFirst()
                .ifPresentOrElse(languageDropdown::setValue, () -> languageDropdown.setValue(languageOptions.get(0)));

        // Add listener to handle language changes
        languageDropdown.addValueChangeListener(event -> {
            Locale selectedLocale = event.getValue().getLocale();
            UI ui = UI.getCurrent();
            if (ui != null) {
                ui.access(() -> {
                    ui.getSession().setLocale(selectedLocale);
                    LocaleContextHolder.setLocale(selectedLocale); // Sync with Spring's context

                    // Persist the selected locale in the cookie
                    ui.getPage().executeJs("document.cookie = 'user-lang=' + $0 + '; path=/; max-age=31536000';",
                            selectedLocale.getLanguage());

                    ui.getPage().reload(); // Reload to apply the changes consistently
                });
            }
        });

        return languageDropdown;
    }

    // Inner class to represent language options
    @Getter
    private static class LanguageOption {
        private final String language;
        private final String flagPath;
        private final Locale locale;

        public LanguageOption(String language, String flagPath, Locale locale) {
            this.language = language;
            this.flagPath = flagPath;
            this.locale = locale;
        }

        @Override
        public String toString() {
            return language;
        }
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

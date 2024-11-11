package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.teacher_attendance_view.TeacherAttendanceView;
import com.studentinfo.views.teacher_update_student_profile.TeacherUpdateStudentProfileView;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.homeprofilepage.ProfilePageView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@CssImport("./themes/studentinformationapp/views/header-view.css")
public class HeaderView extends HorizontalLayout {

    private final transient MessageSource messageSource;
    private static final String ROUTER_LINK_CLASS = "router-link";

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
            homeLink.addClassName(ROUTER_LINK_CLASS);

            RouterLink coursesLink = new RouterLink(getTranslation("header.courses"), CoursesView.class);
            coursesLink.addClassName(ROUTER_LINK_CLASS);

            RouterLink gradesLink = new RouterLink(getTranslation("header.grades"), GradesView.class);
            gradesLink.addClassName(ROUTER_LINK_CLASS);

            RouterLink attendanceTrackingLink = null;
            RouterLink updateStudentProfilesLink = null;

            if (user instanceof com.studentinfo.data.entity.Teacher) {
                attendanceTrackingLink = new RouterLink(getTranslation("header.attendance"), TeacherAttendanceView.class);
                attendanceTrackingLink.addClassName(ROUTER_LINK_CLASS);

                updateStudentProfilesLink = new RouterLink(getTranslation("header.students"), TeacherUpdateStudentProfileView.class);
                updateStudentProfilesLink.addClassName(ROUTER_LINK_CLASS);
            }

            RouterLink editProfileLink = new RouterLink(getTranslation("header.profile"), EditProfileView.class);
            editProfileLink.addClassName(ROUTER_LINK_CLASS);

            // Create a layout for the language flags and logout button
            HorizontalLayout userActionsLayout = new HorizontalLayout();
            userActionsLayout.setAlignItems(Alignment.CENTER);

            // Language flags layout
            HorizontalLayout languageFlags = createLanguageFlags();

            Button logoutButton = new Button(getTranslation("header.logout"), click -> {
                UI ui = UI.getCurrent();
                if (ui != null) {
                    ui.access(() -> ui.getPage().setLocation("/logout"));
                }
            });
            logoutButton.setId("logout-button");
            logoutButton.addClassName("logout-button");

            // Add components to the user actions layout (language flags + logout button)
            userActionsLayout.add(languageFlags, logoutButton);

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

    // Constructor for public pages (minimal header with language flags)
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

        // Language flags for non-logged-in users
        HorizontalLayout languageFlags = createLanguageFlags();

        // Add logo, title, and language flags to the header
        this.add(logoAndTitle, languageFlags);
    }

    private HorizontalLayout createLanguageFlags() {
        HorizontalLayout flagLayout = new HorizontalLayout();
        flagLayout.addClassName("language-flag-layout");

        // Create flag images for each language
        Image englishFlag = createFlagImage("images/flags/uk.png", Locale.ENGLISH);
        Image finnishFlag = createFlagImage("images/flags/fi.png", Locale.forLanguageTag("fi"));
        Image chineseFlag = createFlagImage("images/flags/ch.png", Locale.forLanguageTag("ch"));
        Image russianFlag = createFlagImage("images/flags/ru.png", Locale.forLanguageTag("ru"));

        // Add flags to the layout
        flagLayout.add(englishFlag, finnishFlag, chineseFlag, russianFlag);
        return flagLayout;
    }

    private Image createFlagImage(String imagePath, Locale locale) {
        Image flagImage = new Image(imagePath, locale.getDisplayLanguage());
        flagImage.addClassName("flag-image"); // Apply CSS class for styling
        flagImage.getStyle().set("cursor", "pointer"); // Make it look clickable

        // Set a click listener to change the language
        flagImage.addClickListener(event -> {
            UI ui = UI.getCurrent();
            if (ui != null) {
                ui.access(() -> {
                    ui.getSession().setLocale(locale);
                    LocaleContextHolder.setLocale(locale); // Sync with Spring's context

                    // Check if the locale is Chinese and change the logout button color
                    if (locale.getLanguage().equals("ch")) {
                        ui.getPage().executeJs("document.getElementById('logout-button').classList.add('logout-button-red');");
                    } else {
                        ui.getPage().executeJs("document.getElementById('logout-button').classList.remove('logout-button-red');");
                    }

                    // Persist the selected locale in the cookie
                    ui.getPage().executeJs("document.cookie = 'user-lang=' + $0 + '; path=/; max-age=31536000';",
                            locale.getLanguage());

                    ui.getPage().reload(); // Reload to apply the changes consistently
                });
            }
        });
        return flagImage;
    }

    // Helper method to get translations
    private String getTranslation(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.homeprofilepage.ProfilePageView;
import com.studentinfo.views.TeacherUpdateStudentProfileView.TeacherUpdateStudentProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport;

@CssImport("./themes/studentinformationapp/views/header-view.css")
public class HeaderView extends HorizontalLayout {

    public HeaderView(String title, AuthenticatedUser authenticatedUser) {
        this(title);

        authenticatedUser.get().ifPresent(user -> {
            // Navigation links in the correct order
            RouterLink homeLink = new RouterLink("Home", ProfilePageView.class);
            homeLink.addClassName("router-link");
            homeLink.addClassName("home-link");

            RouterLink coursesLink = new RouterLink("Courses", CoursesView.class);
            coursesLink.addClassName("router-link");

            RouterLink gradesLink = new RouterLink("Grades", GradesView.class);
            gradesLink.addClassName("router-link");

            RouterLink editProfileLink = new RouterLink("Edit Profile", EditProfileView.class);
            editProfileLink.addClassName("router-link");

            // Teacher-specific link for updating student profiles
            RouterLink updateStudentProfilesLink = null;
            if (user instanceof com.studentinfo.data.entity.Teacher) {
                updateStudentProfilesLink = new RouterLink("Student Management", TeacherUpdateStudentProfileView.class);
                updateStudentProfilesLink.addClassName("router-link");
            }

            // Logout button - directs to Spring Security's logout endpoint
            Button logoutButton = new Button("Logout", click -> {
                UI.getCurrent().getPage().setLocation("/logout"); // Redirects to the Spring Security logout endpoint
            });
            logoutButton.addClassName("logout-button");

            // Add all components in the desired order
            this.add(homeLink, coursesLink, gradesLink);
            if (updateStudentProfilesLink != null) {
                this.add(updateStudentProfilesLink);
            }
            this.add(editProfileLink, logoutButton);
        });
    }

    public HeaderView(String title) {
        this.setWidthFull();
        this.setHeight("60px"); // Set a fixed height directly
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.addClassName("header");

        // Application logo
        Image logo = new Image("images/bird.png", "EduBird Logo");
        logo.addClassName("logo");

        // Application title
        Span appName = new Span(title);
        appName.addClassName("app-name");

        // Container for logo and title
        HorizontalLayout logoAndTitle = new HorizontalLayout(logo, appName);
        logoAndTitle.setAlignItems(Alignment.CENTER);
        logoAndTitle.setSpacing(false);
        logoAndTitle.addClassName("logo-title-container");

        // Add the logo and the app name container to the header
        this.add(logoAndTitle);
    }
}

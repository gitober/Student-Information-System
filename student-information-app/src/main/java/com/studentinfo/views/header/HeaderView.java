package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.profilepage.ProfilePageView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.dependency.CssImport; // Import CssImport annotation

@CssImport("./themes/studentinformationapp/views/header-view.css")  // Link to your CSS file
public class HeaderView extends HorizontalLayout {

    // Constructor for authenticated pages
    public HeaderView(String title, AuthenticatedUser authenticatedUser) {
        this(title); // Call the simplified constructor first

        // Check if the user is authenticated
        authenticatedUser.get().ifPresent(user -> {
            // Navigation links
            RouterLink homeLink = new RouterLink("Home", ProfilePageView.class);
            homeLink.addClassName("router-link");

            RouterLink coursesLink = new RouterLink("Courses", CoursesView.class);
            coursesLink.addClassName("router-link");

            RouterLink gradesLink = new RouterLink("Grades", GradesView.class);
            gradesLink.addClassName("router-link");

            RouterLink editProfileLink = new RouterLink("Edit Profile", EditProfileView.class);
            editProfileLink.addClassName("router-link");

            // Logout button
            Button logoutButton = new Button("Logout", click -> {
                authenticatedUser.logout();
                UI.getCurrent().navigate("login");
            });
            logoutButton.addClassName("logout-button");

            // Add the links and logout button to the header
            this.add(homeLink, coursesLink, gradesLink, editProfileLink, logoutButton);
        });
    }

    // Simplified constructor for pages without authentication (e.g., Login)
    public HeaderView(String title) {
        this.setWidthFull();
        this.setHeight("var(--header-height, 60px)");
        this.setAlignItems(Alignment.CENTER);
        this.setJustifyContentMode(JustifyContentMode.BETWEEN);
        this.addClassName("header");

        // Application title
        Span appName = new Span(title);
        appName.addClassName("app-name");

        // Add the app name to the header
        this.add(appName);
    }
}

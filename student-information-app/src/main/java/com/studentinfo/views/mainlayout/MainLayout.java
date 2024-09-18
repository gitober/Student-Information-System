package com.studentinfo.views.mainlayout;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.profilepage.ProfilePageView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@RouteScope
public class MainLayout extends Composite<Div> implements RouterLayout {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public MainLayout(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        // Header
        Div header = new Div();
        header.addClassName("header");
        Span appName = new Span("EduBird");
        appName.addClassName("app-name");

        HorizontalLayout headerContent = new HorizontalLayout();
        headerContent.setWidthFull();
        headerContent.setAlignItems(FlexComponent.Alignment.CENTER);
        headerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        // Check if the user is authenticated and add the buttons dynamically
        authenticatedUser.get().ifPresent(user -> {
            // Navigation links
            RouterLink homeLink = new RouterLink("Home", ProfilePageView.class);
            RouterLink coursesLink = new RouterLink("Courses", CoursesView.class);
            RouterLink gradesLink = new RouterLink("Grades", GradesView.class);
            RouterLink editProfileLink = new RouterLink("Edit Profile", EditProfileView.class);

            // Logout button
            Button logoutButton = new Button("Logout", click -> {
                authenticatedUser.logout();
                UI.getCurrent().navigate("login");
            });

            // Add elements to the header
            headerContent.add(appName, homeLink, coursesLink, gradesLink, editProfileLink, logoutButton);
        });

        header.add(headerContent);
        // Directly add the header to the main content
        getContent().add(header);
    }
}

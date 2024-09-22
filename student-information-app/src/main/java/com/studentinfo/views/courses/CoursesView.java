package com.studentinfo.views.courses;

import com.studentinfo.views.header.HeaderView;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Courses")
@Route(value = "courses")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/courses-view/courses-view.css")
public class CoursesView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;
    private final UserContentLoader userContentLoader;

    @Autowired
    public CoursesView(AuthenticatedUser authenticatedUser, UserContentLoader userContentLoader) {
        this.authenticatedUser = authenticatedUser;
        this.userContentLoader = userContentLoader;

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        // Add padding to avoid content sticking to or going under the header
        mainLayout.getStyle().set("padding-top", "60px");

        // Add the reusable Header component
        mainLayout.add(new HeaderView("EduBird", authenticatedUser));

        // Create a container for user-specific content
        VerticalLayout layoutColumn = new VerticalLayout();

        // Load user-specific content for courses
        userContentLoader.loadCoursesContent(layoutColumn);

        // Add the content layout to the main layout
        mainLayout.add(layoutColumn);
    }
}

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
import org.springframework.context.MessageSource;

@PageTitle("Courses")
@Route(value = "courses")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/courses-view/courses-view.css")
public class CoursesView extends Composite<VerticalLayout> {

    private final UserContentLoader userContentLoader;

    @Autowired
    public CoursesView(AuthenticatedUser authenticatedUser, UserContentLoader userContentLoader, MessageSource messageSource) {
        this.userContentLoader = userContentLoader;

        // Debug statements to ensure proper injection
        if (authenticatedUser == null) {
            throw new IllegalStateException("AuthenticatedUser is not injected!");
        }
        if (userContentLoader == null) {
            throw new IllegalStateException("UserContentLoader is not injected!");
        }
        if (messageSource == null) {
            throw new IllegalStateException("MessageSource is not injected!");
        }

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        // Ensure the main layout occupies full height
        mainLayout.setSizeFull();

        // Add padding to avoid content sticking to or going under the header
        mainLayout.getStyle().set("padding-top", "60px");

        // Add the reusable Header component
        mainLayout.add(new HeaderView(authenticatedUser, messageSource));

        // Create a container for user-specific content
        VerticalLayout layoutColumn = new VerticalLayout();
        layoutColumn.setSizeFull(); // Ensure it fills the available space

        // Load user-specific content for courses
        userContentLoader.loadCoursesContent(layoutColumn);

        // Add the content layout to the main layout
        mainLayout.add(layoutColumn);

        // Set layoutColumn to expand to fill available space
        mainLayout.setFlexGrow(1, layoutColumn);
    }
}

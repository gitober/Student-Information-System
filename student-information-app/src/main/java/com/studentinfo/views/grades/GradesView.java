package com.studentinfo.views.grades;

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

@PageTitle("Grades")
@Route(value = "grades")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/grades-view/grades-view.css")
public class GradesView extends Composite<VerticalLayout> {

    @Autowired
    public GradesView(AuthenticatedUser authenticatedUser, UserContentLoader userContentLoader, MessageSource messageSource) {

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
        mainLayout.setPadding(false); // Disable default padding
        mainLayout.setSpacing(false); // Disable spacing between elements
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Center the content horizontally
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START); // Align content to start vertically

        // Ensure the main layout occupies full height
        mainLayout.setSizeFull();

        // Add padding to avoid content sticking to or going under the header
        mainLayout.getStyle().set("padding-top", "60px"); // Adjust based on header height

        // Add the reusable Header component
        mainLayout.add(new HeaderView(authenticatedUser, messageSource));

        // Create a container for user-specific content
        VerticalLayout layoutColumn = new VerticalLayout();
        layoutColumn.setSizeFull(); // Ensure it fills the available space

        // Load user-specific content for grades
        userContentLoader.loadGradesContent(layoutColumn);

        // Add the content layout to the main layout
        mainLayout.add(layoutColumn);

        // Set layoutColumn to expand to fill available space
        mainLayout.setFlexGrow(1, layoutColumn);
    }
}

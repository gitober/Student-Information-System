package com.studentinfo.views.homeprofilepage;

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

@Route(value = "profile")
@PageTitle("Profile Page")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/profile-page-view/profile-page-view.css")
public class ProfilePageView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;
    private final UserContentLoader userContentLoader;

    @Autowired
    public ProfilePageView(AuthenticatedUser authenticatedUser, UserContentLoader userContentLoader) {
        this.authenticatedUser = authenticatedUser;
        this.userContentLoader = userContentLoader;

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setPadding(false); // Disable default padding if set by Vaadin
        mainLayout.setSpacing(false); // Disable spacing between elements
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER); // Center the content horizontally
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START); // Align content to start vertically

        // Add padding to avoid content sticking to or going under the header
        mainLayout.getStyle().set("padding-top", "60px"); // Adjust based on header height

        // Add the reusable Header component
        mainLayout.add(new HeaderView("EduBird", authenticatedUser));

        // Create a container for user-specific content
        VerticalLayout layoutColumn = new VerticalLayout();

        // Load user-specific content using UserContentLoader
        userContentLoader.loadContent(layoutColumn);

        // Add the content layout to the main layout
        mainLayout.add(layoutColumn);
    }
}

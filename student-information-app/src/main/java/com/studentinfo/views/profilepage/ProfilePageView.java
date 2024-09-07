package com.studentinfo.views.profilepage;

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
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Add the reusable Header component
        mainLayout.add(new HeaderView("Profile Page", authenticatedUser));

        // Create a container for user-specific content
        VerticalLayout layoutColumn = new VerticalLayout();
        layoutColumn.setWidthFull();
        layoutColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        // Load user-specific content using UserContentLoader
        userContentLoader.loadContent(layoutColumn);

        // Add the content layout to the main layout
        mainLayout.add(layoutColumn);
    }
}

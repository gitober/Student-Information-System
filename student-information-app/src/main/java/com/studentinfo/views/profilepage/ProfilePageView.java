package com.studentinfo.views.profilepage;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "profile", layout = MainLayout.class) // Ensure MainLayout is used here
@PageTitle("Profile Page")
@RolesAllowed("USER")
public class ProfilePageView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;
    private final com.studentinfo.views.profilepage.UserContentLoader userContentLoader;

    @Autowired
    public ProfilePageView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        this.userContentLoader = new com.studentinfo.views.profilepage.UserContentLoader(authenticatedUser);

        VerticalLayout mainLayout = getContent();
        mainLayout.setWidthFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        H1 header = new H1("Profile Page");
        mainLayout.add(header);

        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn = new VerticalLayout();
        HorizontalLayout layoutFooter = new HorizontalLayout();

        layoutRow.setWidthFull();
        layoutRow.setAlignItems(FlexComponent.Alignment.START);
        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        layoutColumn.setWidth("100%");
        layoutColumn.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        layoutFooter.setWidthFull();
        layoutFooter.setAlignItems(FlexComponent.Alignment.END);
        layoutFooter.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        mainLayout.add(layoutRow, layoutColumn, layoutFooter);

        // Load user-specific content
        userContentLoader.loadContent(layoutColumn);
    }
}

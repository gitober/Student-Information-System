package com.studentinfo.views.grades;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.mainlayout.MainLayout;
import com.studentinfo.views.profilepage.ProfilePageView;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Grades")
@Route(value = "grades", layout = MainLayout.class)
@AnonymousAllowed
public class GradesView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public GradesView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        VerticalLayout mainLayout = getContent();
        mainLayout.setWidthFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Header layout with navigation links and logout button
        HorizontalLayout headerContent = new HorizontalLayout();
        headerContent.setWidthFull();
        headerContent.setAlignItems(FlexComponent.Alignment.CENTER);
        headerContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        RouterLink homeLink = new RouterLink("Profile - Home", ProfilePageView.class);
        RouterLink coursesLink = new RouterLink("Courses", CoursesView.class);
        RouterLink gradesLink = new RouterLink("Grades", GradesView.class);
        RouterLink editProfileLink = new RouterLink("Edit Profile", EditProfileView.class);

        // Logout button
        Button logoutButton = new Button("Logout", click -> {
            authenticatedUser.logout();
            UI.getCurrent().navigate("login");
        });

        headerContent.add(homeLink, coursesLink, gradesLink, editProfileLink, logoutButton);
        mainLayout.add(headerContent);

        // Main content setup
        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");

        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");

        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        getContent().add(layoutRow2);
    }
}

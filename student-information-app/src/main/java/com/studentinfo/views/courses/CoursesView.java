package com.studentinfo.views.courses;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.mainlayout.MainLayout;
import com.studentinfo.views.profilepage.ProfilePageView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Courses")
@Route(value = "courses", layout = MainLayout.class)
@AnonymousAllowed
public class CoursesView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public CoursesView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        HorizontalLayout headerContent = new HorizontalLayout();
        authenticatedUser.get().ifPresent(user -> {
            RouterLink homeLink = new RouterLink("Profile - Home", ProfilePageView.class);
            RouterLink coursesLink = new RouterLink("Courses", CoursesView.class);
            RouterLink gradesLink = new RouterLink("Grades", GradesView.class);
            RouterLink editProfileLink = new RouterLink("Edit Profile", EditProfileView.class);

            Button logoutButton = new Button("Logout", click -> {
                authenticatedUser.logout();
                UI.getCurrent().navigate("login");
            });

            headerContent.add(homeLink, coursesLink, gradesLink, editProfileLink, logoutButton);
        });

        H1 header = new H1("Courses Page");
        getContent().add(headerContent, header);
    }
}

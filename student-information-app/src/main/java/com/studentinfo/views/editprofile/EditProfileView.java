package com.studentinfo.views.editprofile;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.views.mainlayout.MainLayout;
import com.studentinfo.views.profilepage.ProfilePageView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Edit Profile")
@Route(value = "editprofile", layout = MainLayout.class)
@AnonymousAllowed
public class EditProfileView extends Composite<VerticalLayout> {

    private final AuthenticatedUser authenticatedUser;

    @Autowired
    public EditProfileView(AuthenticatedUser authenticatedUser) {
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

        H3 header = new H3("Edit Profile");
        getContent().add(headerContent, header);
    }
}

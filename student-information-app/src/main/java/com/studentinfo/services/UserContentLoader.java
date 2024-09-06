// UserContentLoader.java
package com.studentinfo.views.profilepage;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserContentLoader {

    private final AuthenticatedUser authenticatedUser;

    public UserContentLoader(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void loadContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            layout.add(new H1("Welcome, " + user.getFirstName() + "!"));

            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherDashboardView());
            } else if (user instanceof Student) {
                layout.add(new StudentDashboardView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.StudentCoursesView;
import com.studentinfo.views.courses.TeacherCoursesView;
import com.studentinfo.views.profilepage.StudentDashboardView;
import com.studentinfo.views.profilepage.TeacherDashboardView;
import com.studentinfo.views.grades.StudentGradesView;
import com.studentinfo.views.grades.TeacherGradesView;
import com.studentinfo.views.editprofile.StudentEditProfileView;
import com.studentinfo.views.editprofile.TeacherEditProfileView;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.stereotype.Component;

@Component // This annotation tells Spring to manage this class as a bean
public class UserContentLoader {

    private final AuthenticatedUser authenticatedUser;

    public UserContentLoader(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    // Method to load profile content
    public void loadContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
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

    // Method to load courses content
    public void loadCoursesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherCoursesView());
            } else if (user instanceof Student) {
                layout.add(new StudentCoursesView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }

    // Method to load grades content
    public void loadGradesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherGradesView());
            } else if (user instanceof Student) {
                layout.add(new StudentGradesView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }

    // Method to load edit profile content
    public void loadEditProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherEditProfileView());
            } else if (user instanceof Student) {
                layout.add(new StudentEditProfileView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }
}

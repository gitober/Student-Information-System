package com.studentinfo.views.profilepage;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/profile-page-view/student-profile-page-view.css")
public class StudentDashboardView extends VerticalLayout {

    public StudentDashboardView() {
        addClassName("student-profile-page-view"); // Apply the CSS class

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Student Dashboard"));
        add(new Paragraph("View your enrolled courses, grades, and attendance details here."));
    }
}

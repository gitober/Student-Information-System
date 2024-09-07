package com.studentinfo.views.profilepage;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/profile-page-view/teacher-profile-page-view.css")
public class TeacherDashboardView extends VerticalLayout {

    public TeacherDashboardView() {
        addClassName("teacher-profile-page-view"); // Apply the CSS class

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Teacher Dashboard"));
        add(new Paragraph("Manage courses, view student performance, HELLLOOOOOOO WOOOOOW TEST2."));
    }
}

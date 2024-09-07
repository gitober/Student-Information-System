package com.studentinfo.views.courses;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/courses-view/teacher-courses-view.css")
public class TeacherCoursesView extends VerticalLayout {

    public TeacherCoursesView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Teacher Courses View"));
        add(new Paragraph("Manage courses, assignments, and student enrollment here."));
        // Add more components as needed for teacher functionalities
    }
}

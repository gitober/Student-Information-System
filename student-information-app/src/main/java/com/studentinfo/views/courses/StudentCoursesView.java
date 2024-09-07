package com.studentinfo.views.courses;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/courses-view/student-courses-view.css")
public class StudentCoursesView extends VerticalLayout {

    public StudentCoursesView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Student Courses View"));
        add(new Paragraph("View your enrolled courses and class schedule here."));
        // Add more components as needed for student functionalities
    }
}

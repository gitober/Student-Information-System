package com.studentinfo.views.grades;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/grades-view/teacher-grades-view.css")
public class TeacherGradesView extends VerticalLayout {

    public TeacherGradesView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Teacher Grades View"));
        add(new Paragraph("Manage student grades and performance here."));
        // Add more components as needed for teacher functionalities
    }
}

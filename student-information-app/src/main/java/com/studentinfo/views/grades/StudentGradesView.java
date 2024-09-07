package com.studentinfo.views.grades;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/grades-view/student-grades-view.css")
public class StudentGradesView extends VerticalLayout {

    public StudentGradesView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Student Grades View"));
        add(new Paragraph("View your grades and performance details here."));
        // Add more components as needed for student functionalities
    }
}

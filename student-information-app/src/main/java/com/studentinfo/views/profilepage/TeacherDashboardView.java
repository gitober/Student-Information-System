// TeacherDashboardView.java
package com.studentinfo.views.profilepage;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TeacherDashboardView extends VerticalLayout {

    public TeacherDashboardView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Teacher Dashboard"));
        add(new Paragraph("Manage courses, view student performance, and track attendance here."));
    }
}

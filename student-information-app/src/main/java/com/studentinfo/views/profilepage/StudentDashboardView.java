// StudentDashboardView.java
package com.studentinfo.views.profilepage;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class StudentDashboardView extends VerticalLayout {

    public StudentDashboardView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H1("Student Dashboard"));
        add(new Paragraph("View your enrolled courses, grades, and attendance details here."));
    }
}

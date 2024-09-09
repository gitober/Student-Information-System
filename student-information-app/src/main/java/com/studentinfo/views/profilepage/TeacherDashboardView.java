package com.studentinfo.views.profilepage;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.html.Image;

@CssImport("./themes/studentinformationapp/views/profile-page-view/teacher-profile-page-view.css")
public class TeacherDashboardView extends VerticalLayout {

    public TeacherDashboardView() {
        addClassName("teacher-profile-page-view"); // Apply the CSS class

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER); // Center content

        // Add welcome text
        H1 welcomeText = new H1("Welcome");
        welcomeText.addClassName("welcome-text"); // Add a class for custom styles
        add(welcomeText);

        // Add smaller description text
        Paragraph descriptionText = new Paragraph("Manage courses, view student performance, and much more.");
        descriptionText.addClassName("description-text");
        add(descriptionText);

        // Add resized bird image
        Image mainImage = new Image("images/bird.png", "testii");
        mainImage.addClassName("main-image");
        add(mainImage);
    }
}

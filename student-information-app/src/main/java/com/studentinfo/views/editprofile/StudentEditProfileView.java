package com.studentinfo.views.editprofile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
public class StudentEditProfileView extends VerticalLayout {

    public StudentEditProfileView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H2("Edit Student Profile"));

        // Example fields, customize as needed
        TextField nameField = new TextField("Name");
        TextField emailField = new TextField("Email");

        // Add a simple "Save" button
        Button saveButton = new Button("Save", event -> {
            Notification.show("Changes saved!", 3000, Notification.Position.TOP_CENTER);
        });

        add(nameField, emailField, saveButton);
    }
}

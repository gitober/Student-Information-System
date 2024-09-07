package com.studentinfo.views.editprofile;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/teacher-edit-profile-view.css")
public class TeacherEditProfileView extends VerticalLayout {

    public TeacherEditProfileView() {
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H2("Edit Teacher Profile"));
        // Example fields, customize as needed
        TextField nameField = new TextField("Name");
        TextField departmentField = new TextField("Department");

        add(nameField, departmentField);
    }
}

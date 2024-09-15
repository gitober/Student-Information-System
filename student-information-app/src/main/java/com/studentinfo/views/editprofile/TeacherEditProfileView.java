package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.DepartmentService;
import com.studentinfo.services.SubjectService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;
import java.util.function.Consumer;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/teacher-edit-profile-view.css")
public class TeacherEditProfileView extends VerticalLayout {

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField phoneNumberField;
    private final TextField emailField;
    private final ComboBox<Department> departmentComboBox;
    private final ComboBox<Subject> subjectComboBox;
    private final Button saveButton;
    private final Teacher teacher;

    // Paragraphs to display current details
    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;
    private final Paragraph departmentParagraph;
    private final Paragraph subjectParagraph;

    // Constructor accepting a Teacher object and services for loading data
    public TeacherEditProfileView(Teacher teacher, DepartmentService departmentService, SubjectService subjectService) {
        this.teacher = teacher;

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        add(new H2("Edit Teacher Profile"));

        // Display current details above the fields
        add(new Paragraph("Current Details:"));
        nameParagraph = new Paragraph("Name: " + teacher.getFirstName() + " " + teacher.getLastName());
        emailParagraph = new Paragraph("Email: " + teacher.getEmail());
        phoneNumberParagraph = new Paragraph("Phone Number: " + teacher.getPhoneNumber());
        departmentParagraph = new Paragraph("Department: " + (teacher.getDepartment() != null ? teacher.getDepartment().getName() : "N/A"));
        subjectParagraph = new Paragraph("Subject: " + (teacher.getSubject() != null ? teacher.getSubject().getName() : "N/A"));

        // Add current detail paragraphs
        add(nameParagraph, emailParagraph, phoneNumberParagraph, departmentParagraph, subjectParagraph);

        // Initialize form fields as empty
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");

        // Populate department and subject dropdowns
        departmentComboBox = new ComboBox<>("Department");
        departmentComboBox.setItems(departmentService.findAll());
        departmentComboBox.setItemLabelGenerator(Department::getName);
        departmentComboBox.setValue(teacher.getDepartment());

        subjectComboBox = new ComboBox<>("Subject");
        subjectComboBox.setItems(subjectService.findAll());
        subjectComboBox.setItemLabelGenerator(Subject::getName);
        subjectComboBox.setValue(teacher.getSubject());

        saveButton = new Button("Save");

        // Add fields to the layout
        add(firstNameField, lastNameField, phoneNumberField, emailField, departmentComboBox, subjectComboBox, saveButton);
    }

    // Method to update teacher profile values
    private void updateTeacherProfile() {
        if (!firstNameField.isEmpty()) teacher.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) teacher.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) teacher.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) teacher.setEmail(emailField.getValue());
        teacher.setDepartment(departmentComboBox.getValue());
        teacher.setSubject(subjectComboBox.getValue());

        // Update displayed paragraphs with new values
        nameParagraph.setText("Name: " + teacher.getFirstName() + " " + teacher.getLastName());
        emailParagraph.setText("Email: " + teacher.getEmail());
        phoneNumberParagraph.setText("Phone Number: " + teacher.getPhoneNumber());
        departmentParagraph.setText("Department: " + (teacher.getDepartment() != null ? teacher.getDepartment().getName() : "N/A"));
        subjectParagraph.setText("Subject: " + (teacher.getSubject() != null ? teacher.getSubject().getName() : "N/A"));
    }

    // Listener to trigger the actual save operation outside of this class
    public void setSaveListener(Consumer<Teacher> saveListener) {
        saveButton.addClickListener(event -> {
            updateTeacherProfile();
            saveListener.accept(teacher);
            Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);
        });
    }
}

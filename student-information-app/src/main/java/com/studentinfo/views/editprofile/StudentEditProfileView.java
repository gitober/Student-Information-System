package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import java.util.function.Consumer;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
public class StudentEditProfileView extends VerticalLayout {

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField phoneNumberField;
    private final TextField emailField;
    private final Button saveButton;
    private final Student student; // Store the student object

    // Paragraphs to display current details
    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;
    private final Paragraph gradeParagraph;
    private final Paragraph studentClassParagraph;

    // Constructor accepting a Student object
    public StudentEditProfileView(Student student) {
        this.student = student; // Initialize the student

        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        // Display current details above the fields
        add(new Paragraph("Current Details:"));
        nameParagraph = new Paragraph("Name: " + student.getFirstName() + " " + student.getLastName());
        emailParagraph = new Paragraph("Email: " + student.getEmail());
        phoneNumberParagraph = new Paragraph("Phone Number: " + student.getPhoneNumber());
        gradeParagraph = new Paragraph("Grade: " + student.getGrade());  // Display only
        studentClassParagraph = new Paragraph("Student Class: " + student.getStudentClass());  // Display only

        // Add current detail paragraphs
        add(nameParagraph, emailParagraph, phoneNumberParagraph, gradeParagraph, studentClassParagraph);

        // Initialize form fields as empty
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");

        saveButton = new Button("Save");

        // Add fields to the layout
        add(firstNameField, lastNameField, phoneNumberField, emailField, saveButton);
    }

    // Method to update student profile values
    private void updateStudentProfile() {
        // Only update fields if they have new values
        if (!firstNameField.isEmpty()) student.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) student.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) student.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) student.setEmail(emailField.getValue());

        // Update displayed paragraphs with new values
        nameParagraph.setText("Name: " + student.getFirstName() + " " + student.getLastName());
        emailParagraph.setText("Email: " + student.getEmail());
        phoneNumberParagraph.setText("Phone Number: " + student.getPhoneNumber());
    }

    // Listener to trigger the actual save operation outside of this class
    public void setSaveListener(Consumer<Student> saveListener) {
        saveButton.addClickListener(event -> {
            updateStudentProfile();
            saveListener.accept(student);
            Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);
        });
    }
}

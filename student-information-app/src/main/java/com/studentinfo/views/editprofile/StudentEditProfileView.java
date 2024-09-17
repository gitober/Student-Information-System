package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
public class StudentEditProfileView extends VerticalLayout {

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField phoneNumberField;
    private final TextField emailField;
    private final Button saveButton;
    private final Student student;

    // Paragraphs to display current details
    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;
    private final Paragraph gradeParagraph;
    private final Paragraph studentClassParagraph;

    // Constructor accepting a Student object
    public StudentEditProfileView(Student student) {
        this.student = student;

        setSizeFull(); // Makes the layout take the full size of the parent
        setPadding(false); // Removes any padding
        setMargin(false); // Removes any margin
        setSpacing(false); // Removes any spacing between child components
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("student-edit-profile-container");

        // Form Title
        H3 h3 = new H3("Edit Your Profile");
        h3.addClassName("student-edit-profile-title");

        // Wrapper layout for the title and main content
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setSpacing(false); // Remove extra spacing between title and main content
        wrapperLayout.setPadding(false); // Remove padding
        wrapperLayout.setMargin(false); // Remove margin
        wrapperLayout.setAlignItems(Alignment.CENTER); // Center align content
        wrapperLayout.setWidthFull(); // Use full width

        // Current details section
        VerticalLayout currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("student-current-details-layout");

        currentDetailsLayout.add(new Paragraph("Current Details:"));
        nameParagraph = new Paragraph("Name: " + student.getFirstName() + " " + student.getLastName());
        emailParagraph = new Paragraph("Email: " + student.getEmail());
        phoneNumberParagraph = new Paragraph("Phone Number: " + student.getPhoneNumber());
        gradeParagraph = new Paragraph("Grade: " + student.getGrade());
        studentClassParagraph = new Paragraph("Student Class: " + student.getStudentClass());

        currentDetailsLayout.add(nameParagraph, emailParagraph, phoneNumberParagraph, gradeParagraph, studentClassParagraph);

        // Form section
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("student-edit-profile-form-layout");

        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");

        saveButton = new Button("Save");
        saveButton.addClassName("student-edit-profile-save-button");

        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, saveButton);

        // Combine current details and form into a main layout
        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setSpacing(true);
        mainLayout.setAlignItems(Alignment.STRETCH); // Ensure child elements are stretched equally
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.addClassName("student-edit-profile-main-layout");

        // Add title and main layout to the wrapper layout
        wrapperLayout.add(h3, mainLayout);

        // Add the wrapper layout to the main view
        add(wrapperLayout);

    }


    // Method to update student profile values
    private void updateStudentProfile() {
        if (!firstNameField.isEmpty()) student.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) student.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) student.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) student.setEmail(emailField.getValue());

        // Update displayed paragraphs with new values
        nameParagraph.setText("Name: " + student.getFirstName() + " " + student.getLastName());
        emailParagraph.setText("Email: " + student.getEmail());
        phoneNumberParagraph.setText("Phone Number: " + student.getPhoneNumber());
    }

    // Listener to trigger the save operation outside of this class
    public void setSaveListener(Consumer<Student> saveListener) {
        saveButton.addClickListener(event -> {
            updateStudentProfile();
            saveListener.accept(student);
            Notification.show("Profile updated successfully", 3000, Notification.Position.TOP_CENTER);
        });
    }
}

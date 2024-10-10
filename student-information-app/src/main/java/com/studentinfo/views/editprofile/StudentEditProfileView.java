package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Setter;

import java.util.function.Consumer;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
public class StudentEditProfileView extends VerticalLayout {

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField phoneNumberField;
    private final TextField emailField;
    private final PasswordField newPasswordField;
    private final Button saveButton;
    private final Student student;
    private final UserService userService;

    // Consumer to handle save actions
    @Setter
    private Consumer<Student> saveListener;

    // Paragraphs to display current details
    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;

    // Constructor accepting a Student object and UserService
    public StudentEditProfileView(Student studentData, UserService userService) {
        this.student = studentData;
        this.userService = userService;

        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("student-edit-profile-container");

        // Page title
        H3 h3 = new H3("Edit Student Profile");
        h3.addClassName("student-edit-profile-title");

        // Current details section
        VerticalLayout currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("student-current-details-layout");

        H3 currentDetailsHeading = new H3("Current Details:");
        currentDetailsHeading.addClassName("current-details-title");
        currentDetailsLayout.add(currentDetailsHeading);

        nameParagraph = new Paragraph();
        nameParagraph.getElement().setProperty("innerHTML", "<span class='label'>Name </span>" + "<br>" + student.getFirstName() + " " + student.getLastName());
        emailParagraph = new Paragraph();
        emailParagraph.getElement().setProperty("innerHTML", "<span class='label'>Email </span>" + "<br>" + student.getEmail());
        phoneNumberParagraph = new Paragraph();
        phoneNumberParagraph.getElement().setProperty("innerHTML", "<span class='label'>Phone Number </span>" + "<br>" + student.getPhoneNumber());

        currentDetailsLayout.add(nameParagraph, emailParagraph, phoneNumberParagraph);

        // Form section
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("student-edit-profile-form-layout");

        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");
        newPasswordField = new PasswordField("New Password");

        // Save button
        saveButton = new Button("Save"); // Save button is now a class-level field
        saveButton.addClassName("student-edit-profile-save-button");

        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, newPasswordField, saveButton);

        // Combine current details and form into a main layout
        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setSpacing(true);
        mainLayout.setAlignItems(Alignment.STRETCH);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.addClassName("student-edit-profile-main-layout");

        // Add title and main layout to the wrapper layout
        add(h3, mainLayout);

        // Add save listener
        saveButton.addClickListener(event -> {
            updateStudentProfile(); // This already updates the password
            if (saveListener != null) {
                saveListener.accept(student); // Trigger the save listener
            }

            Notification.show("Profile updated successfully", 3000, Notification.Position.TOP_CENTER);
        });
    }

    // Method to update the student profile
    private void updateStudentProfile() {
        if (!firstNameField.isEmpty()) student.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) student.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) student.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) student.setEmail(emailField.getValue());

        String newPassword = newPasswordField.getValue();
        if (!newPassword.isEmpty()) {
            userService.updatePassword(student.getEmail(), newPassword); // Ensure password is updated
        }

        // Save the updated student profile
        userService.save(student);

        // Update displayed paragraphs with new values
        nameParagraph.getElement().setProperty("innerHTML", "<span class='label'>Name </span>" + "<br>" + student.getFirstName() + " " + student.getLastName());
        emailParagraph.getElement().setProperty("innerHTML", "<span class='label'>Email </span>" + "<br>" + student.getEmail());
        phoneNumberParagraph.getElement().setProperty("innerHTML", "<span class='label'>Phone Number </span>" + "<br>" + student.getPhoneNumber());
    }
}

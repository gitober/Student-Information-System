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
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

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

    private final VerticalLayout currentDetailsLayout;

    public TeacherEditProfileView(Teacher teacher, DepartmentService departmentService, SubjectService subjectService) {
        this.teacher = teacher;

        // Updated the class name to match the namespaced CSS
        addClassName("teacher-edit-profile-view");

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Edit Teacher Profile"));

        // Create layout for current details
        currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("teacher-edit-profile-view-current-details-layout");

        // Initialize form fields without setting values initially (empty fields)
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");

        departmentComboBox = new ComboBox<>("Department");
        departmentComboBox.setItems(departmentService.findAll());
        departmentComboBox.setItemLabelGenerator(Department::getName);
        departmentComboBox.setValue(teacher.getDepartment());

        subjectComboBox = new ComboBox<>("Subject");
        subjectComboBox.setItems(subjectService.findAll());
        subjectComboBox.setItemLabelGenerator(Subject::getName);
        subjectComboBox.setValue(teacher.getSubject());

        saveButton = new Button("Save");
        saveButton.addClassName("teacher-edit-profile-view-save-button-teacher");

        // Create layout for form fields
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("teacher-edit-profile-view-form-layout");
        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, departmentComboBox, subjectComboBox, saveButton);

        // Create horizontal layout for current details and form fields
        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(Alignment.START); // Ensure both layouts stretch to the same height
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER); // Align content to start at the top

        mainLayout.setFlexGrow(1, currentDetailsLayout);
        mainLayout.setFlexGrow(1, formLayout);

        currentDetailsLayout.setHeightFull();
        formLayout.setHeightFull();

        add(mainLayout);

        // Set initial details display
        updateTeacherProfile();
    }

    // Method to update teacher profile values
    private void updateTeacherProfile() {
        if (!firstNameField.isEmpty()) teacher.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) teacher.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) teacher.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) teacher.setEmail(emailField.getValue());

        // Clear the current details layout and add the heading
        currentDetailsLayout.removeAll(); // Remove all existing content
        currentDetailsLayout.add(new H3("Current Details:"));

        // Add updated details with labels and values
        currentDetailsLayout.add(createDetailLayout("Name", teacher.getFirstName() + " " + teacher.getLastName()));
        currentDetailsLayout.add(createDetailLayout("Email", teacher.getEmail()));
        currentDetailsLayout.add(createDetailLayout("Phone Number", teacher.getPhoneNumber()));
        currentDetailsLayout.add(createDetailLayout("Department", teacher.getDepartment() != null ? teacher.getDepartment().getName() : "N/A"));
        currentDetailsLayout.add(createDetailLayout("Subject", teacher.getSubject() != null ? teacher.getSubject().getName() : "N/A"));
    }

    private VerticalLayout createDetailLayout(String label, String value) {
        VerticalLayout detailLayout = new VerticalLayout();
        detailLayout.addClassName("teacher-edit-profile-view-current-details-layout-detail");

        // Create a Paragraph to hold HTML content
        Paragraph detailParagraph = new Paragraph();
        detailParagraph.getElement().setProperty("innerHTML", "<div class='teacher-edit-profile-view-current-details-layout-label'>" + label + "</div><div class='teacher-edit-profile-view-current-details-layout-value'>" + value + "</div>");
        detailLayout.add(detailParagraph);

        return detailLayout;
    }

    public void setSaveListener(Consumer<Teacher> saveListener) {
        saveButton.addClickListener(event -> {
            // Update the teacher object with new values from the form
            updateTeacherProfile(); // Call the update method to handle fields only if not empty
            teacher.setDepartment(departmentComboBox.getValue());
            teacher.setSubject(subjectComboBox.getValue());

            // Refresh the current details display with updated values
            updateTeacherProfile();

            // Call the save listener with the updated teacher object
            saveListener.accept(teacher);

            // Show confirmation notification
            Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);
        });
    }
}

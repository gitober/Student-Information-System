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

        addClassName("teacher-edit-profile-view");
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Edit Teacher Profile"));

        // Create layout for current details
        currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("current-details-layout");

        // Initialize form fields with current teacher data
        firstNameField = new TextField("First Name");
        firstNameField.setValue(teacher.getFirstName());

        lastNameField = new TextField("Last Name");
        lastNameField.setValue(teacher.getLastName());

        phoneNumberField = new TextField("Phone Number");
        phoneNumberField.setValue(teacher.getPhoneNumber());

        emailField = new TextField("Email");
        emailField.setValue(teacher.getEmail());

        departmentComboBox = new ComboBox<>("Department");
        departmentComboBox.setItems(departmentService.findAll());
        departmentComboBox.setItemLabelGenerator(Department::getName);
        departmentComboBox.setValue(teacher.getDepartment());

        subjectComboBox = new ComboBox<>("Subject");
        subjectComboBox.setItems(subjectService.findAll());
        subjectComboBox.setItemLabelGenerator(Subject::getName);
        subjectComboBox.setValue(teacher.getSubject());

        saveButton = new Button("Save");
        saveButton.addClassName("save-button");

        // Create layout for form fields
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("form-layout");
        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, departmentComboBox, subjectComboBox, saveButton);

        // Create horizontal layout for current details and form fields
        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(Alignment.STRETCH); // Ensures both layouts stretch to the same height
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        currentDetailsLayout.setHeight("100%"); // Ensure it takes up available space
        formLayout.setHeight("100%"); // Ensure it takes up available space


        add(mainLayout);

        // Set initial details
        updateTeacherProfile();
    }

    private void updateTeacherProfile() {
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
        detailLayout.addClassName("detail");

        // Create a Paragraph to hold HTML content
        Paragraph detailParagraph = new Paragraph();
        detailParagraph.getElement().setProperty("innerHTML", "<div class='label'>" + label + "</div><div class='value'>" + value + "</div>");
        detailLayout.add(detailParagraph);

        return detailLayout;
    }

    public void setSaveListener(Consumer<Teacher> saveListener) {
        saveButton.addClickListener(event -> {
            updateTeacherProfile();
            saveListener.accept(teacher);
            Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);
        });
    }
}

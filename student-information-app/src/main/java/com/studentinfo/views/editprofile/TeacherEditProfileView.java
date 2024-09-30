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

        addClassName("teacher-edit-profile-view-container");

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2("Edit Teacher Profile");
        title.addClassName("teacher-edit-profile-view-title");
        add(title);

        currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("teacher-edit-profile-view-current-details-layout");

        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        phoneNumberField = new TextField("Phone Number");
        emailField = new TextField("Email");

        firstNameField.addClassName("teacher-edit-profile-view-input");
        lastNameField.addClassName("teacher-edit-profile-view-input");
        phoneNumberField.addClassName("teacher-edit-profile-view-input");
        emailField.addClassName("teacher-edit-profile-view-input");

        departmentComboBox = new ComboBox<>("Department");
        departmentComboBox.setItems(departmentService.findAll());
        System.out.println("Departments: " + departmentService.findAll());
        departmentComboBox.setItemLabelGenerator(department ->
                department != null && department.getDepartmentName() != null ? department.getDepartmentName() : "N/A");
        departmentComboBox.setValue(teacher.getDepartment());
        departmentComboBox.addClassName("teacher-edit-profile-view-combobox");

        subjectComboBox = new ComboBox<>("Subject");
        subjectComboBox.setItems(subjectService.findAll());
        System.out.println("Subjects: " + subjectService.findAll());
        subjectComboBox.setItemLabelGenerator(subject ->
                subject != null && subject.getName() != null ? subject.getName() : "N/A");
        subjectComboBox.setValue(teacher.getSubject());
        subjectComboBox.addClassName("teacher-edit-profile-view-combobox");

        saveButton = new Button("Save");
        saveButton.addClassName("teacher-edit-profile-view-save-button");

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("teacher-edit-profile-view-form-layout");
        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, departmentComboBox, subjectComboBox, saveButton);

        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(Alignment.START);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        mainLayout.setFlexGrow(1, currentDetailsLayout);
        mainLayout.setFlexGrow(1, formLayout);

        currentDetailsLayout.setHeightFull();
        formLayout.setHeightFull();

        add(mainLayout);

        updateTeacherProfile();
    }

    private void updateTeacherProfile() {
        if (!firstNameField.isEmpty()) teacher.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) teacher.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) teacher.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) teacher.setEmail(emailField.getValue());

        currentDetailsLayout.removeAll();
        H3 detailsTitle = new H3("Current Details:");
        detailsTitle.addClassName("teacher-edit-profile-view-current-details-title");
        currentDetailsLayout.add(detailsTitle);

        currentDetailsLayout.add(createDetailLayout("Name", teacher.getFirstName() + " " + teacher.getLastName()));
        currentDetailsLayout.add(createDetailLayout("Email", teacher.getEmail()));
        currentDetailsLayout.add(createDetailLayout("Phone Number", teacher.getPhoneNumber()));
        currentDetailsLayout.add(createDetailLayout("Department", teacher.getDepartment() != null ? teacher.getDepartment().getDepartmentName() : "N/A"));
        currentDetailsLayout.add(createDetailLayout("Subject", teacher.getSubject() != null ? teacher.getSubject().getName() : "N/A"));
    }

    private VerticalLayout createDetailLayout(String label, String value) {
        VerticalLayout detailLayout = new VerticalLayout();
        detailLayout.addClassName("teacher-edit-profile-view-detail");

        Paragraph detailParagraph = new Paragraph();
        detailParagraph.getElement().setProperty("innerHTML", "<div class='teacher-edit-profile-view-label'>" + label + "</div><div class='teacher-edit-profile-view-value'>" + value + "</div>");
        detailLayout.add(detailParagraph);

        return detailLayout;
    }

    public void setSaveListener(Consumer<Teacher> saveListener) {
        saveButton.addClickListener(event -> {
            updateTeacherProfile();
            teacher.setDepartment(departmentComboBox.getValue());
            teacher.setSubject(subjectComboBox.getValue());

            updateTeacherProfile();
            saveListener.accept(teacher);

            Notification.show("Profile updated successfully!", 3000, Notification.Position.TOP_CENTER);

            // Clear the fields after saving
            firstNameField.clear();
            lastNameField.clear();
            phoneNumberField.clear();
            emailField.clear();
            departmentComboBox.clear();
            subjectComboBox.clear();
        });
    }
}

package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Department;
import com.studentinfo.data.entity.Subject;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.DateService;
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
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.function.Consumer;

@SpringComponent
@UIScope
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
    private final transient MessageSource messageSource;

    private static final String CSS_CLASS_INPUT = "teacher-edit-profile-view-input";

    @Autowired
    public TeacherEditProfileView(Teacher teacher, DepartmentService departmentService, SubjectService subjectService, DateService dateService, MessageSource messageSource) {
        this.teacher = teacher;
        this.messageSource = messageSource;

        addClassName("teacher-edit-profile-view-container");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H2 title = new H2(getMessage("teacher.edit.profile.title"));
        title.addClassName("teacher-edit-profile-view-title");
        add(title);

        currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("teacher-edit-profile-view-current-details-layout");

        firstNameField = new TextField(getMessage("teacher.edit.profile.firstName"));
        lastNameField = new TextField(getMessage("teacher.edit.profile.lastName"));
        phoneNumberField = new TextField(getMessage("teacher.edit.profile.phoneNumber"));
        emailField = new TextField(getMessage("teacher.edit.profile.email"));

        firstNameField.addClassName(CSS_CLASS_INPUT);
        lastNameField.addClassName(CSS_CLASS_INPUT);
        phoneNumberField.addClassName(CSS_CLASS_INPUT);
        emailField.addClassName(CSS_CLASS_INPUT);

        departmentComboBox = new ComboBox<>(getMessage("teacher.edit.profile.department"));
        departmentComboBox.setItems(departmentService.findAll());
        departmentComboBox.setItemLabelGenerator(department -> department != null && department.getDepartmentName() != null ? department.getDepartmentName() : "N/A");
        departmentComboBox.setValue(teacher.getDepartment());
        departmentComboBox.addClassName("teacher-edit-profile-view-combobox");

        subjectComboBox = new ComboBox<>(getMessage("teacher.edit.profile.subject"));
        subjectComboBox.setItems(subjectService.findAll());
        subjectComboBox.setItemLabelGenerator(subject -> subject != null && subject.getName() != null ? subject.getName() : "N/A");
        subjectComboBox.setValue(teacher.getSubject());
        subjectComboBox.addClassName("teacher-edit-profile-view-combobox");

        saveButton = new Button(getMessage("teacher.edit.profile.save"));
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
        currentDetailsLayout.removeAll();
        H3 detailsTitle = new H3(getMessage("teacher.edit.profile.currentDetails"));
        detailsTitle.addClassName("teacher-edit-profile-view-current-details-title");
        currentDetailsLayout.add(detailsTitle);

        currentDetailsLayout.add(createDetailLayout(getMessage("teacher.edit.profile.name"), teacher.getFirstName() + " " + teacher.getLastName()));
        currentDetailsLayout.add(createDetailLayout(getMessage("teacher.edit.profile.email"), teacher.getEmail()));
        currentDetailsLayout.add(createDetailLayout(getMessage("teacher.edit.profile.phoneNumber"), teacher.getPhoneNumber()));
        currentDetailsLayout.add(createDetailLayout(getMessage("teacher.edit.profile.department"), teacher.getDepartment() != null ? teacher.getDepartment().getDepartmentName() : "N/A"));
        currentDetailsLayout.add(createDetailLayout(getMessage("teacher.edit.profile.subject"), teacher.getSubject() != null ? teacher.getSubject().getName() : "N/A"));
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
            // Only update fields if they have non-empty values to avoid overwriting with null
            if (!firstNameField.isEmpty()) teacher.setFirstName(firstNameField.getValue());
            if (!lastNameField.isEmpty()) teacher.setLastName(lastNameField.getValue());
            if (!phoneNumberField.isEmpty()) teacher.setPhoneNumber(phoneNumberField.getValue());
            if (!emailField.isEmpty()) teacher.setEmail(emailField.getValue());
            if (departmentComboBox.getValue() != null) teacher.setDepartment(departmentComboBox.getValue());
            if (subjectComboBox.getValue() != null) teacher.setSubject(subjectComboBox.getValue());

            // Save teacher entity through the provided save listener
            saveListener.accept(teacher);

            Notification.show(getMessage("teacher.edit.profile.success"), 3000, Notification.Position.TOP_CENTER);

            updateTeacherProfile(); // Refresh displayed profile details

            // Clear fields after saving
            firstNameField.clear();
            lastNameField.clear();
            phoneNumberField.clear();
            emailField.clear();
            departmentComboBox.clear();
            subjectComboBox.clear();
        });
    }


    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component; // Added this import
import lombok.Setter;

import java.util.function.Consumer;

@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
@Component // Added this annotation to make it a Spring bean
public class StudentEditProfileView extends VerticalLayout {

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField phoneNumberField;
    private final TextField emailField;
    private final PasswordField newPasswordField;
    private final Button saveButton;
    private final Student student;
    private final UserService userService;
    private final MessageSource messageSource;

    @Setter
    private Consumer<Student> saveListener;

    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;

    @Autowired
    public StudentEditProfileView(Student studentData, UserService userService, MessageSource messageSource) {
        this.student = studentData;
        this.userService = userService;
        this.messageSource = messageSource;

        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("student-edit-profile-container");

        H3 h3 = new H3(getMessage("student.edit.profile.title"));
        h3.addClassName("student-edit-profile-title");

        VerticalLayout currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("student-current-details-layout");

        H3 currentDetailsHeading = new H3(getMessage("student.edit.profile.currentDetails"));
        currentDetailsHeading.addClassName("current-details-title");
        currentDetailsLayout.add(currentDetailsHeading);

        nameParagraph = new Paragraph();
        nameParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.name") + "</span><br>" +
                student.getFirstName() + " " + student.getLastName());
        emailParagraph = new Paragraph();
        emailParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.email") + "</span><br>" +
                student.getEmail());
        phoneNumberParagraph = new Paragraph();
        phoneNumberParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.phoneNumber") + "</span><br>" +
                student.getPhoneNumber());

        currentDetailsLayout.add(nameParagraph, emailParagraph, phoneNumberParagraph);

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("student-edit-profile-form-layout");

        firstNameField = new TextField(getMessage("student.edit.profile.firstName"));
        lastNameField = new TextField(getMessage("student.edit.profile.lastName"));
        phoneNumberField = new TextField(getMessage("student.edit.profile.phoneNumber"));
        emailField = new TextField(getMessage("student.edit.profile.email"));
        newPasswordField = new PasswordField(getMessage("student.edit.profile.newPassword"));

        saveButton = new Button(getMessage("student.edit.profile.save"));
        saveButton.addClassName("student-edit-profile-save-button");

        formLayout.add(firstNameField, lastNameField, phoneNumberField, emailField, newPasswordField, saveButton);

        HorizontalLayout mainLayout = new HorizontalLayout(currentDetailsLayout, formLayout);
        mainLayout.setWidthFull();
        mainLayout.setSpacing(true);
        mainLayout.setAlignItems(Alignment.STRETCH);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        mainLayout.addClassName("student-edit-profile-main-layout");

        add(h3, mainLayout);

        saveButton.addClickListener(event -> {
            updateStudentProfile();
            if (saveListener != null) {
                saveListener.accept(student);
            }

            Notification.show(getMessage("student.edit.profile.success"), 3000, Notification.Position.TOP_CENTER);
        });
    }

    private void updateStudentProfile() {
        if (!firstNameField.isEmpty()) student.setFirstName(firstNameField.getValue());
        if (!lastNameField.isEmpty()) student.setLastName(lastNameField.getValue());
        if (!phoneNumberField.isEmpty()) student.setPhoneNumber(phoneNumberField.getValue());
        if (!emailField.isEmpty()) student.setEmail(emailField.getValue());

        String newPassword = newPasswordField.getValue();
        if (!newPassword.isEmpty()) {
            userService.updatePassword(student.getEmail(), newPassword);
        }

        userService.save(student);

        nameParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.name") + "</span><br>" +
                student.getFirstName() + " " + student.getLastName());
        emailParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.email") + "</span><br>" +
                student.getEmail());
        phoneNumberParagraph.getElement().setProperty("innerHTML", "<span class='label'>" + getMessage("student.edit.profile.phoneNumber") + "</span><br>" +
                student.getPhoneNumber());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

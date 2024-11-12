package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.function.Consumer;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/edit-profile-view/student-edit-profile-view.css")
public class StudentEditProfileView extends VerticalLayout {

    private static final String INNER_HTML = "innerHTML";
    private static final String LABEL_HTML = "<span class='label'>";
    private static final String CLOSING_SPAN_BR = "</span><br>";
    private static final String NAME_KEY = "student.edit.profile.name";
    private static final String EMAIL_KEY = "student.edit.profile.email";
    private static final String PHONE_NUMBER_KEY = "student.edit.profile.phoneNumber";
    private static final String FIRST_NAME_KEY = "student.edit.profile.firstName";
    private static final String LAST_NAME_KEY = "student.edit.profile.lastName";
    private static final String NEW_PASSWORD_KEY = "student.edit.profile.newPassword";
    private static final String SAVE_BUTTON_KEY = "student.edit.profile.save";
    private static final String SUCCESS_MESSAGE_KEY = "student.edit.profile.success";

    private final transient TextField firstNameField;
    private final transient TextField lastNameField;
    private final transient TextField phoneNumberField;
    private final transient TextField emailField;
    private final transient PasswordField newPasswordField;
    private final transient Student student;
    private final transient UserService userService;
    private final transient MessageSource messageSource;
    private final Button saveButton;

    @Setter
    private transient Consumer<Student> saveListener;

    private final Paragraph nameParagraph;
    private final Paragraph emailParagraph;
    private final Paragraph phoneNumberParagraph;

    @Autowired
    public StudentEditProfileView(Student student, UserService userService, MessageSource messageSource) {
        this.student = student;
        this.userService = userService;
        this.messageSource = messageSource;

        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("student-edit-profile-container");

        // Page title
        H3 h3 = new H3(getMessage("student.edit.profile.title"));
        h3.addClassName("student-edit-profile-title");

        // Current details section
        VerticalLayout currentDetailsLayout = new VerticalLayout();
        currentDetailsLayout.addClassName("student-current-details-layout");

        H3 currentDetailsHeading = new H3(getMessage("student.edit.profile.currentDetails"));
        currentDetailsHeading.addClassName("current-details-title");
        currentDetailsLayout.add(currentDetailsHeading);

        nameParagraph = new Paragraph();
        nameParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(NAME_KEY) + CLOSING_SPAN_BR +
                student.getFirstName() + " " + student.getLastName());
        emailParagraph = new Paragraph();
        emailParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(EMAIL_KEY) + CLOSING_SPAN_BR +
                student.getEmail());
        phoneNumberParagraph = new Paragraph();
        phoneNumberParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(PHONE_NUMBER_KEY) + CLOSING_SPAN_BR +
                student.getPhoneNumber());

        currentDetailsLayout.add(nameParagraph, emailParagraph, phoneNumberParagraph);

        // Form section
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.addClassName("student-edit-profile-form-layout");

        firstNameField = new TextField(getMessage(FIRST_NAME_KEY));
        lastNameField = new TextField(getMessage(LAST_NAME_KEY));
        phoneNumberField = new TextField(getMessage(PHONE_NUMBER_KEY));
        emailField = new TextField(getMessage(EMAIL_KEY));
        newPasswordField = new PasswordField(getMessage(NEW_PASSWORD_KEY));

        saveButton = new Button(getMessage(SAVE_BUTTON_KEY));
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

            // Only show the notification if UI.getCurrent() is available
            if (UI.getCurrent() != null) {
                Notification.show(getMessage(SUCCESS_MESSAGE_KEY), 3000, Notification.Position.TOP_CENTER);
            }
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

        nameParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(NAME_KEY) + CLOSING_SPAN_BR +
                student.getFirstName() + " " + student.getLastName());
        emailParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(EMAIL_KEY) + CLOSING_SPAN_BR +
                student.getEmail());
        phoneNumberParagraph.getElement().setProperty(INNER_HTML, LABEL_HTML + getMessage(PHONE_NUMBER_KEY) + CLOSING_SPAN_BR +
                student.getPhoneNumber());

        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

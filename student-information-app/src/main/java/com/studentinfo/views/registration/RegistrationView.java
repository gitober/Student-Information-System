package com.studentinfo.views.registration;

import com.studentinfo.data.dto.RegistrationDTO;
import com.studentinfo.data.entity.Language;
import com.studentinfo.views.header.HeaderView;
import com.studentinfo.utils.RegistrationHandler;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@PageTitle("Registration")
@Route(value = "register")
@AnonymousAllowed
@CssImport("./themes/studentinformationapp/views/registration-view.css")
public class RegistrationView extends Composite<VerticalLayout> {

    private final transient RegistrationHandler registrationHandler;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final EmailField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final ComboBox<String> roleComboBox;
    private final Button saveButton;

    @Autowired
    public RegistrationView(RegistrationHandler registrationHandler, MessageSource messageSource) {
        this.registrationHandler = registrationHandler;

        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.START);
        mainLayout.addClassName("registration-container");

        HeaderView header = new HeaderView(messageSource);
        header.setWidthFull();
        mainLayout.add(header);

        mainLayout.getStyle().set("padding-top", "60px");

        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setAlignItems(Alignment.CENTER);
        layoutColumn2.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn2.addClassName("registration-form");

        H3 h3 = new H3(messageSource.getMessage("registration.formTitle", null, LocaleContextHolder.getLocale()));
        h3.addClassName("registration-form-title");

        FormLayout formLayout2Col = new FormLayout();
        firstNameField = new TextField(messageSource.getMessage("registration.firstName", null, LocaleContextHolder.getLocale()));
        lastNameField = new TextField(messageSource.getMessage("registration.lastName", null, LocaleContextHolder.getLocale()));
        DatePicker birthdayField = new DatePicker(messageSource.getMessage("registration.birthday", null, LocaleContextHolder.getLocale()));

        // Set date format based on locale
        Locale currentLocale = LocaleContextHolder.getLocale();
        if ("ch".equalsIgnoreCase(currentLocale.getLanguage())) {
            birthdayField.setI18n(new DatePicker.DatePickerI18n()
                    .setDateFormat("yyyy-MM-dd")); // Custom date format for Chinese locale
        } else {
            birthdayField.setI18n(new DatePicker.DatePickerI18n()
                    .setDateFormat("dd/MM/yyyy")); // Default date format
        }
        birthdayField.setLocale(currentLocale);

        TextField phoneNumberField = new TextField(messageSource.getMessage("registration.phoneNumber", null, LocaleContextHolder.getLocale()));
        emailField = new EmailField(messageSource.getMessage("registration.email", null, LocaleContextHolder.getLocale()));
        passwordField = new PasswordField(messageSource.getMessage("registration.password", null, LocaleContextHolder.getLocale()));
        confirmPasswordField = new PasswordField(messageSource.getMessage("registration.confirmPassword", null, LocaleContextHolder.getLocale()));

        formLayout2Col.add(
                firstNameField,
                lastNameField,
                birthdayField,
                phoneNumberField,
                emailField,
                passwordField,
                confirmPasswordField
        );
        formLayout2Col.addClassName("registration-form-field");
        layoutColumn2.add(h3, formLayout2Col);

        roleComboBox = new ComboBox<>(messageSource.getMessage("registration.role", null, LocaleContextHolder.getLocale()));
        roleComboBox.setItems(
                messageSource.getMessage("registration.role.student", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("registration.role.teacher", null, LocaleContextHolder.getLocale())
        );
        roleComboBox.setPlaceholder(messageSource.getMessage("registration.role.placeholder", null, LocaleContextHolder.getLocale()));
        formLayout2Col.add(roleComboBox);

        saveButton = new Button(messageSource.getMessage("registration.registerButton", null, LocaleContextHolder.getLocale()));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("registration-primary-button");

        Button cancelButton = new Button(messageSource.getMessage("registration.cancelButton", null, LocaleContextHolder.getLocale()));
        cancelButton.addClassName("registration-secondary-button");

        saveButton.addClickListener(e -> handleRegistration(birthdayField, phoneNumberField, messageSource));
        cancelButton.addClickListener(e -> UI.getCurrent().navigate("login"));

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.addClassName("registration-button-layout");
        layoutColumn2.add(buttonLayout);

        mainLayout.addAndExpand(layoutColumn2);

        addFieldListeners(messageSource);
    }

    private void handleRegistration(DatePicker birthdayField, TextField phoneNumberField, MessageSource messageSource) {
        if (firstNameField.isEmpty() || lastNameField.isEmpty() || emailField.isEmpty() ||
                passwordField.isEmpty() || confirmPasswordField.isEmpty() || roleComboBox.isEmpty()) {
            Notification.show(messageSource.getMessage("registration.emptyFields", null, LocaleContextHolder.getLocale()));
            return;
        }

        // Map the selected role to the expected English values
        String selectedRole = roleComboBox.getValue();
        String role = null;
        if (selectedRole.equals(messageSource.getMessage("registration.role.student", null, LocaleContextHolder.getLocale()))) {
            role = "Student";
        } else if (selectedRole.equals(messageSource.getMessage("registration.role.teacher", null, LocaleContextHolder.getLocale()))) {
            role = "Teacher";
        }

        // Get the current locale from LocaleContextHolder
        Language currentLocale = Language.valueOf(LocaleContextHolder.getLocale().getLanguage().toUpperCase());

        // Create RegistrationDTO object using builder pattern
        RegistrationDTO registrationDTO = RegistrationDTO.builder()
                .firstName(firstNameField.getValue())
                .lastName(lastNameField.getValue())
                .birthday(birthdayField.getValue())
                .phoneNumber(phoneNumberField.getValue())
                .email(emailField.getValue())
                .password(passwordField.getValue())
                .role(role)
                .currentLocale(currentLocale)
                .build();

        boolean registrationSuccessful = registrationHandler.registerUser(registrationDTO);

        if (registrationSuccessful) {
            Notification.show(messageSource.getMessage("registration.success", null, LocaleContextHolder.getLocale()));
            UI.getCurrent().navigate("login");
        } else {
            Notification.show(messageSource.getMessage("registration.failure", null, LocaleContextHolder.getLocale()));
        }
    }

    private void addFieldListeners(MessageSource messageSource) {
        emailField.addValueChangeListener(e -> validateEmail(messageSource));
        passwordField.addValueChangeListener(e -> validatePasswordStrength(messageSource));
        confirmPasswordField.addValueChangeListener(e -> validatePasswordMatch(messageSource));
    }

    private void validateEmail(MessageSource messageSource) {
        if (emailField.isEmpty() || !emailField.getValue().contains("@")) {
            emailField.setErrorMessage(messageSource.getMessage("registration.invalidEmail", null, LocaleContextHolder.getLocale()));
            emailField.setInvalid(true);
        } else if (registrationHandler.isEmailRegistered(emailField.getValue())) {
            emailField.setErrorMessage(messageSource.getMessage("registration.emailRegistered", null, LocaleContextHolder.getLocale()));
            emailField.setInvalid(true);
        } else {
            emailField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    private void validatePasswordStrength(MessageSource messageSource) {
        String password = passwordField.getValue();

        // Check if password length is at least 8 characters and contains at least one digit
        if (password.length() < 8 || !hasDigit(password)) {
            passwordField.setErrorMessage(messageSource.getMessage("registration.weakPassword", null, LocaleContextHolder.getLocale()));
            passwordField.setInvalid(true);
        } else {
            passwordField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    // Helper method to check if the password contains at least one digit
    private boolean hasDigit(String password) {
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void validatePasswordMatch(MessageSource messageSource) {
        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            confirmPasswordField.setErrorMessage(messageSource.getMessage("registration.passwordMismatch", null, LocaleContextHolder.getLocale()));
            confirmPasswordField.setInvalid(true);
        } else {
            confirmPasswordField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    private void updateSaveButtonState() {
        saveButton.setEnabled(!emailField.isInvalid() && !passwordField.isInvalid() && !confirmPasswordField.isInvalid());
    }
}

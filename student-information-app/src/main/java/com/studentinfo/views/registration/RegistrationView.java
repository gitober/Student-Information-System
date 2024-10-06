package com.studentinfo.views.registration;

import com.studentinfo.views.header.HeaderView;
import com.studentinfo.services.RegistrationHandler;
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

@PageTitle("Registration")
@Route(value = "register")
@AnonymousAllowed
@CssImport("./themes/studentinformationapp/views/registration-view.css")
public class RegistrationView extends Composite<VerticalLayout> {

    private final RegistrationHandler registrationHandler;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final EmailField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final ComboBox<String> roleComboBox;
    private final Button saveButton;

    @Autowired
    public RegistrationView(RegistrationHandler registrationHandler) {
        this.registrationHandler = registrationHandler;

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.START);
        mainLayout.addClassName("registration-container");

        // Add the reusable Header component
        HeaderView header = new HeaderView("EduBird");
        header.setWidthFull();
        mainLayout.add(header);

        // Add padding to ensure content starts below the header
        mainLayout.getStyle().set("padding-top", "60px");

        // Layout for form and content
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setAlignItems(Alignment.CENTER);
        layoutColumn2.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn2.addClassName("registration-form");

        // Form Title
        H3 h3 = new H3("Register Here");
        h3.addClassName("registration-form-title");

        // Form fields
        FormLayout formLayout2Col = new FormLayout();
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        DatePicker birthdayField = new DatePicker("Birthday");
        TextField phoneNumberField = new TextField("Phone Number");
        emailField = new EmailField("Email");
        passwordField = new PasswordField("Create Password");
        confirmPasswordField = new PasswordField("Confirm Password");

        // Add components to the form layout
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

        // Role selection
        roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems("Student", "Teacher");
        roleComboBox.setPlaceholder("Select role");
        formLayout2Col.add(roleComboBox);

        // Buttons
        saveButton = new Button("Register");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("registration-primary-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("registration-secondary-button");

        // Button click listeners
        saveButton.addClickListener(e -> handleRegistration(birthdayField, phoneNumberField));

        cancelButton.addClickListener(e -> UI.getCurrent().navigate("login"));

        // Buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.addClassName("registration-button-layout");
        layoutColumn2.add(buttonLayout);

        // Add the form layout to the main layout
        mainLayout.addAndExpand(layoutColumn2);

        // Add real-time field listeners for validation
        addFieldListeners();
    }

    private void handleRegistration(DatePicker birthdayField, TextField phoneNumberField) {
        // Check if any required fields are empty
        if (firstNameField.isEmpty() || lastNameField.isEmpty() || emailField.isEmpty() ||
                passwordField.isEmpty() || confirmPasswordField.isEmpty() || roleComboBox.isEmpty()) {
            Notification.show("Please fill in all the required fields.");
            return;
        }

        // Attempt to register the user
        boolean registrationSuccessful = registrationHandler.registerUser(
                firstNameField.getValue(),
                lastNameField.getValue(),
                birthdayField.getValue(),
                phoneNumberField.getValue(),
                emailField.getValue(),
                passwordField.getValue(),
                roleComboBox.getValue()
        );

        if (registrationSuccessful) {
            Notification.show("Registration successful!");
            UI.getCurrent().navigate("login");
        } else {
            Notification.show("Registration failed. The email may already be in use or an unexpected error occurred.");
        }
    }

    // Real-time field validation listeners
    private void addFieldListeners() {
        emailField.addValueChangeListener(e -> validateEmail());
        passwordField.addValueChangeListener(e -> validatePasswordStrength());
        confirmPasswordField.addValueChangeListener(e -> validatePasswordMatch());
    }

    private void validateEmail() {
        if (emailField.isEmpty() || !emailField.getValue().contains("@")) {
            emailField.setErrorMessage("Please enter a valid email address.");
            emailField.setInvalid(true);
        } else if (registrationHandler.isEmailRegistered(emailField.getValue())) {
            emailField.setErrorMessage("This email is already registered.");
            emailField.setInvalid(true);
        } else {
            emailField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    private void validatePasswordStrength() {
        String password = passwordField.getValue();
        if (password.length() < 8 || !password.matches(".*\\d.*")) {
            passwordField.setErrorMessage("Password must be at least 8 characters long and contain at least one number.");
            passwordField.setInvalid(true);
        } else {
            passwordField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    private void validatePasswordMatch() {
        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            confirmPasswordField.setErrorMessage("Passwords do not match.");
            confirmPasswordField.setInvalid(true);
        } else {
            confirmPasswordField.setInvalid(false);
        }
        updateSaveButtonState();
    }

    // Enable or disable the save button based on form validation
    private void updateSaveButtonState() {
        saveButton.setEnabled(!emailField.isInvalid() && !passwordField.isInvalid() && !confirmPasswordField.isInvalid());
    }
}

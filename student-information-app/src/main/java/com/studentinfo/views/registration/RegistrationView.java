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
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        DatePicker birthdayField = new DatePicker("Birthday");
        TextField phoneNumberField = new TextField("Phone Number");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Create Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

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
        ComboBox<String> roleComboBox = new ComboBox<>("Role");
        roleComboBox.setItems("Student", "Teacher");
        roleComboBox.setPlaceholder("Select role");
        formLayout2Col.add(roleComboBox);

        // Buttons
        Button saveButton = new Button("Register");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("registration-primary-button");

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("registration-secondary-button");

        // Button click listeners
        saveButton.addClickListener(e -> {
            if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
                Notification.show("Passwords do not match. Please try again.");
                return;
            }

            boolean registrationSuccessful = registrationHandler.registerUser(
                    firstNameField.getValue(),
                    lastNameField.getValue(),
                    birthdayField.getValue(),
                    phoneNumberField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue(),
                    roleComboBox.getValue()
                    // No studentGrade and studentClass parameters anymore
            );

            if (registrationSuccessful) {
                Notification.show("Registration successful!");
                UI.getCurrent().navigate("login");
            } else {
                Notification.show("Registration failed. Please try again.");
            }
        });

        cancelButton.addClickListener(e -> UI.getCurrent().navigate("login"));

        // Buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.addClassName("registration-button-layout");
        layoutColumn2.add(buttonLayout);

        // Add the form layout to the main layout
        mainLayout.addAndExpand(layoutColumn2);
    }
}

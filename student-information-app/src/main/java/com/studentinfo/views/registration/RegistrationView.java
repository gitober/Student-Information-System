package com.studentinfo.views.registration;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.time.LocalDate;

@PageTitle("Registration")
@Route(value = "register")
@AnonymousAllowed
public class RegistrationView extends Composite<VerticalLayout> {

    public RegistrationView() {
        // Main layout
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3("Personal Information");
        FormLayout formLayout2Col = new FormLayout();

        // Form fields
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        DatePicker birthdayField = new DatePicker("Birthday");
        TextField phoneNumberField = new TextField("Phone Number");
        EmailField emailField = new EmailField("Email");
        PasswordField passwordField = new PasswordField("Create Password");
        PasswordField confirmPasswordField = new PasswordField("Confirm Password");

        // Buttons
        Button saveButton = new Button("Register");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("Cancel");

        // Button click listeners
        saveButton.addClickListener(e -> {
            // Validate password and confirm password match
            if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
                Notification.show("Passwords do not match. Please try again.");
                return;
            }

            // Simulate saving the user (you would replace this with actual saving logic)
            boolean registrationSuccessful = saveUser(
                    firstNameField.getValue(),
                    lastNameField.getValue(),
                    birthdayField.getValue(),
                    phoneNumberField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue()
            );

            if (registrationSuccessful) {
                // Show success notification
                Notification.show("Registration successful!");

                // Navigate back to the login page (index route "")
                UI.getCurrent().navigate("");
            } else {
                Notification.show("Registration failed. Please try again.");
            }
        });

        cancelButton.addClickListener(e -> {
            // Navigate back to the login page without saving
            UI.getCurrent().navigate("");
        });

        // Layout configuration
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setAlignItems(Alignment.CENTER);
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);

        // Add components to the layout
        formLayout2Col.add(
                firstNameField,
                lastNameField,
                birthdayField,
                phoneNumberField,
                emailField,
                passwordField,
                confirmPasswordField
        );
        layoutColumn2.add(h3, formLayout2Col);

        // Buttons layout
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.addClassName(Gap.MEDIUM);
        layoutColumn2.add(buttonLayout);

        // Add main layout to the content
        getContent().add(layoutColumn2);
    }

    /**
     * Simulates saving the user information.
     * Replace this method with actual service call to save the user details.
     */
    private boolean saveUser(String firstName, String lastName, LocalDate birthday, String phoneNumber,
                             String email, String password) {
        // Add the logic to save the user details here (e.g., call a service method)
        // Return true if successful, false otherwise

        // This is a stub. Replace with actual save logic.
        System.out.println("Saving user: " + firstName + " " + lastName);
        return true; // Simulate successful registration
    }
}

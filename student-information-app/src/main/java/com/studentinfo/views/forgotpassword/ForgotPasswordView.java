package com.studentinfo.views.forgotpassword;

import com.studentinfo.services.PasswordResetService;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Forgot Password")
@Route(value = "forgotpassword")
@AnonymousAllowed
@CssImport("./themes/studentinformationapp/views/forgotpassword-view.css")
public class ForgotPasswordView extends Composite<VerticalLayout> {

    private final EmailField emailField;
    private final Button submitButton;
    private final Button cancelButton;

    @Autowired
    private PasswordResetService passwordResetService;

    public ForgotPasswordView() {
        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        mainLayout.add(new HeaderView("EduBird"));

        VerticalLayout formContainer = new VerticalLayout();
        formContainer.setWidth("100%");
        formContainer.setMaxWidth("400px");
        formContainer.setAlignItems(Alignment.CENTER);
        formContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        formContainer.addClassName("password-form-container"); // Updated class name based on CSS

        H3 title = new H3("Forgot Password?");
        title.addClassName("password-form-title"); // Updated class name based on CSS
        H6 subtitle = new H6("Enter your email here");
        subtitle.addClassName("password-form-subtitle"); // Updated class name based on CSS

        FormLayout formLayout = new FormLayout();
        formLayout.setWidthFull();
        emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.addClassName("password-email-field"); // Updated class name based on CSS

        submitButton = new Button("Submit");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClickListener(e -> handleForgotPassword());
        submitButton.addClassName("password-submit-button"); // Updated class name based on CSS

        cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> navigateToLogin());
        cancelButton.addClassName("password-cancel-button"); // Updated class name based on CSS

        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, cancelButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        formLayout.add(emailField);
        formContainer.add(title, subtitle, formLayout, buttonLayout);

        mainLayout.add(formContainer);
    }

    private void handleForgotPassword() {
        String email = emailField.getValue();
        if (email.isEmpty()) {
            Notification.show("Please enter your email address.", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        // Call the service to initiate the password reset
        passwordResetService.initiatePasswordReset(email);

        Notification.show("If this email is registered, a password reset link will be sent.", 5000, Notification.Position.TOP_CENTER);
        emailField.clear();
    }

    private void navigateToLogin() {
        getUI().ifPresent(ui -> ui.navigate("login"));
    }
}

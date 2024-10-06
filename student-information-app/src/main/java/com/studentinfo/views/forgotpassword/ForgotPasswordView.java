package com.studentinfo.views.forgotpassword;

import com.studentinfo.services.EmailService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@PageTitle("Forgot Password / Reset Password")
@Route(value = "forgotpassword")
@AnonymousAllowed
@CssImport("./themes/studentinformationapp/views/forgotpassword-view.css")
public class ForgotPasswordView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private EmailService emailService;

    private final EmailField emailField;
    private final PasswordField newPasswordField;
    private final VerticalLayout forgotPasswordLayout;
    private final VerticalLayout resetPasswordLayout;
    private String resetToken = null; // Store the reset token here

    public ForgotPasswordView() {
        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.addClassName("password-reset-page");

        // "Forgot Password" layout
        forgotPasswordLayout = new VerticalLayout();
        forgotPasswordLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        forgotPasswordLayout.addClassName("forgot-password-layout");

        H3 forgotPasswordTitle = new H3("Forgot Password?");
        forgotPasswordTitle.addClassName("forgot-password-title");

        emailField = new EmailField("Email");
        emailField.addClassName("forgot-password-email-field");

        Button submitButton = new Button("Submit");
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClassName("forgot-password-submit-button");
        submitButton.addClickListener(e -> handleForgotPassword());

        Button cancelButton = new Button("Cancel");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClassName("forgot-password-cancel-button");
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        forgotPasswordLayout.add(forgotPasswordTitle, emailField, submitButton, cancelButton);

        // "Reset Password" layout
        resetPasswordLayout = new VerticalLayout();
        resetPasswordLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        resetPasswordLayout.addClassName("reset-password-layout");

        H3 resetPasswordTitle = new H3("Reset Password");
        resetPasswordTitle.addClassName("reset-password-title");

        newPasswordField = new PasswordField("New Password");
        newPasswordField.addClassName("reset-password-field");

        Button resetButton = new Button("Reset Password");
        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.addClassName("reset-password-button");
        resetButton.addClickListener(e -> handleResetPassword());

        Button resetCancelButton = new Button("Cancel");
        resetCancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetCancelButton.addClassName("reset-password-cancel-button");
        resetCancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("login")));

        resetPasswordLayout.add(resetPasswordTitle, newPasswordField, resetButton, resetCancelButton);
        resetPasswordLayout.setVisible(false); // Initially hide the reset password form

        // Add both layouts to the main layout
        mainLayout.add(forgotPasswordLayout, resetPasswordLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Check for token in the URL
        Optional<String> tokenOptional = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("token", List.of()).stream().findFirst();

        // Validate the token if present
        tokenOptional.ifPresent(this::validateResetToken);
    }

    private void handleForgotPassword() {
        String email = emailField.getValue();
        if (email.isEmpty()) {
            Notification.show("Please enter your email address.", 3000, Notification.Position.TOP_CENTER);
        } else {
            // Generate and save the reset token in memory
            resetToken = emailService.generateResetToken();
            emailService.saveResetTokenToMemory(email, resetToken);
            String resetLink = "http://localhost:8080/forgotpassword?token=" + resetToken;
            emailService.sendEmail(email, "Password Reset Request", resetLink);

            Notification.show("An email has been sent to reset your password.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void validateResetToken(String token) {
        String email = emailService.getEmailByToken(token);
        if (email != null) {
            this.resetToken = token; // Store the token for later use
            toggleToResetPasswordView();
        } else {
            Notification.show("Invalid or expired token.", 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void handleResetPassword() {
        String newPassword = newPasswordField.getValue();
        if (newPassword.isEmpty()) {
            Notification.show("Please enter a new password.", 3000, Notification.Position.TOP_CENTER);
        } else {
            // Retrieve email associated with the token
            String email = emailService.getEmailByToken(resetToken);
            if (email != null) {
                // Implement the logic to update the user's password
                emailService.updatePassword(email, newPassword);
                Notification.show("Your password has been reset successfully.", 3000, Notification.Position.TOP_CENTER);
                // Optionally, navigate to the login page
                getUI().ifPresent(ui -> ui.navigate("login"));
            } else {
                Notification.show("Failed to reset password. Please try again.", 3000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void toggleToResetPasswordView() {
        forgotPasswordLayout.setVisible(false);
        resetPasswordLayout.setVisible(true);
        emailField.clear(); // Clear email field when switching views
    }
}

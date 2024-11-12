package com.studentinfo.views.forgotpassword;

import com.studentinfo.services.EmailService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@PageTitle("Forgot Password / Reset Password")
@Route(value = "forgotpassword", layout = MainLayout.class)
@AnonymousAllowed
@CssImport("./themes/studentinformationapp/views/forgotpassword-view.css")
public class ForgotPasswordView extends Composite<VerticalLayout> implements BeforeEnterObserver, LocaleChangeObserver {

    private final transient EmailService emailService;
    private final transient MessageSource messageSource;

    private static final String LOGIN_ROUTE = "login";
    private EmailField emailField;
    private PasswordField newPasswordField;
    private VerticalLayout forgotPasswordLayout;
    private VerticalLayout resetPasswordLayout;
    private String resetToken = null;

    @Autowired
    public ForgotPasswordView(EmailService emailService, MessageSource messageSource) {
        this.emailService = emailService;
        this.messageSource = messageSource;
        initializeUI();
    }

    @PostConstruct
    public void init() {
        updateTextsBasedOnLocale(LocaleContextHolder.getLocale());
    }

    private void updateTextsBasedOnLocale(Locale locale) {
        // Update labels and titles for components based on locale
        emailField.setLabel(messageSource.getMessage("forgot.email", null, locale));
        newPasswordField.setLabel(messageSource.getMessage("reset.newpassword", null, locale));

        // Correctly update the components in forgot password layout
        if (forgotPasswordLayout != null && forgotPasswordLayout.getComponentCount() > 0) {
            H2 forgotTitle = (H2) forgotPasswordLayout.getComponentAt(1); // Assuming 2nd component is H2
            forgotTitle.setText(messageSource.getMessage("forgot.title", null, locale));

            Paragraph forgotInstructions = (Paragraph) forgotPasswordLayout.getComponentAt(2); // Assuming 3rd component is Paragraph
            forgotInstructions.setText(messageSource.getMessage("forgot.instructions", null, locale));

            // Correctly reference the Button components inside the HorizontalLayout
            HorizontalLayout buttonLayout = (HorizontalLayout) forgotPasswordLayout.getComponentAt(4); // Assuming this is the layout containing buttons
            Button submitButton = (Button) buttonLayout.getComponentAt(0);
            submitButton.setText(messageSource.getMessage("forgot.submit", null, locale));

            Button cancelButton = (Button) buttonLayout.getComponentAt(1);
            cancelButton.setText(messageSource.getMessage("forgot.back", null, locale));
        }

        // Correctly update the components in reset password layout
        if (resetPasswordLayout != null && resetPasswordLayout.getComponentCount() > 0) {
            H2 resetTitle = (H2) resetPasswordLayout.getComponentAt(0); // Assuming first component is H2
            resetTitle.setText(messageSource.getMessage("reset.title", null, locale));

            Button resetButton = (Button) resetPasswordLayout.getComponentAt(2); // Assuming 3rd component is Button
            resetButton.setText(messageSource.getMessage("reset.button", null, locale));

            Button resetCancelButton = (Button) resetPasswordLayout.getComponentAt(3); // Assuming 4th component is Button
            resetCancelButton.setText(messageSource.getMessage("reset.close", null, locale));
        }
    }

    @Override
    public void localeChange(LocaleChangeEvent event) {
        updateTextsBasedOnLocale(event.getLocale());
    }

    private void initializeUI() {
        VerticalLayout mainLayout = getContent();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.addClassName("password-reset-page");

        forgotPasswordLayout = createForgotPasswordLayout();
        resetPasswordLayout = createResetPasswordLayout();
        resetPasswordLayout.setVisible(false);

        mainLayout.add(forgotPasswordLayout, resetPasswordLayout);
    }

    private VerticalLayout createForgotPasswordLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("forgot-password-layout");

        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("forgot-password-bird-image");
        birdImage.setWidth("240px");
        layout.add(birdImage);

        H2 title = new H2(getTranslation("forgot.title"));
        title.addClassName("forgot-password-title");

        Paragraph instructions = new Paragraph(getTranslation("forgot.instructions"));
        instructions.addClassName("forgot-password-instructions");

        emailField = new EmailField(getTranslation("forgot.email"));
        emailField.addClassName("forgot-password-email-field");

        Button submitButton = new Button(getTranslation("forgot.submit"));
        submitButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitButton.addClassName("forgot-password-submit-button");
        submitButton.addClickListener(e -> handleForgotPassword());

        Button cancelButton = new Button(getTranslation("forgot.back"));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClassName("forgot-password-close-button");
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LOGIN_ROUTE)));

        HorizontalLayout buttonLayout = new HorizontalLayout(submitButton, cancelButton);
        buttonLayout.setSpacing(true);

        layout.add(title, instructions, emailField, buttonLayout);
        return layout;
    }

    private VerticalLayout createResetPasswordLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.addClassName("reset-password-layout");

        H2 title = new H2(getTranslation("reset.title"));
        title.addClassName("reset-password-title");

        newPasswordField = new PasswordField(getTranslation("reset.newpassword"));
        newPasswordField.addClassName("reset-password-field");

        Button resetButton = new Button(getTranslation("reset.button"));
        resetButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.addClassName("reset-password-button");
        resetButton.addClickListener(e -> handleResetPassword());

        Button resetCancelButton = new Button(getTranslation("reset.close"));
        resetCancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        resetCancelButton.addClassName("reset-password-close-button");
        resetCancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LOGIN_ROUTE)));

        layout.add(title, newPasswordField, resetButton, resetCancelButton);
        return layout;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> tokenOptional = event.getLocation().getQueryParameters().getParameters()
                .getOrDefault("token", List.of()).stream().findFirst();
        tokenOptional.ifPresent(this::validateResetToken);
    }

    private String getTranslation(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private void handleForgotPassword() {
        String email = emailField.getValue();
        if (email.isEmpty()) {
            Notification.show(getTranslation("email.notification.failure"), 3000, Notification.Position.TOP_CENTER);
        } else {
            resetToken = emailService.generateResetToken();
            emailService.saveResetTokenToMemory(email, resetToken);
            String resetLink = "http://localhost:8080/forgotpassword?token=" + resetToken;
            emailService.sendEmail(email, "Password Reset Request", resetLink);
            Notification.show(getTranslation("email.notification.success"), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void validateResetToken(String token) {
        String email = emailService.getEmailByToken(token);
        if (email != null) {
            this.resetToken = token;
            toggleToResetPasswordView();
        } else {
            Notification.show(getTranslation("reset.token.invalid"), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void handleResetPassword() {
        String newPassword = newPasswordField.getValue();
        if (newPassword.isEmpty()) {
            Notification.show(getTranslation("reset.newpassword.empty"), 3000, Notification.Position.TOP_CENTER);
        } else {
            String email = emailService.getEmailByToken(resetToken);
            if (email != null) {
                emailService.updatePassword(email, newPassword);
                Notification.show(getTranslation("reset.notification.success"), 3000, Notification.Position.TOP_CENTER);
                getUI().ifPresent(ui -> ui.navigate(LOGIN_ROUTE));
            } else {
                Notification.show(getTranslation("reset.notification.failure"), 3000, Notification.Position.TOP_CENTER);
            }
        }
    }

    private void toggleToResetPasswordView() {
        forgotPasswordLayout.setVisible(false);
        resetPasswordLayout.setVisible(true);
        emailField.clear();
    }
}

package com.studentinfo.views.loginpage;

import com.studentinfo.services.LoginHandler;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import jakarta.servlet.http.Cookie;

@PageTitle("Login Page")
@Route(value = "login")
@AnonymousAllowed
public class LoginPageView extends Composite<VerticalLayout> {

    private final TextField emailField;
    private final PasswordField passwordField;
    private final Button signInButton;
    private final Button signUpButton;
    private final Button forgotPasswordButton;
    private final Checkbox rememberMeCheckbox;

    @Autowired
    public LoginPageView(LoginHandler loginHandler, MessageSource messageSource) {
        // Internationalized text fields and buttons
        this.emailField = new TextField(
                messageSource.getMessage("login.email", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("login.email.placeholder", null, LocaleContextHolder.getLocale())
        );
        this.emailField.addClassName("login-text-field");

        this.passwordField = new PasswordField(
                messageSource.getMessage("login.password", null, LocaleContextHolder.getLocale()),
                messageSource.getMessage("login.password.placeholder", null, LocaleContextHolder.getLocale())
        );
        this.passwordField.addClassName("login-text-field");

        this.signInButton = new Button(messageSource.getMessage("login.signin", null, LocaleContextHolder.getLocale()));
        this.signInButton.addClassName("login-signin-button");

        this.signUpButton = new Button(messageSource.getMessage("login.signup", null, LocaleContextHolder.getLocale()));
        this.signUpButton.addClassName("login-signup-link");

        this.forgotPasswordButton = new Button(messageSource.getMessage("login.forgotPassword", null, LocaleContextHolder.getLocale()));
        this.forgotPasswordButton.addClassName("login-forgot-password-link");
        this.forgotPasswordButton.addClickListener(e -> UI.getCurrent().navigate("forgotpassword"));

        this.rememberMeCheckbox = new Checkbox(messageSource.getMessage("login.rememberMe", null, LocaleContextHolder.getLocale()));
        this.rememberMeCheckbox.addClassName("login-remember-checkbox");
        this.rememberMeCheckbox.setId("remember-me");

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.addClassName("login-view-page");

        // Add the reusable Header component with MessageSource
        HeaderView header = new HeaderView(messageSource);
        header.setWidthFull();
        mainLayout.add(header);

        // Add a spacer to ensure content starts below the header
        mainLayout.getStyle().set("padding-top", "60px");

        // Content layout setup (below the header)
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidthFull();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);
        contentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        contentLayout.addClassName("login-content-layout");

        // Setup left and right content
        contentLayout.add(setupLeftContent(messageSource), setupRightContent());
        contentLayout.setFlexGrow(1);

        // Add content layout to main layout
        mainLayout.addAndExpand(contentLayout);

        // Set up click listener for sign-in button
        signInButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            boolean rememberMe = rememberMeCheckbox.getValue();

            if (email.isEmpty()) {
                Notification.show(messageSource.getMessage("login.email.empty", null, LocaleContextHolder.getLocale()), 3000, Notification.Position.MIDDLE);
                return;
            }

            if (password.isEmpty()) {
                Notification.show(messageSource.getMessage("login.password.empty", null, LocaleContextHolder.getLocale()), 3000, Notification.Position.MIDDLE);
                return;
            }

            // Attempt to log in
            boolean loginSuccessful = loginHandler.login(email, password, rememberMe);

            // Check login success
            if (!loginSuccessful) {
                Notification.show(messageSource.getMessage("login.invalid", null, LocaleContextHolder.getLocale()), 3000, Notification.Position.MIDDLE);
            } else {
                Notification.show(messageSource.getMessage("login.success", null, LocaleContextHolder.getLocale()), 3000, Notification.Position.MIDDLE);
            }
        });

        // Load the email from cookie if it exists
        loadRememberMeCookie();

        signUpButton.addClickListener(e -> UI.getCurrent().navigate("register"));
    }

    private VerticalLayout setupLeftContent(MessageSource messageSource) {
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setSizeFull();
        leftContent.setPadding(false);
        leftContent.setSpacing(false);
        leftContent.setAlignItems(FlexComponent.Alignment.CENTER);
        leftContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        leftContent.addClassName("login-left-content");

        // Add components with translated text
        Span welcomeText = new Span(messageSource.getMessage("login.welcome", null, LocaleContextHolder.getLocale()));
        welcomeText.addClassName("login-welcome-text");

        Span instructionText = new Span(messageSource.getMessage("login.instruction", null, LocaleContextHolder.getLocale()));
        instructionText.addClassName("login-instruction-text");

        Span signUpText = new Span(messageSource.getMessage("login.noAccount", null, LocaleContextHolder.getLocale()));
        signUpText.addClassName("login-signup-text");

        // Add the "Forgot Password?" button under the password field
        leftContent.add(welcomeText, instructionText, emailField, passwordField, forgotPasswordButton, rememberMeCheckbox, signInButton, signUpText, signUpButton);

        return leftContent;
    }

    private VerticalLayout setupRightContent() {
        VerticalLayout rightContent = new VerticalLayout();
        rightContent.setSizeFull();
        rightContent.setPadding(false);
        rightContent.setSpacing(false);
        rightContent.setAlignItems(FlexComponent.Alignment.CENTER);
        rightContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rightContent.addClassName("login-right-content");

        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("login-bird-image");
        rightContent.add(birdImage);

        return rightContent;
    }

    // Load the "Remember me" cookie
    private void loadRememberMeCookie() {
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("rememberMe".equals(cookie.getName())) {
                    emailField.setValue(cookie.getValue()); // Set the email from the cookie
                    rememberMeCheckbox.setValue(true); // Check the "Remember me" checkbox
                    break;
                }
            }
        }
    }
}

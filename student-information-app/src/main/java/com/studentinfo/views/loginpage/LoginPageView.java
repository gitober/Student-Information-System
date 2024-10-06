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
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.Cookie;

@PageTitle("Login Page")
@Route(value = "login")
@AnonymousAllowed
public class LoginPageView extends Composite<VerticalLayout> {

    private final TextField emailField;
    private final PasswordField passwordField;
    private final Button signInButton;
    private final Button signUpButton;
    private final Checkbox rememberMeCheckbox;

    @Autowired
    public LoginPageView(LoginHandler loginHandler) {
        this.emailField = new TextField("Email", "Enter your email");
        this.emailField.addClassName("login-text-field");

        this.passwordField = new PasswordField("Password", "Enter password");
        this.passwordField.addClassName("login-text-field");

        this.signInButton = new Button("Sign in");
        this.signInButton.addClassName("login-signin-button");

        this.signUpButton = new Button("Signup");
        this.signUpButton.addClassName("login-signup-link");

        this.rememberMeCheckbox = new Checkbox("Remember me");
        this.rememberMeCheckbox.addClassName("login-remember-checkbox");
        this.rememberMeCheckbox.setId("remember-me"); // Set the checkbox ID for Spring Security recognition

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.addClassName("login-view-page");

        // Add the reusable Header component first
        HeaderView header = new HeaderView("EduBird");
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
        contentLayout.add(setupLeftContent(), setupRightContent());
        contentLayout.setFlexGrow(1);

        // Add content layout to main layout
        mainLayout.addAndExpand(contentLayout);

        // Set up click listener for sign-in button
        signInButton.addClickListener(e -> {
            String email = emailField.getValue();
            String password = passwordField.getValue();
            boolean rememberMe = rememberMeCheckbox.getValue(); // Get the state of the "Remember me" checkbox

            // Validate input fields
            if (email.isEmpty()) {
                Notification.show("Please enter your email.", 3000, Notification.Position.MIDDLE);
                return; // Stop further execution if email is empty
            }

            if (password.isEmpty()) {
                Notification.show("Please enter your password.", 3000, Notification.Position.MIDDLE);
                return; // Stop further execution if password is empty
            }

            // Call the login handler
            loginHandler.login(email, password, rememberMe); // Pass the rememberMe state to the loginHandler
        });

        // Load the email from cookie if it exists
        loadRememberMeCookie();

        signUpButton.addClickListener(e -> UI.getCurrent().navigate("register"));

    }

    private VerticalLayout setupLeftContent() {
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setSizeFull();
        leftContent.setPadding(false);
        leftContent.setSpacing(false);
        leftContent.setAlignItems(FlexComponent.Alignment.CENTER);
        leftContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        leftContent.addClassName("login-left-content");

        // Add components
        Span welcomeText = new Span("Welcome");
        welcomeText.addClassName("login-welcome-text");
        Span instructionText = new Span("Please enter your details");
        instructionText.addClassName("login-instruction-text");

        Span signUpText = new Span("Don’t have an account yet?");
        signUpText.addClassName("login-signup-text");

        leftContent.add(welcomeText, instructionText, emailField, passwordField, rememberMeCheckbox, signInButton, signUpText, signUpButton);
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
                    emailField.setValue(cookie.getValue());
                    rememberMeCheckbox.setValue(true);
                    break;
                }
            }
        }
    }
}

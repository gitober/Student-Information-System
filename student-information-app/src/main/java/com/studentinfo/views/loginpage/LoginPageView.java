package com.studentinfo.views.loginpage;

import com.studentinfo.views.mainlayout.MainLayout;
import com.studentinfo.services.LoginHandler;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Login Page")
@Route(value = "login", layout = MainLayout.class)
@AnonymousAllowed
public class LoginPageView extends Composite<VerticalLayout> {

    private final TextField emailField;
    private final PasswordField passwordField;
    private final LoginHandler loginHandler;

    @Autowired
    public LoginPageView(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;

        // Content layout setup
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        contentLayout.addClassName("content-layout");

        // Left content setup
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.addClassName("left-content");

        // Welcome Text
        Span welcomeText = new Span("Welcome");
        welcomeText.addClassName("welcome-text");

        // Instruction Text
        Span instructionText = new Span("Please enter your details");
        instructionText.addClassName("instruction-text");

        // Email field
        emailField = new TextField();
        emailField.setLabel("Email");
        emailField.setPlaceholder("Enter your email");
        emailField.addClassName("input-field");

        // Password field
        passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Enter password");
        passwordField.addClassName("input-field");

        // Remember me checkbox
        Checkbox rememberMeCheckbox = new Checkbox("Remember me");
        rememberMeCheckbox.addClassName("remember-checkbox");

        // Sign in button
        Button signInButton = new Button("Sign in");
        signInButton.addClassName("signin-button");
        signInButton.addClickListener(e -> loginHandler.login(emailField.getValue(), passwordField.getValue()));

        // Signup text and link
        Span signupText = new Span("Don’t have an account yet? ");
        signupText.addClassName("signup-text");

        Button signupButton = new Button("Signup");
        signupButton.addClassName("signup-link");
        signupButton.addClickListener(e -> UI.getCurrent().navigate("register"));

        // Adding components to left content
        leftContent.add(welcomeText, instructionText, emailField, passwordField, rememberMeCheckbox, signInButton, signupText, signupButton);

        // Right content setup
        VerticalLayout rightContent = new VerticalLayout();
        rightContent.addClassName("right-content");

        // Bird Image
        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("bird-image");

        rightContent.add(birdImage);

        // Add left and right content to content layout
        contentLayout.add(leftContent, rightContent);

        // Add the content layout to the main view
        getContent().add(contentLayout);
    }
}

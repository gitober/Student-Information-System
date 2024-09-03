package com.studentinfo.views.loginpage;

import com.studentinfo.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login Page")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "")
@AnonymousAllowed

public class LoginPageView extends Composite<VerticalLayout> {

    public LoginPageView() {
        // Main layout
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.addClassName("main-container");

        // Header
        Div header = new Div();
        header.addClassName("header");
        Span appName = new Span("EduBird");
        appName.addClassName("app-name");
        header.add(appName);

        // Content layout
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();
        contentLayout.addClassName("content-layout");

        // Left content
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.addClassName("left-content");

        // Welcome Text
        Span welcomeText = new Span("Welcome");
        welcomeText.addClassName("welcome-text");

        // Instruction Text
        Span instructionText = new Span("please enter your details");
        instructionText.addClassName("instruction-text");

        // Email field
        TextField emailField = new TextField();
        emailField.setLabel("Email");
        emailField.setPlaceholder("Enter your email");
        emailField.addClassName("input-field");

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Password");
        passwordField.setPlaceholder("Enter password");
        passwordField.addClassName("input-field");

        // Remember me checkbox
        Checkbox rememberMeCheckbox = new Checkbox("remember me");
        rememberMeCheckbox.addClassName("remember-checkbox");

        // Sign in button
        Button signInButton = new Button("Sign in");
        signInButton.addClassName("signin-button");
        signInButton.addClickListener(e -> {
            // Add logic for sign-in action here
            System.out.println("Sign in button clicked");
        });

        // Signup text and link
        Span signupText = new Span("Don’t have an account yet? ");
        signupText.addClassName("signup-text");

        Button signupButton = new Button("Signup");
        signupButton.addClassName("signup-link");
        signupButton.addClickListener(e -> {
            // Add logic for signup action here
            System.out.println("Signup button clicked");
        });

        // Adding components to left content
        leftContent.add(welcomeText, instructionText, emailField, passwordField, rememberMeCheckbox, signInButton, signupText, signupButton);

        // Right content
        VerticalLayout rightContent = new VerticalLayout();
        rightContent.addClassName("right-content");

        // Bird Image
        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("bird-image");

        rightContent.add(birdImage);

        // Add left and right content to content layout
        contentLayout.add(leftContent, rightContent);

        // Add header and content layout to main layout
        mainLayout.add(header, contentLayout);

        // Set the content
        getContent().add(mainLayout);
    }
}

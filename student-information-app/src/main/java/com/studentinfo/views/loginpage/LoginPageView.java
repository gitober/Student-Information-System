package com.studentinfo.views.loginpage;

import com.studentinfo.services.LoginHandler;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Login Page")
@Route(value = "login")
@AnonymousAllowed
public class LoginPageView extends Composite<VerticalLayout> {

    private final TextField emailField = new TextField("Email", "Enter your email");
    private final PasswordField passwordField = new PasswordField("Password", "Enter password");
    private final LoginHandler loginHandler;

    @Autowired
    public LoginPageView(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.addClassName("login-page");

        // Add the simplified Header component without authentication links
        mainLayout.add(new HeaderView("Login Page"));

        // Content layout setup
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setSizeFull();

        // Configure the fields
        configureFields();

        // Layouts for content
        VerticalLayout leftContent = setupLeftContent();
        VerticalLayout rightContent = setupRightContent();

        // Add left and right content to content layout
        contentLayout.add(leftContent, rightContent);
        contentLayout.setFlexGrow(1, leftContent);
        contentLayout.setFlexGrow(1, rightContent);

        // Add the content layout to the main layout
        mainLayout.add(contentLayout);
    }

    private void configureFields() {
        emailField.addClassName("input-field");
        emailField.getElement().getStyle().set("box-sizing", "border-box");
        passwordField.addClassName("input-field");
        passwordField.getElement().getStyle().set("box-sizing", "border-box");
    }

    private VerticalLayout setupLeftContent() {
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setSizeFull();
        leftContent.addClassName("left-content");
        leftContent.setAlignItems(FlexComponent.Alignment.CENTER);

        // Components setup
        Span welcomeText = new Span("Welcome");
        welcomeText.addClassName("welcome-text");
        Span instructionText = new Span("Please enter your details");
        instructionText.addClassName("instruction-text");

        Checkbox rememberMeCheckbox = new Checkbox("Remember me");
        rememberMeCheckbox.addClassName("remember-checkbox");

        Button signInButton = new Button("Sign in");
        signInButton.addClassName("signin-button");
        // Updated call to match login method
        signInButton.addClickListener(e -> loginHandler.login(emailField.getValue(), passwordField.getValue()));

        Span signupText = new Span("Don’t have an account yet? ");
        signupText.addClassName("signup-text");
        Button signupButton = new Button("Signup");
        signupButton.addClassName("signup-link");
        signupButton.addClickListener(e -> UI.getCurrent().navigate("register"));

        leftContent.add(welcomeText, instructionText, emailField, passwordField, rememberMeCheckbox, signInButton, signupText, signupButton);

        return leftContent;
    }

    private VerticalLayout setupRightContent() {
        VerticalLayout rightContent = new VerticalLayout();
        rightContent.setSizeFull();
        rightContent.addClassName("right-content");

        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("bird-image");
        rightContent.add(birdImage);

        return rightContent;
    }
}

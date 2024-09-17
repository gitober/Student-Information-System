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
        mainLayout.setWidthFull();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.addClassName("login-page");

        // Add the reusable Header component first
        HeaderView header = new HeaderView("EduBird");
        header.setWidthFull();
        mainLayout.add(header);

        // Add a spacer to ensure content starts below the header
        mainLayout.getStyle().set("padding-top", "60px"); // Dynamically set padding to match the header height

        // Content layout setup (below the header)
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidthFull();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);
        contentLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        contentLayout.addClassName("content-layout");

        // Setup left and right content
        contentLayout.add(setupLeftContent(), setupRightContent());
        contentLayout.setFlexGrow(1);

        // Add content layout to main layout
        mainLayout.addAndExpand(contentLayout);

    }

    private VerticalLayout setupLeftContent() {
        VerticalLayout leftContent = new VerticalLayout();
        leftContent.setSizeFull();
        leftContent.setPadding(false);
        leftContent.setSpacing(false);
        leftContent.setAlignItems(FlexComponent.Alignment.CENTER);
        leftContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        leftContent.addClassName("left-content");

        // Add components
        Span welcomeText = new Span("Welcome");
        welcomeText.addClassName("welcome-text");
        Span instructionText = new Span("Please enter your details");
        instructionText.addClassName("instruction-text");

        Checkbox rememberMeCheckbox = new Checkbox("Remember me");
        rememberMeCheckbox.addClassName("remember-checkbox");

        Button signInButton = new Button("Sign in");
        signInButton.addClassName("signin-button");
        signInButton.addClickListener(e -> loginHandler.login(emailField.getValue(), passwordField.getValue()));

        Span signupText = new Span("Don’t have an account yet?");
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
        rightContent.setPadding(false);
        rightContent.setSpacing(false);
        rightContent.setAlignItems(FlexComponent.Alignment.CENTER);
        rightContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        rightContent.addClassName("right-content");

        Image birdImage = new Image("images/bird.png", "Colorful Bird");
        birdImage.addClassName("bird-image");
        rightContent.add(birdImage);

        return rightContent;
    }
}
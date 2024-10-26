package com.studentinfo.views.loginpage;

import com.studentinfo.services.LoginHandler;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginPageViewTest {

    private LoginPageView loginPageView;
    private LoginHandler loginHandler;
    private MockedStatic<VaadinService> vaadinServiceMock;

    @BeforeEach
    public void setUp() {
        // Mock the LoginHandler and MessageSource
        loginHandler = Mockito.mock(LoginHandler.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        // Mock the message source to return expected labels
        when(messageSource.getMessage("login.email", null, LocaleContextHolder.getLocale())).thenReturn("Email");
        when(messageSource.getMessage("login.password", null, LocaleContextHolder.getLocale())).thenReturn("Password");
        when(messageSource.getMessage("login.signin", null, LocaleContextHolder.getLocale())).thenReturn("Sign in");
        when(messageSource.getMessage("login.signup", null, LocaleContextHolder.getLocale())).thenReturn("Signup");
        when(messageSource.getMessage("login.forgotPassword", null, LocaleContextHolder.getLocale())).thenReturn("Forgot Password?");
        when(messageSource.getMessage("login.rememberMe", null, LocaleContextHolder.getLocale())).thenReturn("Remember me");

        // Set up the UI context for Vaadin components
        UI ui = new UI();
        UI.setCurrent(ui);

        // Mock VaadinService, VaadinRequest, and VaadinResponse
        VaadinRequest vaadinRequest = mock(VaadinRequest.class);
        VaadinResponse vaadinResponse = mock(VaadinResponse.class);
        VaadinSession vaadinSession = mock(VaadinSession.class);
        VaadinService vaadinService = mock(VaadinService.class);
        Router router = mock(Router.class);

        vaadinServiceMock = Mockito.mockStatic(VaadinService.class);
        vaadinServiceMock.when(VaadinService::getCurrentRequest).thenReturn(vaadinRequest);
        vaadinServiceMock.when(VaadinService::getCurrentResponse).thenReturn(vaadinResponse);
        vaadinServiceMock.when(VaadinService::getCurrent).thenReturn(vaadinService);

        // Simulate the presence of a "rememberMe" cookie
        Cookie[] cookies = { new Cookie("rememberMe", "test@example.com") };
        when(vaadinRequest.getCookies()).thenReturn(cookies);

        // Set the session and router for the UI
        when(vaadinSession.getService()).thenReturn(vaadinService);
        when(vaadinService.getRouter()).thenReturn(router);
        ui.getInternals().setSession(vaadinSession);

        // Instantiate the LoginPageView with the mocked login handler and message source
        loginPageView = new LoginPageView(loginHandler, messageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear the UI context to avoid affecting other tests
        UI.setCurrent(null);

        // Clear any other references to avoid memory leaks
        loginPageView = null;
        loginHandler = null;

        // Close the static mock
        vaadinServiceMock.close();
    }

    @Test
    public void testLoginPageComponents() {
        // Check if the email field is present
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        assertNotNull(emailField, "Email field should be present in the login page.");

        // Check if the password field is present
        PasswordField passwordField = findComponentOfType(loginPageView.getContent(), PasswordField.class);
        assertNotNull(passwordField, "Password field should be present in the login page.");

        // Check if the sign-in button is present
        Button signInButton = findButtonByText(loginPageView.getContent(), "Sign in");
        assertNotNull(signInButton, "Sign in button should be present in the login page.");

        // Check if the sign-up button is present
        Button signUpButton = findButtonByText(loginPageView.getContent(), "Signup");
        assertNotNull(signUpButton, "Sign up button should be present in the login page.");

        // Check if the header is present
        Span header = findComponentOfType(loginPageView.getContent(), Span.class);
        assertNotNull(header, "Header should be present in the login page.");

        // Check if the bird image is present
        Image birdImage = findComponentOfType(loginPageView.getContent(), Image.class);
        assertNotNull(birdImage, "Bird image should be present in the login page.");

        // Check if the "Remember me" checkbox is present
        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);
        assertNotNull(rememberMeCheckbox, "Remember me checkbox should be present in the login page.");
    }

    @Test
    public void testLoginButtonFunctionality() {
        // Find the email and password fields
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        PasswordField passwordField = findComponentOfType(loginPageView.getContent(), PasswordField.class);
        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);

        // Set values for email and password
        assert emailField != null;
        emailField.setValue("test@example.com");
        assert passwordField != null;
        passwordField.setValue("password123");
        assert rememberMeCheckbox != null;
        rememberMeCheckbox.setValue(true);

        // Find the sign-in button and click it
        Button signInButton = findButtonByText(loginPageView.getContent(), "Sign in");
        assert signInButton != null;
        signInButton.click();

        // Verify that the login method in LoginHandler was called with the correct arguments
        verify(loginHandler, times(1)).login(eq("test@example.com"), eq("password123"), eq(true));
    }

    @Test
    public void testForgotPasswordButtonFunctionality() {
        // Mock the UI class
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        // Find the "Forgot Password?" button
        Button forgotPasswordButton = findButtonByText(loginPageView.getContent(), "Forgot Password?");
        assertNotNull(forgotPasswordButton, "Forgot Password button should be present in the login page.");

        // Simulate clicking the button
        forgotPasswordButton.click();

        // Verify that the navigation to the "forgotpassword" route was triggered
        verify(mockUI).navigate("forgotpassword");
    }

    @Test
    public void testRememberMeCookie() {
        // Verify that the email was loaded from the cookie
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        assert emailField != null;
        assertEquals("test@example.com", emailField.getValue(), "Email should be pre-filled from cookie.");

        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);
        assert rememberMeCheckbox != null;
        assertTrue(rememberMeCheckbox.getValue(), "Remember me checkbox should be selected if cookie exists.");
    }

    // Helper method to find a component of a given type in the layout's hierarchy
    private <T extends Component> T findComponentOfType(Component root, Class<T> componentType) {
        if (componentType.isInstance(root)) {
            return componentType.cast(root);
        }
        for (Component child : root.getChildren().toList()) {
            T found = findComponentOfType(child, componentType);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    // Helper method to find a button by its text in the layout's hierarchy
    private Button findButtonByText(Component root, String text) {
        if (root instanceof Button && text.equals(((Button) root).getText())) {
            return (Button) root;
        }
        for (Component child : root.getChildren().toList()) {
            Button found = findButtonByText(child, text);
            if (found != null) {
                return found;
            }
        }
        return null;
    }
}

package com.studentinfo.views.loginpage;

import com.studentinfo.services.LoginHandler;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginPageViewTest {

    private LoginPageView loginPageView;
    private LoginHandler loginHandler;
    private MockedStatic<VaadinService> vaadinServiceMock;

    @BeforeEach
    public void setUp() {
        loginHandler = Mockito.mock(LoginHandler.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        // Mocking translations based on your provided properties
        when(messageSource.getMessage("login.email", null, "Email", LocaleContextHolder.getLocale())).thenReturn("Email");
        when(messageSource.getMessage("login.email.placeholder", null, "Enter your email", LocaleContextHolder.getLocale())).thenReturn("Enter your email");
        when(messageSource.getMessage("login.password", null, "Password", LocaleContextHolder.getLocale())).thenReturn("Password");
        when(messageSource.getMessage("login.password.placeholder", null, "Enter your password", LocaleContextHolder.getLocale())).thenReturn("Enter your password");
        when(messageSource.getMessage("login.signin", null, "Sign In", LocaleContextHolder.getLocale())).thenReturn("Sign In");
        when(messageSource.getMessage("login.signup", null, "Sign Up", LocaleContextHolder.getLocale())).thenReturn("Sign Up");
        when(messageSource.getMessage("login.forgotPassword", null, "Forgot Your Password?", LocaleContextHolder.getLocale())).thenReturn("Forgot Your Password?");
        when(messageSource.getMessage("login.rememberMe", null, "Remember me", LocaleContextHolder.getLocale())).thenReturn("Remember me");

        UI ui = new UI();
        UI.setCurrent(ui);

        VaadinRequest vaadinRequest = mock(VaadinRequest.class);
        VaadinResponse vaadinResponse = mock(VaadinResponse.class);
        VaadinSession vaadinSession = mock(VaadinSession.class);
        VaadinService vaadinService = mock(VaadinService.class);

        vaadinServiceMock = Mockito.mockStatic(VaadinService.class);
        vaadinServiceMock.when(VaadinService::getCurrentRequest).thenReturn(vaadinRequest);
        vaadinServiceMock.when(VaadinService::getCurrentResponse).thenReturn(vaadinResponse);
        vaadinServiceMock.when(VaadinService::getCurrent).thenReturn(vaadinService);

        Cookie[] cookies = {new Cookie("rememberMe", "test@example.com")};
        when(vaadinRequest.getCookies()).thenReturn(cookies);

        when(vaadinSession.getService()).thenReturn(vaadinService);
        ui.getInternals().setSession(vaadinSession);

        loginPageView = new LoginPageView(loginHandler, messageSource);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
        loginPageView = null;
        loginHandler = null;
        vaadinServiceMock.close();
    }

    @Test
    void testLoginPageComponents() {
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        assertNotNull(emailField, "Email field should be present in the login page.");
        assertEquals("Enter your email", emailField.getPlaceholder(), "Email field should have the correct placeholder.");

        PasswordField passwordField = findComponentOfType(loginPageView.getContent(), PasswordField.class);
        assertNotNull(passwordField, "Password field should be present in the login page.");
        assertEquals("Enter your password", passwordField.getPlaceholder(), "Password field should have the correct placeholder.");

        Button signInButton = findButtonByText(loginPageView.getContent(), "Sign In");
        assertNotNull(signInButton, "Sign in button should be present in the login page.");

        Button signUpButton = findButtonByText(loginPageView.getContent(), "Sign Up");
        assertNotNull(signUpButton, "Sign up button should be present in the login page.");

        Button forgotPasswordButton = findButtonByText(loginPageView.getContent(), "Forgot Your Password?");
        assertNotNull(forgotPasswordButton, "Forgot Password button should be present in the login page.");

        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);
        assertNotNull(rememberMeCheckbox, "Remember me checkbox should be present in the login page.");
    }

    @Test
    void testLoginButtonFunctionality() {
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        PasswordField passwordField = findComponentOfType(loginPageView.getContent(), PasswordField.class);
        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);

        assertNotNull(emailField);
        emailField.setValue("test@example.com");
        assertNotNull(passwordField);
        passwordField.setValue("password123");
        assertNotNull(rememberMeCheckbox);
        rememberMeCheckbox.setValue(true);

        Button signInButton = findButtonByText(loginPageView.getContent(), "Sign In");
        assertNotNull(signInButton);
        signInButton.click();

        verify(loginHandler, times(1)).login("test@example.com", "password123", true);
    }

    @Test
    void testForgotPasswordButtonFunctionality() {
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        Button forgotPasswordButton = findButtonByText(loginPageView.getContent(), "Forgot Your Password?");
        assertNotNull(forgotPasswordButton);

        forgotPasswordButton.click();

        verify(mockUI).navigate("forgotpassword");
    }

    @Test
    void testRememberMeCookie() {
        TextField emailField = findComponentOfType(loginPageView.getContent(), TextField.class);
        assertNotNull(emailField);
        assertEquals("test@example.com", emailField.getValue(), "Email should be pre-filled from the cookie.");

        Checkbox rememberMeCheckbox = findComponentOfType(loginPageView.getContent(), Checkbox.class);
        assertNotNull(rememberMeCheckbox);
        assertTrue(rememberMeCheckbox.getValue(), "Remember me checkbox should be selected if cookie exists.");
    }

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

    private Button findButtonByText(Component root, String text) {
        if (root instanceof Button button && text.equals(button.getText())) {
                return button;
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
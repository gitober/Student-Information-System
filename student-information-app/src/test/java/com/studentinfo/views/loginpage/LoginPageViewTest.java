package com.studentinfo.views.loginpage;

import com.studentinfo.services.LoginHandler;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockedStatic;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginPageViewTest {

    private LoginPageView loginPageView;
    private LoginHandler loginHandler;

    @BeforeEach
    public void setUp() {
        // Mock the LoginHandler
        loginHandler = Mockito.mock(LoginHandler.class);

        // Set up the UI context for Vaadin components
        UI.setCurrent(new UI());

        // Mock VaadinService, VaadinRequest, and VaadinResponse
        VaadinRequest vaadinRequest = mock(VaadinRequest.class);
        VaadinResponse vaadinResponse = mock(VaadinResponse.class);

        try (MockedStatic<VaadinService> vaadinServiceMock = Mockito.mockStatic(VaadinService.class)) {
            vaadinServiceMock.when(VaadinService::getCurrentRequest).thenReturn(vaadinRequest);
            vaadinServiceMock.when(VaadinService::getCurrentResponse).thenReturn(vaadinResponse);

            // Instantiate the LoginPageView with the mocked login handler
            loginPageView = new LoginPageView(loginHandler);
        }
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
        emailField.setValue("test@example.com");
        passwordField.setValue("password123");
        rememberMeCheckbox.setValue(true);

        // Find the sign-in button and click it
        Button signInButton = findButtonByText(loginPageView.getContent(), "Sign in");
        signInButton.click();

        // Verify that the login method in LoginHandler was called with the correct arguments
        verify(loginHandler, times(1)).login(eq("test@example.com"), eq("password123"), eq(true));
    }

    // Helper method to find a component of a given type in the layout's hierarchy
    private <T extends Component> T findComponentOfType(Component root, Class<T> componentType) {
        if (componentType.isInstance(root)) {
            return componentType.cast(root);
        }
        Optional<T> childResult = root.getChildren()
                .map(child -> findComponentOfType(child, componentType))
                .filter(componentType::isInstance)
                .map(componentType::cast)
                .findFirst();
        return childResult.orElse(null);
    }

    // Helper method to find a button by its text in the layout's hierarchy
    private Button findButtonByText(Component root, String text) {
        if (root instanceof Button && text.equals(((Button) root).getText())) {
            return (Button) root;
        }
        Optional<Button> childResult = root.getChildren()
                .map(child -> findButtonByText(child, text))
                .filter(Objects::nonNull)
                .map(component -> (Button) component)
                .findFirst();
        return childResult.orElse(null);
    }
}

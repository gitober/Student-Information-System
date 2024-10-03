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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginPageViewTest {

    private LoginPageView loginPageView;

    @BeforeEach
    public void setUp() {
        // Mock the LoginHandler
        LoginHandler loginHandler = Mockito.mock(LoginHandler.class);

        // Set up the UI context for Vaadin components
        UI.setCurrent(new UI());

        // Instantiate the LoginPageView with the mocked login handler
        loginPageView = new LoginPageView(loginHandler);
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

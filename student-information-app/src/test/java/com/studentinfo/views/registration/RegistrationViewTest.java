package com.studentinfo.views.registration;

import com.studentinfo.services.RegistrationHandler;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class RegistrationViewTest {

    private RegistrationView registrationView;

    @BeforeEach
    public void setUp() {
        // Mock the RegistrationHandler and MessageSource
        RegistrationHandler mockRegistrationHandler = Mockito.mock(RegistrationHandler.class);
        MessageSource mockMessageSource = Mockito.mock(MessageSource.class);

        // Set up the UI context for Vaadin
        UI ui = new UI();
        UI.setCurrent(ui);

        // Create a mock VaadinSession and attach it to the UI
        VaadinSession mockSession = mock(VaadinSession.class);
        VaadinSession.setCurrent(mockSession);
        ui.getInternals().setSession(mockSession);

        // Instantiate the RegistrationView with mocks
        registrationView = new RegistrationView(mockRegistrationHandler, mockMessageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear the Vaadin UI and session context
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);

        // Clear the registrationView reference
        registrationView = null;
    }

    @Test
    public void testRegistrationViewComponents() {
        // Check if the email field is present
        EmailField emailField = findComponent(EmailField.class, "Email");
        assertNotNull(emailField, "Email field should be present in the registration view.");

        // Check if the password fields are present
        PasswordField passwordField = findComponent(PasswordField.class, "Create Password");
        PasswordField confirmPasswordField = findComponent(PasswordField.class, "Confirm Password");
        assertNotNull(passwordField, "Password field should be present in the registration view.");
        assertNotNull(confirmPasswordField, "Confirm password field should be present in the registration view.");

        // Check if the register button is present
        Button registerButton = findComponent(Button.class, "Register");
        assertNotNull(registerButton, "Register button should be present in the registration view.");
    }

    // Recursive utility method to find components by class type and label text
    private <T> T findComponent(Class<T> componentClass, String labelText) {
        return findComponentInTree(registrationView.getContent(), componentClass, labelText);
    }

    private <T> T findComponentInTree(com.vaadin.flow.component.Component component, Class<T> componentClass, String labelText) {
        if (componentClass.isInstance(component)) {
            switch (component) {
                case TextField textField when labelText.equals(textField.getLabel()) -> {
                    return componentClass.cast(component);
                }
                case PasswordField passwordField when labelText.equals(passwordField.getLabel()) -> {
                    return componentClass.cast(component);
                }
                case Button button when labelText.equals(button.getText()) -> {
                    return componentClass.cast(component);
                }
                case EmailField emailField when labelText.equals(emailField.getLabel()) -> {
                    return componentClass.cast(component);
                }
                default -> {
                }
            }
        }

        // Recursively search through the children of the current component
        Optional<T> foundComponent = component.getChildren()
                .map(child -> findComponentInTree(child, componentClass, labelText))
                .filter(java.util.Objects::nonNull)
                .findFirst();

        return foundComponent.orElse(null);
    }
}

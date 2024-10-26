package com.studentinfo.views.registration;

import com.studentinfo.services.RegistrationHandler;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
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
        RegistrationHandler mockRegistrationHandler = Mockito.mock(RegistrationHandler.class);
        MessageSource mockMessageSource = Mockito.mock(MessageSource.class);

        UI ui = new UI();
        UI.setCurrent(ui);

        VaadinSession mockSession = mock(VaadinSession.class);
        VaadinSession.setCurrent(mockSession);
        ui.getInternals().setSession(mockSession);

        registrationView = new RegistrationView(mockRegistrationHandler, mockMessageSource);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
        registrationView = null;
    }

    @Test
    public void testRegistrationViewComponents() {
        // Recursively search the content for components
        FormLayout formLayout = findComponentByType(registrationView.getContent(), FormLayout.class);
        assertNotNull(formLayout, "FormLayout should be present in the registration view.");

        EmailField emailField = findComponentByType(formLayout, EmailField.class);
        assertNotNull(emailField, "Email field should be present in the registration view.");

        PasswordField passwordField = findComponentByType(formLayout, PasswordField.class);
        assertNotNull(passwordField, "Password field should be present in the registration view.");

        PasswordField confirmPasswordField = findComponentByType(formLayout, PasswordField.class, true);
        assertNotNull(confirmPasswordField, "Confirm password field should be present in the registration view.");

        Button registerButton = findComponentByType(registrationView.getContent(), Button.class);
        assertNotNull(registerButton, "Register button should be present in the registration view.");
    }

    // Utility method to recursively search for components of a specific type
    private <T> T findComponentByType(com.vaadin.flow.component.Component parent, Class<T> componentClass) {
        return findComponentByType(parent, componentClass, false);
    }

    private <T> T findComponentByType(com.vaadin.flow.component.Component parent, Class<T> componentClass, boolean skipFirst) {
        if (componentClass.isInstance(parent)) {
            return componentClass.cast(parent);
        }

        // Search recursively in children
        Optional<T> foundComponent = parent.getChildren()
                .map(child -> findComponentByType(child, componentClass, skipFirst))
                .filter(java.util.Objects::nonNull)
                .skip(skipFirst ? 1 : 0)
                .findFirst();

        return foundComponent.orElse(null);
    }
}

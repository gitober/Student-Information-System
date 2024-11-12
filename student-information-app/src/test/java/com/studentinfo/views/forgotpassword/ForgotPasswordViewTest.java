package com.studentinfo.views.forgotpassword;

import com.studentinfo.services.EmailService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.QueryParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ForgotPasswordViewTest {

    @Mock
    private EmailService emailService;

    @Mock
    private MessageSource messageSource;  // Mock MessageSource

    @InjectMocks
    private ForgotPasswordView forgotPasswordView;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the messages for email.notification.success and any other expected keys
        when(messageSource.getMessage(eq("email.notification.success"), any(), any(Locale.class)))
                .thenReturn("An email has been sent to reset your password.");
        when(messageSource.getMessage(eq("forgot.password.email.sent"), any(), any(Locale.class)))
                .thenReturn("An email has been sent to reset your password.");
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(emailService, messageSource);
    }

    @Test
    void testHandleForgotPassword() throws Exception {
        // Arrange
        String email = "test@example.com";
        when(emailService.generateResetToken()).thenReturn("reset-token");

        // Use reflection to set the value of the private emailField
        Field emailField = ForgotPasswordView.class.getDeclaredField("emailField");
        emailField.setAccessible(true);
        emailField.set(forgotPasswordView, new com.vaadin.flow.component.textfield.EmailField());
        ((com.vaadin.flow.component.textfield.EmailField) emailField.get(forgotPasswordView)).setValue(email);

        // Use reflection to invoke the private handleForgotPassword method
        Method handleForgotPassword = ForgotPasswordView.class.getDeclaredMethod("handleForgotPassword");
        handleForgotPassword.setAccessible(true);

        // Mock Notification
        try (MockedStatic<Notification> notificationMockedStatic = mockStatic(Notification.class)) {
            notificationMockedStatic.when(() -> Notification.show(
                            "An email has been sent to reset your password.", 3000, Notification.Position.TOP_CENTER))
                    .thenReturn(mock(Notification.class));

            // Act
            handleForgotPassword.invoke(forgotPasswordView);

            // Assert
            verify(emailService, times(1)).sendEmail(eq(email), anyString(), anyString());
            notificationMockedStatic.verify(() -> Notification.show(
                    "An email has been sent to reset your password.", 3000, Notification.Position.TOP_CENTER));
        }
    }

    @Test
    void testBeforeEnter() {
        // Arrange
        BeforeEnterEvent event = mock(BeforeEnterEvent.class);
        Location location = mock(Location.class);
        when(event.getLocation()).thenReturn(location);
        when(location.getQueryParameters()).thenReturn(QueryParameters.simple(Map.of("token", "reset-token")));
        when(emailService.getEmailByToken("reset-token")).thenReturn("test@example.com");

        // Act
        forgotPasswordView.beforeEnter(event);

        // Assert
        verify(emailService, times(1)).getEmailByToken("reset-token");
    }
}

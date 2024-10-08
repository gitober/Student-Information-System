package com.studentinfo.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() throws Exception {
        // Set the value for fromEmail directly using reflection
        Field fromEmailField = EmailService.class.getDeclaredField("fromEmail");
        fromEmailField.setAccessible(true);
        fromEmailField.set(emailService, "from@example.com");
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test
        reset(mailSender);
    }

    @Test
    void testSendEmail() {
        // Arrange
        String subject = "Test Subject";
        String text = "Test email body.";
        String to = "test@example.com";

        // Create the expected message
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("from@example.com");
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(text);

        // Act
        emailService.sendEmail(to, subject, text);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
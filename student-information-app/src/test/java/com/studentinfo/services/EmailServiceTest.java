package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        // Initializes the mocks and injects them into the object under test (emailService)
        MockitoAnnotations.openMocks(this);
        emailService = new EmailService(mailSender, userRepository);  // Inject userRepository through the constructor
    }

    @AfterEach
    void tearDown() {
        // Reset mocks after each test to ensure a clean state
        reset(mailSender, userRepository);
    }

    @Test
    void testSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test message";

        // Call the method
        emailService.sendEmail(to, subject, text);

        // Verify that mailSender's send method was called with the expected message
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class)); // Cast to SimpleMailMessage
    }

    @Test
    void testGenerateResetToken() {
        String token = emailService.generateResetToken();
        assertNotNull(token);
        assertEquals(36, token.length()); // UUID length is 36 characters
    }

    @Test
    void testSaveAndRetrieveResetToken() {
        String email = "test@example.com";
        String token = UUID.randomUUID().toString();

        // Save token
        emailService.saveResetTokenToMemory(email, token);

        // Retrieve email by token
        String retrievedEmail = emailService.getEmailByToken(token);
        assertEquals(email, retrievedEmail);
    }

    @Test
    void testGetEmailByInvalidToken() {
        String token = "invalidToken";
        String retrievedEmail = emailService.getEmailByToken(token);
        assertNull(retrievedEmail); // Should return null for invalid token
    }

    @Test
    void testUpdatePassword() {
        String email = "test@example.com";
        String newPassword = "newPassword";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Call the method
        emailService.updatePassword(email, newPassword);

        // Verify that the password was updated and saved
        verify(userRepository, times(1)).save(user);
        assertNotNull(user.getHashedPassword());
    }

    @Test
    void testUpdatePasswordWithNonExistentUser() {
        String email = "nonexistent@example.com";
        String newPassword = "newPassword";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Call the method
        emailService.updatePassword(email, newPassword);

        // Verify that save was not called since user does not exist
        verify(userRepository, never()).save(any(User.class));
    }
}

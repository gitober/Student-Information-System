package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.studentinfo.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {

    private final Map<String, String> resetTokenStore = new HashMap<>(); // In-memory storage for reset tokens
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Password encoder

    @Value("${EMAIL_USERNAME}")
    private String fromEmail;

    @Autowired
    private UserRepository userRepository; // Inject the UserRepository to update the password

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // Generate a reset token
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    // Save reset token in the in-memory store
    public void saveResetTokenToMemory(String email, String token) {
        resetTokenStore.put(email, token);
    }

    // Retrieve the email associated with a given reset token
    public String getEmailByToken(String token) {
        return resetTokenStore.entrySet().stream()
                .filter(entry -> entry.getValue().equals(token))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    // Method to update the user's password in the database
    public void updatePassword(String email, String newPassword) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Hash the new password using BCrypt
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setHashedPassword(hashedPassword);
            userRepository.save(user);
        }
    }
}

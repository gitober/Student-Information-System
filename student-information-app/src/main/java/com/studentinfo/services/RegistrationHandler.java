package com.studentinfo.services;

import com.studentinfo.data.dto.RegistrationDTO;
import com.studentinfo.data.entity.*;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RegistrationHandler {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TranslationService translationService;

    @Autowired
    public RegistrationHandler(StudentService studentService, UserService userService,
                               PasswordEncoder passwordEncoder, TranslationService translationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.translationService = translationService;
    }

    public boolean registerUser(RegistrationDTO registrationData) {
        try {
            // Check if the email is already registered
            if (userService.findByEmail(registrationData.getEmail()).isPresent()) {
                Notification.show("This email is already registered. Please use a different email.");
                return false;
            }

            // Create user based on role
            User user = createUserBasedOnRole(registrationData.getRole());
            if (user == null) {
                Notification.show("Invalid role selected.");
                return false;
            }

            // Set user attributes from registration data
            setUserAttributes(user, registrationData);

            // Generate student number for Student user
            if (user instanceof Student student) {
                student.setStudentNumber(generateStudentNumber());
            }

            // Save the user to the database
            userService.save(user);

            // Add translations for the user
            addUserTranslations(user, registrationData);

            return true;
        } catch (IllegalArgumentException e) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Invalid input", e);
            Notification.show("Registration failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Registration failed", e);
            Notification.show("Registration failed due to an unexpected error.");
            return false;
        }
    }

    private User createUserBasedOnRole(String role) {
        if ("Student".equalsIgnoreCase(role)) {
            Student student = new Student();
            student.setUserType("STUDENT");
            return student;
        } else if ("Teacher".equalsIgnoreCase(role)) {
            Teacher teacher = new Teacher();
            teacher.setUserType("TEACHER");
            return teacher;
        }
        return null;
    }

    private Long generateStudentNumber() {
        return System.currentTimeMillis();
    }

    private void setUserAttributes(User user, RegistrationDTO registrationData) {
        user.setFirstName(registrationData.getFirstName());
        user.setLastName(registrationData.getLastName());
        user.setBirthday(registrationData.getBirthday());
        user.setPhoneNumber(registrationData.getPhoneNumber());
        user.setUsername(registrationData.getEmail());
        user.setEmail(registrationData.getEmail());

        String encodedPassword = passwordEncoder.encode(registrationData.getPassword());
        user.setHashedPassword(encodedPassword);

        user.setRoles(Set.of(Role.USER));
    }

    private void addUserTranslations(User user, RegistrationDTO registrationData) {
        translationService.saveUserTranslations(List.of(
                new UserTranslation(user, registrationData.getCurrentLocale(), "first_name", registrationData.getFirstName()),
                new UserTranslation(user, registrationData.getCurrentLocale(), "last_name", registrationData.getLastName()),
                new UserTranslation(user, registrationData.getCurrentLocale(), "email", registrationData.getEmail())
        ));
    }


    public boolean isEmailRegistered(String email) {
        return userService.findByEmail(email).isPresent();
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.*;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RegistrationHandler {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;
    private final TranslationService translationService;

    @Autowired
    public RegistrationHandler(StudentService studentService, UserService userService,
                               PasswordEncoder passwordEncoder, TranslationService translationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.studentService = studentService;
        this.translationService = translationService;
    }

    public boolean registerUser(String firstName, String lastName, LocalDate birthday,
                                String phoneNumber, String email, String password, String role, Language currentLocale) {
        try {
            if (userService.findByEmail(email).isPresent()) {
                Notification.show("This email is already registered. Please use a different email.");
                return false;
            }

            User user = createUserBasedOnRole(role);
            if (user == null) {
                Notification.show("Invalid role selected.");
                return false;
            }

            setUserAttributes(user, firstName, lastName, birthday, phoneNumber, email, password);

            if (user instanceof Student) {
                ((Student) user).setStudentNumber(generateStudentNumber());
            }

            userService.save(user);

            // Use the existing currentLocale parameter
            addUserTranslations(user, firstName, lastName, email, currentLocale);

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

    private void setUserAttributes(User user, String firstName, String lastName,
                                   LocalDate birthday, String phoneNumber,
                                   String email, String password) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthday(birthday);
        user.setPhoneNumber(phoneNumber);
        user.setUsername(email);
        user.setEmail(email);

        String encodedPassword = passwordEncoder.encode(password);
        user.setHashedPassword(encodedPassword);

        user.setRoles(Set.of(Role.USER));
    }

    private void addUserTranslations(User user, String firstName, String lastName, String email, Language locale) {
        translationService.saveUserTranslations(List.of(
                new UserTranslation(user, locale, "first_name", firstName),
                new UserTranslation(user, locale, "last_name", lastName),
                new UserTranslation(user, locale, "email", email)
        ));
    }


    public boolean isEmailRegistered(String email) {
        return userService.findByEmail(email).isPresent();
    }
}

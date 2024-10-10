package com.studentinfo.services;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RegistrationHandler {

    // Dependencies
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final StudentService studentService;

    @Autowired
    public RegistrationHandler(StudentService studentService, TeacherService teacherService,
                               UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.studentService = studentService;
    }

    // Public Methods

    // Register a new user based on role
    public boolean registerUser(String firstName, String lastName, LocalDate birthday,
                                String phoneNumber, String email, String password, String role) {
        try {
            // Check if the email already exists
            if (userService.findByEmail(email).isPresent()) {
                Notification.show("This email is already registered. Please use a different email.");
                return false;
            }

            // Create user based on role
            User user = createUserBasedOnRole(role);
            if (user == null) {
                Notification.show("Invalid role selected.");
                return false;
            }

            // Set user attributes
            setUserAttributes(user, firstName, lastName, birthday, phoneNumber, email, password);

            // Set student number for students
            if (user instanceof Student) {
                ((Student) user).setStudentNumber(generateStudentNumber());
            }

            // Save the user
            userService.save(user);
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

    // Private Helper Methods

    // Create a User object based on role
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
        return null; // Invalid role
    }

    // Generate student number
    private Long generateStudentNumber() {
        Long maxStudentNumber = studentService.list().stream()
                .map(Student::getStudentNumber)
                .filter(Objects::nonNull)
                .max(Long::compare)
                .orElse(0L); // Start from 0 if no students exist

        return maxStudentNumber + 1; // Next available student number
    }

    // Set common attributes for User
    private void setUserAttributes(User user, String firstName, String lastName,
                                   LocalDate birthday, String phoneNumber,
                                   String email, String password) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthday(birthday);
        user.setPhoneNumber(phoneNumber);
        user.setUsername(email);
        user.setEmail(email);

        // Encode and set password
        String encodedPassword = passwordEncoder.encode(password);
        user.setHashedPassword(encodedPassword);

        // Assign roles
        user.setRoles(Set.of(Role.USER));
    }

    // Check if email is already registered
    public boolean isEmailRegistered(String email) {
        return userService.findByEmail(email).isPresent();
    }
}

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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class RegistrationHandler {

    // Dependencies
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationHandler(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Public Methods

    // Register a new user based on role
    public boolean registerUser(String firstName, String lastName, LocalDate birthday, String phoneNumber,
                                String email, String password, String role) {
        try {
            User user = createUserBasedOnRole(role);
            if (user == null) {
                Notification.show("Invalid role selected.");
                return false;
            }

            // Set common user attributes
            setUserAttributes(user, firstName, lastName, birthday, phoneNumber, email, password);

            // If the user is a Student, set the student number
            if (user instanceof Student) {
                ((Student) user).setStudentNumber(generateStudentNumber());
            }

            // Save the user using UserService
            userService.save(user);
            return true;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Registration failed", e);
            return false;
        }
    }

    // Private Helper Methods

    // Create a User object based on the role
    private User createUserBasedOnRole(String role) {
        if ("Student".equalsIgnoreCase(role)) {
            Student student = new Student();
            student.setUserType("STUDENT");
            return student;
        } else if ("Teacher".equalsIgnoreCase(role)) {
            Teacher teacher = new Teacher();
            teacher.setUserType("TEACHER");
            // Ensure teacher does not have a student number
            return teacher;
        }
        return null; // Invalid role
    }

    // Generate a student number (You can modify this logic as per your requirements)
    private Long generateStudentNumber() {
        // Example: Generate a random student number or implement a logic to get the next available number
        return System.currentTimeMillis(); // Using current time for simplicity
    }

    // Set common attributes for the User
    private void setUserAttributes(User user, String firstName, String lastName, LocalDate birthday,
                                   String phoneNumber, String email, String password) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBirthday(birthday);
        user.setPhoneNumber(phoneNumber);
        user.setUsername(email);
        user.setEmail(email);

        // Encode and set password
        String encodedPassword = passwordEncoder.encode(password);
        user.setHashedPassword(encodedPassword);

        // Set roles
        user.setRoles(Set.of(Role.USER));
    }
}

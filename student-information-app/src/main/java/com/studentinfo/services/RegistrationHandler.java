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

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationHandler(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean registerUser(String firstName, String lastName, LocalDate birthday, String phoneNumber,
                                String email, String password, String role) {
        try {
            User user;

            // Determine user type based on the role and create the appropriate object
            if ("Student".equalsIgnoreCase(role)) {
                user = new Student(); // Create a Student instance
                ((Student) user).setGrade("Grade Example");
                ((Student) user).setStudentClass("Class Example");
            } else if ("Teacher".equalsIgnoreCase(role)) {
                user = new Teacher(); // Create a Teacher instance
                ((Teacher) user).setSubject("Subject Example");
                ((Teacher) user).setDepartment("Department Example");
            } else {
                Notification.show("Invalid role selected.");
                return false;
            }

            // Common user attributes
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setBirthday(birthday);
            user.setPhoneNumber(phoneNumber);
            user.setUsername(email); // Previously set as username; set it explicitly as email too
            user.setEmail(email); // Ensure the email field is set correctly

            // Encode and set password
            String encodedPassword = passwordEncoder.encode(password);
            user.setHashedPassword(encodedPassword);

            // Correctly map role selection to Role enum
            if ("Student".equalsIgnoreCase(role)) {
                user.setRoles(Set.of(Role.USER)); // Set to USER
                // Set specific student attributes
            } else if ("Teacher".equalsIgnoreCase(role)) {
                user.setRoles(Set.of(Role.USER)); // Set to USER
                // Set specific teacher attributes
            }

            // Save the user using UserService
            userService.save(user);
            return true;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Registration failed", e);
            return false;
        }
    }

}

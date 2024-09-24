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
                user = new Student();
                user.setUserType("STUDENT"); // Set userType for Student
            } else if ("Teacher".equalsIgnoreCase(role)) {
                user = new Teacher();
                user.setUserType("TEACHER"); // Set userType for Teacher
            } else {
                Notification.show("Invalid role selected.");
                return false;
            }

            // Common user attributes
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

            // Save the user using UserService
            userService.save(user);
            return true;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Registration failed", e);
            return false;
        }
    }

}
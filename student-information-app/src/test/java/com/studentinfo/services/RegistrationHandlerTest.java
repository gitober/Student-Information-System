package com.studentinfo.services;

import com.studentinfo.data.entity.Role;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StudentService studentService;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private RegistrationHandler registrationHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulStudentRegistration() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        LocalDate birthday = LocalDate.of(2000, 1, 1);
        String phoneNumber = "1234567890";
        String email = "john.doe@example.com";
        String password = "password";
        String role = "Student";

        User user = new Student(); // Simulate a Student entity
        when(userService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");

        // Act
        boolean result = registrationHandler.registerUser(firstName, lastName, birthday, phoneNumber, email, password, role);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(password);
    }

    @Test
    public void testSuccessfulTeacherRegistration() {
        // Arrange
        String firstName = "Jane";
        String lastName = "Smith";
        LocalDate birthday = LocalDate.of(1985, 5, 15);
        String phoneNumber = "0987654321";
        String email = "jane.smith@example.com";
        String password = "password";
        String role = "Teacher";

        User user = new Teacher(); // Simulate a Teacher entity
        when(userService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(password)).thenReturn("hashedPassword");

        // Act
        boolean result = registrationHandler.registerUser(firstName, lastName, birthday, phoneNumber, email, password, role);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(password);
    }


}

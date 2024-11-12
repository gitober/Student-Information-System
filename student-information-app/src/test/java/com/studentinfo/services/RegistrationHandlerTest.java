package com.studentinfo.services;

import com.studentinfo.data.dto.RegistrationDTO;
import com.studentinfo.data.entity.*;
import org.junit.jupiter.api.AfterEach;
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
    private TranslationService translationService;

    @InjectMocks
    private RegistrationHandler registrationHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        reset(userService, passwordEncoder, studentService, translationService);
    }

    @Test
    void testSuccessfulStudentRegistration() {
        // Arrange
        RegistrationDTO registrationData = RegistrationDTO.builder()
                .firstName("John")
                .lastName("Doe")
                .birthday(LocalDate.of(2000, 1, 1))
                .phoneNumber("1234567890")
                .email("john.doe@example.com")
                .password("password")
                .role("Student")
                .currentLocale(Language.EN)
                .build();

        User user = new Student(); // Simulate a Student entity
        when(userService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Act
        boolean result = registrationHandler.registerUser(registrationData);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
        verify(translationService, times(1)).saveUserTranslations(anyList());
    }

    @Test
    void testSuccessfulTeacherRegistration() {
        // Arrange
        RegistrationDTO registrationData = RegistrationDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .birthday(LocalDate.of(1985, 5, 15))
                .phoneNumber("0987654321")
                .email("jane.smith@example.com")
                .password("password")
                .role("Teacher")
                .currentLocale(Language.FI)
                .build();

        User user = new Teacher(); // Simulate a Teacher entity
        when(userService.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        // Act
        boolean result = registrationHandler.registerUser(registrationData);

        // Assert
        assertTrue(result);
        verify(userService, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }
}

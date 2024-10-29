package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentEditProfileViewTest {

    private StudentEditProfileView studentEditProfileView;
    private Student studentMock;
    private UserService userServiceMock;
    private MessageSource messageSourceMock;

    @BeforeEach
    public void setUp() {
        // Mock the Student, UserService, and MessageSource
        studentMock = mock(Student.class);
        userServiceMock = mock(UserService.class);
        messageSourceMock = mock(MessageSource.class);

        // Define behavior for the message source mock to return appropriate translations
        when(messageSourceMock.getMessage(anyString(), any(), any(Locale.class))).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            switch (key) {
                case "student.edit.profile.title": return "Edit Student Profile";
                case "student.edit.profile.currentDetails": return "Current Details";
                case "student.edit.profile.firstName": return "First Name";
                case "student.edit.profile.lastName": return "Last Name";
                case "student.edit.profile.phoneNumber": return "Phone Number";
                case "student.edit.profile.email": return "Email";
                case "student.edit.profile.newPassword": return "New Password";
                case "student.edit.profile.save": return "Save";
                case "student.edit.profile.name": return "Name";
                case "student.edit.profile.success": return "Profile updated successfully!";
                default: return key;
            }
        });

        // Mock Student object field values
        when(studentMock.getFirstName()).thenReturn("John");
        when(studentMock.getLastName()).thenReturn("Doe");
        when(studentMock.getEmail()).thenReturn("john.doe@example.com");
        when(studentMock.getPhoneNumber()).thenReturn("123-456-7890");

        // Initialize a mocked Vaadin UI context
        UI uiMock = mock(UI.class);
        UI.setCurrent(uiMock);  // Ensure the UI context is available

        // Instantiate the view with mocks
        studentEditProfileView = new StudentEditProfileView(studentMock, userServiceMock, messageSourceMock);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context
    }

    @Test
    public void testSaveButtonClickUpdatesStudentProfile() throws Exception {
        // Access private fields using reflection
        TextField firstNameField = (TextField) findField(studentEditProfileView, "firstNameField");
        TextField lastNameField = (TextField) findField(studentEditProfileView, "lastNameField");
        PasswordField newPasswordField = (PasswordField) findField(studentEditProfileView, "newPasswordField");
        Button saveButton = (Button) findField(studentEditProfileView, "saveButton");

        // Set new values in the text fields
        firstNameField.setValue("Jane");
        lastNameField.setValue("Smith");
        newPasswordField.setValue("newPassword123");

        // Simulate the save button click by triggering the event
        saveButton.click();

        // Verify that the profile was updated with the new values
        verify(studentMock).setFirstName("Jane");
        verify(studentMock).setLastName("Smith");
        verify(userServiceMock, times(1)).updatePassword("john.doe@example.com", "newPassword123"); // Only expect it once
        verify(userServiceMock).save(studentMock);

        // Verify that the success notification message was displayed
        verify(messageSourceMock).getMessage("student.edit.profile.success", null, LocaleContextHolder.getLocale());
    }

    // Helper method to access private fields using reflection
    private Object findField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }
}

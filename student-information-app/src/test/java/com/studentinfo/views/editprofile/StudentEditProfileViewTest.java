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

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class StudentEditProfileViewTest {

    private StudentEditProfileView studentEditProfileView;
    private Student studentMock;
    private UserService userServiceMock;

    @BeforeEach
    public void setUp() {
        // Mock the Student and UserService
        studentMock = mock(Student.class);
        userServiceMock = mock(UserService.class);

        when(studentMock.getFirstName()).thenReturn("John");
        when(studentMock.getLastName()).thenReturn("Doe");
        when(studentMock.getEmail()).thenReturn("john.doe@example.com");
        when(studentMock.getPhoneNumber()).thenReturn("123-456-7890");

        // Initialize a mocked Vaadin UI context
        UI uiMock = mock(UI.class);
        UI.setCurrent(uiMock);  // Ensure the UI context is available

        // Instantiate the view
        studentEditProfileView = new StudentEditProfileView(studentMock, userServiceMock, Mockito.mock(MessageSource.class));
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
        TextField phoneNumberField = (TextField) findField(studentEditProfileView, "phoneNumberField");
        TextField emailField = (TextField) findField(studentEditProfileView, "emailField");
        PasswordField newPasswordField = (PasswordField) findField(studentEditProfileView, "newPasswordField");
        Button saveButton = (Button) findField(studentEditProfileView, "saveButton");

        // Set new values in the text fields
        firstNameField.setValue("Jane");
        lastNameField.setValue("Smith");
        phoneNumberField.setValue("987-654-3210");
        emailField.setValue("jane.smith@example.com");
        newPasswordField.setValue("newPassword123");

        // Simulate the save button click
        saveButton.click();

        // Verify that the profile was updated with the new values
        verify(studentMock).setFirstName("Jane");
        verify(studentMock).setLastName("Smith");
        verify(studentMock).setPhoneNumber("987-654-3210");
        verify(studentMock).setEmail("jane.smith@example.com");
        verify(userServiceMock, times(1)).updatePassword("john.doe@example.com", "newPassword123");
        verify(userServiceMock).save(studentMock);
    }

    @Test
    public void testSaveButtonClickWithoutNewPassword() throws Exception {
        // Access private fields using reflection
        TextField firstNameField = (TextField) findField(studentEditProfileView, "firstNameField");
        TextField lastNameField = (TextField) findField(studentEditProfileView, "lastNameField");
        TextField phoneNumberField = (TextField) findField(studentEditProfileView, "phoneNumberField");
        TextField emailField = (TextField) findField(studentEditProfileView, "emailField");
        PasswordField newPasswordField = (PasswordField) findField(studentEditProfileView, "newPasswordField");
        Button saveButton = (Button) findField(studentEditProfileView, "saveButton");

        // Set values in the text fields
        firstNameField.setValue("Jane");
        lastNameField.setValue("Smith");
        phoneNumberField.setValue("987-654-3210");
        emailField.setValue("jane.smith@example.com");
        newPasswordField.setValue("");  // No new password

        // Simulate the save button click
        saveButton.click();

        // Verify that the profile was updated with the new values, but no password update
        verify(studentMock).setFirstName("Jane");
        verify(studentMock).setLastName("Smith");
        verify(studentMock).setPhoneNumber("987-654-3210");
        verify(studentMock).setEmail("jane.smith@example.com");
        verify(userServiceMock, never()).updatePassword(anyString(), anyString());  // Ensure no password update occurred
        verify(userServiceMock).save(studentMock);
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

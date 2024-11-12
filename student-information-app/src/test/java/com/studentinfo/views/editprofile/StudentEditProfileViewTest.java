package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.util.Locale;

import static org.mockito.Mockito.*;

class StudentEditProfileViewTest {

    private StudentEditProfileView studentEditProfileView;
    private Student studentMock;
    private UserService userServiceMock;
    private MessageSource messageSourceMock;

    @BeforeEach
    public void setUp() {
        studentMock = mock(Student.class);
        userServiceMock = mock(UserService.class);
        messageSourceMock = mock(MessageSource.class);

        when(messageSourceMock.getMessage(anyString(), any(), any(Locale.class))).thenAnswer(invocation -> {
            String key = invocation.getArgument(0);
            return switch (key) {
                case "student.edit.profile.title" -> "Edit Student Profile";
                case "student.edit.profile.currentDetails" -> "Current Details";
                case "student.edit.profile.firstName" -> "First Name";
                case "student.edit.profile.lastName" -> "Last Name";
                case "student.edit.profile.phoneNumber" -> "Phone Number";
                case "student.edit.profile.email" -> "Email";
                case "student.edit.profile.newPassword" -> "New Password";
                case "student.edit.profile.save" -> "Save";
                case "student.edit.profile.success" -> "Profile updated successfully!";
                default -> key;
            };
        });

        when(studentMock.getFirstName()).thenReturn("John");
        when(studentMock.getLastName()).thenReturn("Doe");
        when(studentMock.getEmail()).thenReturn("john.doe@example.com");
        when(studentMock.getPhoneNumber()).thenReturn("123-456-7890");

        // Mock UI context
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        studentEditProfileView = new StudentEditProfileView(studentMock, userServiceMock, messageSourceMock);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear UI context
    }

    @Test
    void testSaveButtonClickUpdatesStudentProfile() {
        TextField firstNameField = (TextField) findField(studentEditProfileView, "firstNameField");
        TextField lastNameField = (TextField) findField(studentEditProfileView, "lastNameField");
        PasswordField newPasswordField = (PasswordField) findField(studentEditProfileView, "newPasswordField");
        Button saveButton = (Button) findField(studentEditProfileView, "saveButton");

        firstNameField.setValue("Jane");
        lastNameField.setValue("Smith");
        newPasswordField.setValue("newPassword123");

        saveButton.click();

        verify(studentMock).setFirstName("Jane");
        verify(studentMock).setLastName("Smith");
        verify(userServiceMock, times(1)).updatePassword("john.doe@example.com", "newPassword123");
        verify(userServiceMock).save(studentMock);

        // Verify that success message was requested from messageSource
        verify(messageSourceMock).getMessage("student.edit.profile.success", null, LocaleContextHolder.getLocale());
    }

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

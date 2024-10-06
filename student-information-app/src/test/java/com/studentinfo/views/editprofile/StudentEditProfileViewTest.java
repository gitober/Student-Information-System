package com.studentinfo.views.editprofile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.UserService; // Import the UserService
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StudentEditProfileViewTest {

    private StudentEditProfileView studentEditProfileView;
    private UserService userService; // Mocked UserService

    @BeforeEach
    public void setUp() {
        // Mock the Student object
        Student student = Mockito.mock(Student.class);
        when(student.getFirstName()).thenReturn("John");
        when(student.getLastName()).thenReturn("Doe");
        when(student.getEmail()).thenReturn("john.doe@example.com");
        when(student.getPhoneNumber()).thenReturn("123-456-7890");

        // Mock the UserService
        userService = Mockito.mock(UserService.class);

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view
        System.out.println("Setting up StudentEditProfileView...");
        studentEditProfileView = new StudentEditProfileView(student, userService); // Pass mocked UserService
        System.out.println("StudentEditProfileView setup completed.");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }

    @Test
    public void testStudentEditProfileViewComponents() throws Exception {
        System.out.println("Testing StudentEditProfileView components...");

        // Verify that the main layout is not null
        assertNotNull(studentEditProfileView, "The StudentEditProfileView should not be null.");

        // Access private fields using reflection
        Field firstNameField = StudentEditProfileView.class.getDeclaredField("firstNameField");
        firstNameField.setAccessible(true);
        TextField firstNameTextField = (TextField) firstNameField.get(studentEditProfileView);
        assertNotNull(firstNameTextField, "First Name field should be present in the view.");
        System.out.println("First Name field is present.");

        Field saveButtonField = StudentEditProfileView.class.getDeclaredField("saveButton");
        saveButtonField.setAccessible(true);
        Button saveButton = (Button) saveButtonField.get(studentEditProfileView);
        assertNotNull(saveButton, "Save button should be present in the view.");
        System.out.println("Save button is present.");

        // Simulate clicking the save button
        System.out.println("Clicking the save button...");
        assertDoesNotThrow(saveButton::click, "Clicking the save button should not throw an exception.");
        System.out.println("Save button clicked successfully.");
    }
}

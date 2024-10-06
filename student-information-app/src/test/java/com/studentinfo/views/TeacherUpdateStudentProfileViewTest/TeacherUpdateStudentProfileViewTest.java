package com.studentinfo.views.TeacherUpdateStudentProfileViewTest;

import com.studentinfo.data.entity.Student;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.StudentService;
import com.studentinfo.views.TeacherUpdateStudentProfileView.TeacherUpdateStudentProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TeacherUpdateStudentProfileViewTest {

    private TeacherUpdateStudentProfileView view;

    @BeforeEach
    public void setUp() {
        // Mock the services
        StudentService studentService = Mockito.mock(StudentService.class);
        AuthenticatedUser authenticatedUser = Mockito.mock(AuthenticatedUser.class);

        // Mock the student list
        when(studentService.list()).thenReturn(Collections.singletonList(new Student()));

        // Initialize the UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        view = new TeacherUpdateStudentProfileView(studentService, authenticatedUser);
    }

    @AfterEach
    public void tearDown() {
        // Clear the Vaadin UI context
        UI.setCurrent(null);

        // Clear the view reference
        view = null;
    }

    @Test
    public void testComponentsPresence() {
        // Check if the grid is present
        @SuppressWarnings("unchecked")
        Grid<Student> studentGrid = view.getContent().getChildren()
                .filter(component -> component instanceof Grid)
                .map(component -> (Grid<Student>) component)
                .findFirst()
                .orElse(null);

        assertNotNull(studentGrid, "Student grid should be present in the view.");

        // Check if the search field is present
        TextField searchField = view.getContent().getChildren()
                .filter(component -> component instanceof TextField)
                .map(component -> (TextField) component)
                .findFirst()
                .orElse(null);
        assertNotNull(searchField, "Search field should be present in the view.");

        // Check if the save button is present
        Button saveButton = view.getContent().getChildren()
                .flatMap(Component::getChildren)
                .filter(component -> component instanceof Button)
                .map(component -> (Button) component)
                .filter(button -> "Save changes".equals(button.getText()))
                .findFirst()
                .orElse(null);
        assertNotNull(saveButton, "Save button should be present in the view.");
    }
}

package com.studentinfo.views.teacher_update_student_profile;

import com.studentinfo.data.entity.Student;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.StudentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherUpdateStudentProfileViewTest {

    private TeacherUpdateStudentProfileView view;

    @BeforeEach
    public void setUp() {
        // Mock services and dependencies
        StudentService studentService = mock(StudentService.class);
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        MessageSource messageSource = mock(MessageSource.class);

        // Mock the student list
        when(studentService.list()).thenReturn(Collections.singletonList(new Student()));

        // Mock translations for UI labels
        when(messageSource.getMessage("teacher.update.student.form.save", null, LocaleContextHolder.getLocale())).thenReturn("Save changes");
        when(messageSource.getMessage("teacher.update.student.search.placeholder", null, LocaleContextHolder.getLocale())).thenReturn("Search by First Name or Last Name");

        // Initialize UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        view = new TeacherUpdateStudentProfileView(studentService, authenticatedUser, messageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear the Vaadin UI context
        UI.setCurrent(null);
        view = null;
    }

    @Test
    void testComponentsPresence() {
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
                .filter(field -> "Search by First Name or Last Name".equals(field.getLabel()))
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

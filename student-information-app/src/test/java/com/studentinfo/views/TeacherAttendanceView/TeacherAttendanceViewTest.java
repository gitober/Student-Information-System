package com.studentinfo.views.TeacherAttendanceView;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeacherAttendanceViewTest {

    private TeacherAttendanceView teacherAttendanceView;

    @BeforeEach
    public void setUp() {
        // Mock services
        TeacherService mockTeacherService = mock(TeacherService.class);
        CourseService mockCourseService = mock(CourseService.class);
        AuthenticatedUser mockAuthenticatedUser = mock(AuthenticatedUser.class);

        // Set up mock authentication
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        UserDetails mockUserDetails = mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testTeacher");
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetails);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        // Mock the teacher ID retrieval
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(1L);
        when(mockTeacherService.getTeacherByUsername("testTeacher")).thenReturn(Optional.of(mockTeacher));
        when(mockTeacherService.getAttendanceRecordsForTeacher(1L)).thenReturn(Collections.emptyList());

        // Set up the UI context for Vaadin
        UI ui = new UI();
        UI.setCurrent(ui);

        // Create a mock VaadinSession and attach it to the UI
        VaadinSession mockSession = mock(VaadinSession.class);
        VaadinSession.setCurrent(mockSession);
        ui.getInternals().setSession(mockSession);

        // Instantiate the TeacherAttendanceView
        teacherAttendanceView = new TeacherAttendanceView(mockTeacherService, mockCourseService, mockAuthenticatedUser);
    }

    @AfterEach
    public void tearDown() {
        // Clear the Vaadin UI and session context
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);

        // Clear the teacherAttendanceView reference
        teacherAttendanceView = null;

        // Reset the SecurityContext
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testTeacherAttendanceViewComponents() {
        // Check if the search field is present
        TextField searchField = findComponent(TextField.class, "Search by Course or Student");
        assertNotNull(searchField, "Search field should be present in the teacher attendance view.");

        // Check if the attendance grid is present
        Grid attendanceGrid = findComponent(Grid.class, null);
        assertNotNull(attendanceGrid, "Attendance grid should be present in the teacher attendance view.");

        // Check if the 'Add Attendance' button is present
        Button addAttendanceButton = findComponent(Button.class, "Add Attendance");
        assertNotNull(addAttendanceButton, "'Add Attendance' button should be present in the teacher attendance view.");
    }

    // Utility method to find components by class type and label text
    private <T> T findComponent(Class<T> componentClass, String labelText) {
        return findComponentInTree(teacherAttendanceView.getContent(), componentClass, labelText);
    }

    private <T> T findComponentInTree(com.vaadin.flow.component.Component component, Class<T> componentClass, String labelText) {
        if (componentClass.isInstance(component)) {
            switch (component) {
                case TextField textField when labelText.equals(textField.getLabel()) -> {
                    return componentClass.cast(component);
                }
                case Button button when labelText.equals(button.getText()) -> {
                    return componentClass.cast(component);
                }
                case Grid grid -> {
                    return componentClass.cast(component);
                }
                default -> {
                }
            }
        }

        // Recursively search through the children of the current component
        return component.getChildren()
                .map(child -> findComponentInTree(child, componentClass, labelText))
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElse(null);
    }
}

package com.studentinfo.views.TeacherAttendanceView;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.studentinfo.views.teacher_attendance_view.TeacherAttendanceView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TeacherAttendanceViewTest {

    private TeacherAttendanceView teacherAttendanceView;

    @BeforeEach
    public void setUp() {
        // Mock services
        TeacherService mockTeacherService = mock(TeacherService.class);
        CourseService mockCourseService = mock(CourseService.class);
        AuthenticatedUser mockAuthenticatedUser = mock(AuthenticatedUser.class);
        MessageSource mockMessageSource = mock(MessageSource.class);

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

        // Mock translation messages
        when(mockMessageSource.getMessage("teacher.attendance.search.placeholder", null, Locale.getDefault()))
                .thenReturn("Search by Course or Student");
        when(mockMessageSource.getMessage("teacher.attendance.add.button", null, Locale.getDefault()))
                .thenReturn("Add Attendance");

        // Set up the UI context for Vaadin
        UI ui = new UI();
        UI.setCurrent(ui);

        // Create a mock VaadinSession and attach it to the UI
        VaadinSession mockSession = mock(VaadinSession.class);
        VaadinSession.setCurrent(mockSession);
        ui.getInternals().setSession(mockSession);

        // Instantiate the TeacherAttendanceView with mocked MessageSource
        teacherAttendanceView = new TeacherAttendanceView(mockTeacherService, mockCourseService, mockAuthenticatedUser, mockMessageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear the Vaadin UI and session context to prevent leaks between tests
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);

        // Clear the reference to TeacherAttendanceView to ensure no residual data persists
        teacherAttendanceView = null;

        // Reset the SecurityContext to clear any mock authentication
        SecurityContextHolder.clearContext();

        // Clear any other mocked or injected static state if applicable
        Mockito.reset();  // Resets all Mockito mocks created during this test
    }

    @Test
    void testTeacherAttendanceViewComponents() {
        // Locate the search field by type (TextField) without specifying a labelText
        TextField searchField = findComponent(TextField.class);
        assertNotNull(searchField, "Search field should be present in the teacher attendance view.");

        // Locate the attendance grid
        Grid<?> attendanceGrid = findComponent(Grid.class);
        assertNotNull(attendanceGrid, "Attendance grid should be present in the teacher attendance view.");
    }

    // Updated findComponent method to accept optional labelText
    private <T> T findComponent(Class<T> componentClass) {
        return findComponentInTree(teacherAttendanceView.getContent(), componentClass, null);
    }

    // Modified findComponentInTree to handle both cases: with and without labelText
    private <T> T findComponentInTree(com.vaadin.flow.component.Component component, Class<T> componentClass, String labelText) {
        if (componentClass.isInstance(component)) {
            if (labelText == null) {
                return componentClass.cast(component);
            } else if (component instanceof TextField textField && labelText.equals(textField.getPlaceholder())) {
                return componentClass.cast(component);
            } else if (component instanceof Button button && labelText.equals(button.getText())) {
                System.out.println("Button with text '" + button.getText() + "' found");  // Debugging print
                return componentClass.cast(component);
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

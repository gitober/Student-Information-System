package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StudentCoursesViewTest {

    private StudentCoursesView studentCoursesView;
    private MessageSource messageSource; // Declare messageSource as a field

    @BeforeEach
    public void setUp() {
        System.out.println("Setting up the test environment...");

        // Mock the services
        CourseService courseService = Mockito.mock(CourseService.class);
        AttendanceService attendanceService = Mockito.mock(AttendanceService.class);
        UserService userService = Mockito.mock(UserService.class);
        messageSource = Mockito.mock(MessageSource.class); // Initialize MessageSource

        // Mock service methods
        when(courseService.getEnrolledCourses(Mockito.anyLong())).thenReturn(Collections.emptyList());
        when(courseService.getAvailableCourses()).thenReturn(Collections.emptyList());
        when(userService.getCurrentStudentNumber()).thenReturn(1L);
        when(messageSource.getMessage(Mockito.anyString(), Mockito.isNull(), Mockito.any())).thenReturn("Mocked Message");

        // Instantiate the view with mocked services
        studentCoursesView = new StudentCoursesView(courseService, attendanceService, userService, messageSource);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("Cleaning up after test...");

        // Reset all mocks to avoid interference between tests
        Mockito.reset(messageSource);

        // Clear UI context if it was set in tests
        UI.setCurrent(null);

        System.out.println("Test cleanup completed.");
    }

    @Test
    void testStudentCoursesViewComponents() throws Exception {
        System.out.println("Testing components in StudentCoursesView...");

        // Mock the expected title for "my.courses.title" key
        String expectedTitle = messageSource.getMessage("my.courses.title", null, LocaleContextHolder.getLocale());

        // Verify that the main container layout is not null
        assertNotNull(studentCoursesView.getContent(), "The main container layout should not be null.");
        System.out.println("Main container layout is not null.");

        // Check the title component
        boolean hasTitle = studentCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof H2 && expectedTitle.equals(((H2) component).getText()));
        assertTrue(hasTitle, "The title should be '" + expectedTitle + "'.");

        // Access the private fields using reflection
        try {
            // Test accessing enrolledCoursesGrid field
            Field enrolledCoursesGridField = StudentCoursesView.class.getDeclaredField("enrolledCoursesGrid");
            enrolledCoursesGridField.setAccessible(true);
            Grid<Course> enrolledCoursesGrid = (Grid<Course>) enrolledCoursesGridField.get(studentCoursesView);
            assertNotNull(enrolledCoursesGrid, "Enrolled courses grid should be initialized.");
            System.out.println("Enrolled courses grid is initialized.");

            // Test accessing availableCoursesGrid field
            Field availableCoursesGridField = StudentCoursesView.class.getDeclaredField("availableCoursesGrid");
            availableCoursesGridField.setAccessible(true);
            Grid<Course> availableCoursesGrid = (Grid<Course>) availableCoursesGridField.get(studentCoursesView);
            assertNotNull(availableCoursesGrid, "Available courses grid should be initialized.");
            System.out.println("Available courses grid is initialized.");

            // Check if columns are added to the grids
            assertEquals(5, enrolledCoursesGrid.getColumns().size(), "Enrolled courses grid should have 5 columns.");
            System.out.println("Number of columns in enrolledCoursesGrid: " + enrolledCoursesGrid.getColumns().size());

            assertEquals(5, availableCoursesGrid.getColumns().size(), "Available courses grid should have 5 columns.");
            System.out.println("Number of columns in availableCoursesGrid: " + availableCoursesGrid.getColumns().size());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection error: " + e.getMessage());
        }

        System.out.println("Component testing in StudentCoursesView completed.");
    }


}

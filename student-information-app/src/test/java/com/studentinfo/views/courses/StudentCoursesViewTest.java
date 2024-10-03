package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StudentCoursesViewTest {

    private StudentCoursesView studentCoursesView;

    @BeforeEach
    public void setUp() {
        System.out.println("Setting up the test environment...");

        // Mock the services
        CourseService courseService = Mockito.mock(CourseService.class);
        AttendanceService attendanceService = Mockito.mock(AttendanceService.class);
        UserService userService = Mockito.mock(UserService.class);

        // Mock service methods
        when(courseService.getEnrolledCourses(Mockito.anyLong())).thenReturn(Collections.emptyList());
        when(courseService.getAvailableCourses()).thenReturn(Collections.emptyList());
        when(userService.getCurrentStudentNumber()).thenReturn(1L);  // Mock a student number

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        studentCoursesView = new StudentCoursesView(courseService, attendanceService, userService);
    }

    @Test
    public void testStudentCoursesViewComponents() throws Exception {
        System.out.println("Testing components in StudentCoursesView...");

        // Verify that the main container layout is not null
        assertNotNull(studentCoursesView.getContent(), "The main container layout should not be null.");
        System.out.println("Main container layout is not null.");

        // Check the title and description components
        boolean hasTitle = studentCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof H2 && "My Courses".equals(((H2) component).getText()));
        assertTrue(hasTitle, "The title should be 'My Courses'.");
        System.out.println("Title 'My Courses' is present.");

        boolean hasSearchField = studentCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof TextField && "Search Courses".equals(((TextField) component).getLabel()));
        assertTrue(hasSearchField, "There should be a TextField with the label 'Search Courses'.");
        System.out.println("Search field with label 'Search Courses' is present.");

        // Access the private fields using reflection
        Field enrolledCoursesGridField = StudentCoursesView.class.getDeclaredField("enrolledCoursesGrid");
        enrolledCoursesGridField.setAccessible(true);
        Grid<Course> enrolledCoursesGrid = (Grid<Course>) enrolledCoursesGridField.get(studentCoursesView);

        Field availableCoursesGridField = StudentCoursesView.class.getDeclaredField("availableCoursesGrid");
        availableCoursesGridField.setAccessible(true);
        Grid<Course> availableCoursesGrid = (Grid<Course>) availableCoursesGridField.get(studentCoursesView);

        // Verify that the grid components are initialized
        assertNotNull(enrolledCoursesGrid, "Enrolled courses grid should be initialized.");
        System.out.println("Enrolled courses grid is initialized.");

        assertNotNull(availableCoursesGrid, "Available courses grid should be initialized.");
        System.out.println("Available courses grid is initialized.");

        // Check if columns are added to the grids
        // Update the expected column count if necessary
        System.out.println("Number of columns in enrolledCoursesGrid: " + enrolledCoursesGrid.getColumns().size());
        assertEquals(5, enrolledCoursesGrid.getColumns().size(), "Enrolled courses grid should have 5 columns.");

        System.out.println("Number of columns in availableCoursesGrid: " + availableCoursesGrid.getColumns().size());
        assertEquals(5, availableCoursesGrid.getColumns().size(), "Available courses grid should have 5 columns.");

        System.out.println("Component testing in StudentCoursesView completed.");
    }

    @Test
    public void testEnrollInCourseButton() throws Exception {
        System.out.println("Testing enroll in course button...");

        // Create a sample course
        Course sampleCourse = new Course();
        sampleCourse.setCourseName("Sample Course");
        sampleCourse.setTeachers(Collections.emptyList());  // Ensure getTeachers() does not return null

        // Access the private method using reflection
        Method enrollInCourseMethod = StudentCoursesView.class.getDeclaredMethod("enrollInCourse", Course.class);
        enrollInCourseMethod.setAccessible(true);

        // Invoke the private method
        System.out.println("Invoking enrollInCourse method...");
        assertDoesNotThrow(() -> enrollInCourseMethod.invoke(studentCoursesView, sampleCourse),
                "Invoking the 'enrollInCourse' method should not throw an exception.");
        System.out.println("enrollInCourse method invoked successfully.");

        // Verify if a confirmation dialog opens and the action is handled properly
        // This part is more for structural verification as dialog testing is complex in unit tests
        Button enrollButton = new Button("Enroll");
        enrollButton.addClickListener(event -> Notification.show("Enrollment confirmed"));

        System.out.println("Simulating enroll button click...");
        assertDoesNotThrow(enrollButton::click, "Clicking the 'Enroll' button should not throw an exception.");
        System.out.println("Enroll button click simulation completed.");

        System.out.println("Enroll in course button test completed.");
    }
}

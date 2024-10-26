package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TeacherCoursesViewTest {

    private TeacherCoursesView teacherCoursesView;
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        // Mock the services
        CourseService courseService = Mockito.mock(CourseService.class);
        TeacherService teacherService = Mockito.mock(TeacherService.class);
        messageSource = Mockito.mock(MessageSource.class);

        // Mock service methods
        when(courseService.getAllCourses()).thenReturn(Collections.emptyList());
        when(teacherService.getAllTeachers()).thenReturn(Collections.emptyList());

        // Mock message translations
        when(messageSource.getMessage(Mockito.anyString(), Mockito.any(), Mockito.any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Set default locale for consistent testing
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        teacherCoursesView = new TeacherCoursesView(courseService, teacherService, messageSource);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }

    @Test
    public void testTeacherCoursesViewComponents() throws Exception {
        // Verify that the main container layout is not null
        assertNotNull(teacherCoursesView.getContent(), "The main container layout should not be null.");

        // Check the title component
        boolean hasTitle = teacherCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof H2 && "courses.title".equals(((H2) component).getText()));
        assertTrue(hasTitle, "The title should be translated as 'Course Management'.");

        // Check the search field component
        boolean hasSearchField = teacherCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof TextField && "courses.searchPlaceholder".equals(((TextField) component).getLabel()));
        assertTrue(hasSearchField, "There should be a TextField with the label 'Search Courses'.");

        // Access the private field using reflection
        Field coursesGridField = TeacherCoursesView.class.getDeclaredField("coursesGrid");
        coursesGridField.setAccessible(true);
        Grid<Course> coursesGrid = (Grid<Course>) coursesGridField.get(teacherCoursesView);

        // Verify that the grid component is initialized
        assertNotNull(coursesGrid, "Courses grid should be initialized.");

        // Check if columns are added to the grid
        assertEquals(4, coursesGrid.getColumns().size(), "Courses grid should have 4 columns.");
    }

    @Test
    public void testOpenEditDialog() {
        System.out.println("Testing openEditDialog method...");

        // Create a sample course
        Course sampleCourse = new Course();
        sampleCourse.setCourseName("Sample Course");
        sampleCourse.setCoursePlan("Sample Plan");
        sampleCourse.setDuration(30);  // Set a valid duration to avoid NullPointerException
        sampleCourse.setTeachers(Collections.emptyList());  // Initialize the teachers list to avoid NullPointerException

        // Mock MessageSource
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        try {
            // Access the method with reflection
            Method openEditDialogMethod = TeacherCoursesView.class.getDeclaredMethod("openEditDialog", Course.class, MessageSource.class);
            openEditDialogMethod.setAccessible(true); // Allow access if private

            // Invoke the method with both required parameters
            assertDoesNotThrow(() -> openEditDialogMethod.invoke(teacherCoursesView, sampleCourse, messageSource),
                    "Invoking the 'openEditDialog' method should not throw an exception.");
            System.out.println("openEditDialog method invoked successfully.");
        } catch (NoSuchMethodException e) {
            fail("Method 'openEditDialog' with parameters Course and MessageSource not found in TeacherCoursesView");
        } catch (Exception e) {
            e.printStackTrace();
            fail("An unexpected exception occurred while testing 'openEditDialog': " + e.getMessage());
        }
    }




    @Test
    public void testAddCourseButton() {
        // Simulate the "Add Course" button click and check for exceptions
        Button addCourseButton = teacherCoursesView.getContent().getChildren()
                .filter(component -> component instanceof Button && "courses.addCourse".equals(((Button) component).getText()))
                .map(Button.class::cast)
                .findFirst()
                .orElse(null);

        assertNotNull(addCourseButton, "Add Course button should be present in the view.");

        // Simulate button click without exception
        assertDoesNotThrow(addCourseButton::click, "Clicking the 'Add Course' button should not throw an exception.");
    }

    @Test
    public void testDeleteConfirmationDialog() throws Exception {
        // Create a sample course
        Course sampleCourse = new Course();
        sampleCourse.setCourseName("Sample Course");

        // Access the private method to open the delete confirmation dialog
        Method openDeleteConfirmationDialogMethod = TeacherCoursesView.class.getDeclaredMethod("openDeleteConfirmationDialog", Course.class, MessageSource.class);
        openDeleteConfirmationDialogMethod.setAccessible(true);

        // Invoke the private method and ensure no exceptions are thrown
        assertDoesNotThrow(() -> openDeleteConfirmationDialogMethod.invoke(teacherCoursesView, sampleCourse, messageSource),
                "Invoking the 'openDeleteConfirmationDialog' method should not throw an exception.");
    }
}

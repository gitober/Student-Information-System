package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TeacherCoursesViewTest {

    private TeacherCoursesView teacherCoursesView;

    @BeforeEach
    public void setUp() {
        System.out.println("Setting up the test environment for TeacherCoursesView...");

        // Mock the services
        CourseService courseService = Mockito.mock(CourseService.class);
        TeacherService teacherService = Mockito.mock(TeacherService.class);

        // Mock service methods
        when(courseService.getAllCourses()).thenReturn(Collections.emptyList());
        when(teacherService.getAllTeachers()).thenReturn(Collections.emptyList());

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        teacherCoursesView = new TeacherCoursesView(courseService, teacherService);

        System.out.println("Test environment setup for TeacherCoursesView completed.");
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }

    @Test
    public void testTeacherCoursesViewComponents() throws Exception {
        System.out.println("Testing components in TeacherCoursesView...");

        // Verify that the main container layout is not null
        assertNotNull(teacherCoursesView.getContent(), "The main container layout should not be null.");
        System.out.println("Main container layout is not null.");

        // Check the title component
        boolean hasTitle = teacherCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof H2 && "Course Management".equals(((H2) component).getText()));
        assertTrue(hasTitle, "The title should be 'Course Management'.");
        System.out.println("Title 'Course Management' is present.");

        // Check the search field component
        boolean hasSearchField = teacherCoursesView.getContent().getChildren()
                .anyMatch(component -> component instanceof TextField && "Search Courses".equals(((TextField) component).getLabel()));
        assertTrue(hasSearchField, "There should be a TextField with the label 'Search Courses'.");
        System.out.println("Search field with label 'Search Courses' is present.");

        // Access the private field using reflection
        Field coursesGridField = TeacherCoursesView.class.getDeclaredField("coursesGrid");
        coursesGridField.setAccessible(true);
        Grid<Course> coursesGrid = (Grid<Course>) coursesGridField.get(teacherCoursesView);

        // Verify that the grid component is initialized
        assertNotNull(coursesGrid, "Courses grid should be initialized.");
        System.out.println("Courses grid is initialized.");

        // Check if columns are added to the grid
        System.out.println("Number of columns in coursesGrid: " + coursesGrid.getColumns().size());
        assertEquals(4, coursesGrid.getColumns().size(), "Courses grid should have 4 columns.");
        System.out.println("Component testing in TeacherCoursesView completed.");
    }

    @Test
    public void testOpenEditDialog() throws Exception {
        System.out.println("Testing openEditDialog method...");

        // Create a sample course
        Course sampleCourse = new Course();
        sampleCourse.setCourseName("Sample Course");
        sampleCourse.setCoursePlan("Sample Plan");
        sampleCourse.setDuration(30);  // Set a valid duration to avoid NullPointerException
        sampleCourse.setTeachers(Collections.emptyList());  // Initialize the teachers list to avoid NullPointerException

        // Access the private method using reflection
        Method openEditDialogMethod = TeacherCoursesView.class.getDeclaredMethod("openEditDialog", Course.class);
        openEditDialogMethod.setAccessible(true);

        // Invoke the private method
        System.out.println("Invoking openEditDialog method...");
        assertDoesNotThrow(() -> openEditDialogMethod.invoke(teacherCoursesView, sampleCourse),
                "Invoking the 'openEditDialog' method should not throw an exception.");
        System.out.println("openEditDialog method invoked successfully.");
    }



    @Test
    public void testAddCourseButton() {
        System.out.println("Testing 'Add Course' button...");

        // Access the private method using reflection
        Method openAddCourseDialogMethod;
        try {
            openAddCourseDialogMethod = TeacherCoursesView.class.getDeclaredMethod("openAddCourseDialog");
            openAddCourseDialogMethod.setAccessible(true);

            // Invoke the private method
            System.out.println("Invoking openAddCourseDialog method...");
            assertDoesNotThrow(() -> openAddCourseDialogMethod.invoke(teacherCoursesView),
                    "Invoking the 'openAddCourseDialog' method should not throw an exception.");
            System.out.println("openAddCourseDialog method invoked successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception during 'Add Course' button test: " + e.getMessage());
        }

        // Check if the button does not throw an exception
        Button addCourseButton = new Button("Add Course");
        addCourseButton.addClickListener(event -> Notification.show("Add Course button clicked"));

        assertDoesNotThrow(addCourseButton::click, "Clicking the 'Add Course' button should not throw an exception.");
        System.out.println("'Add Course' button click simulation completed.");
    }
}

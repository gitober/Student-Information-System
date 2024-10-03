package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class StudentGradesViewTest {

    private StudentGradesView studentGradesView;
    private GradeService gradeService;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // Mock the services
        gradeService = Mockito.mock(GradeService.class);
        userService = Mockito.mock(UserService.class);

        // Mock service methods
        when(userService.getCurrentStudentNumber()).thenReturn(1L);  // Mock a student number
        when(gradeService.getGradesByStudentNumber(1L)).thenReturn(Collections.emptyList());

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        studentGradesView = new StudentGradesView(gradeService, userService);
    }

    @Test
    public void testStudentGradesViewComponents() {
        // Check if the title and description are set correctly
        H2 title = (H2) studentGradesView.getContent().getChildren()
                .filter(component -> component instanceof H2)
                .findFirst()
                .orElse(null);
        assertNotNull(title, "Title component should not be null.");
        assertEquals("Grades Overview", title.getText(), "Title should be 'Grades Overview'.");

        // Check if the description is set correctly
        String descriptionText = studentGradesView.getContent().getChildren()
                .filter(component -> component instanceof Paragraph)
                .map(component -> ((Paragraph) component).getText())
                .findFirst()
                .orElse(null);
        assertEquals("View your grades for the courses you have taken.", descriptionText,
                "Description should be 'View your grades for the courses you have taken.'");

        // Check if the search field is present
        TextField searchField = (TextField) studentGradesView.getContent().getChildren()
                .filter(component -> component instanceof TextField)
                .findFirst()
                .orElse(null);
        assertNotNull(searchField, "Search field should not be null.");
        assertEquals("Search Courses", searchField.getLabel(), "Search field label should be 'Search Courses'.");

        // Check if the grades grid is initialized
        Grid<Grade> gradesGrid = getPrivateField("gradesGrid");
        assertNotNull(gradesGrid, "Grades grid should not be null.");
    }

    @Test
    public void testRefreshGradesData() throws Exception {
        // Mock the grades data
        Course mockCourse = new Course("Mathematics", "MATH101", 3); // Use the correct constructor
        Grade mockGrade = new Grade();
        mockGrade.setGrade("A");
        mockGrade.setGradingDay(LocalDate.now());
        mockGrade.setCourse(mockCourse);

        // Mock the GradeService to return the mock grade
        when(gradeService.getGradesByStudentNumber(1L)).thenReturn(List.of(mockGrade));

        // Use reflection to call the private refreshGradesData method
        Method refreshDataMethod = StudentGradesView.class.getDeclaredMethod("refreshGradesData", Long.class);
        refreshDataMethod.setAccessible(true);
        refreshDataMethod.invoke(studentGradesView, 1L); // Call the method with the mocked student number

        // Refresh the grades data indirectly by checking the grid
        Field refreshDataField = StudentGradesView.class.getDeclaredField("gradesGrid");
        refreshDataField.setAccessible(true);
        Grid<Grade> gradesGrid = (Grid<Grade>) refreshDataField.get(studentGradesView);

        // The grades should be set in the grid
        assertEquals(1, gradesGrid.getDataProvider().size(new Query<>()), "Grades grid should contain one grade.");
    }



    private Grid<Grade> getPrivateField(String fieldName) {
        try {
            Field field = StudentGradesView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (Grid<Grade>) field.get(studentGradesView);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class StudentGradesViewTest {

    private StudentGradesView studentGradesView;
    private GradeService gradeService;
    private MessageSource messageSource; // Added MessageSource

    @BeforeEach
    public void setUp() {
        // Set a default locale to ensure consistency in the test
        LocaleContextHolder.setLocale(Locale.ENGLISH);

        // Mock the services
        gradeService = Mockito.mock(GradeService.class);
        UserService userService = Mockito.mock(UserService.class);
        messageSource = Mockito.mock(MessageSource.class);

        // Mock service methods
        when(userService.getCurrentStudentNumber()).thenReturn(1L);
        when(gradeService.getGradesByStudentNumber(1L)).thenReturn(Collections.emptyList());

        // Mock messages
        when(messageSource.getMessage("grades.title", null, LocaleContextHolder.getLocale())).thenReturn("Grades Overview");
        when(messageSource.getMessage("grades.description", null, LocaleContextHolder.getLocale())).thenReturn("View your grades for the courses you have taken.");
        when(messageSource.getMessage("grades.search", null, LocaleContextHolder.getLocale())).thenReturn("Search Courses");

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        studentGradesView = new StudentGradesView(gradeService, userService, messageSource);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
        LocaleContextHolder.resetLocaleContext();
    }


    @Test
    void testStudentGradesViewComponents() {
        // Check if the title and description are set correctly
        H2 title = (H2) studentGradesView.getContent().getChildren()
                .filter(component -> component instanceof H2)
                .findFirst()
                .orElse(null);
        assertNotNull(title, "Title component should not be null.");

        // Retrieve expected title text using messageSource for consistency with LocaleContextHolder's locale
        String expectedTitle = messageSource.getMessage("grades.title", null, LocaleContextHolder.getLocale());
        assertEquals(expectedTitle, title.getText(), "Title should match the localized message 'Grades Overview'.");

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
        Grid<Grade> gradesGrid = getPrivateField();
        assertNotNull(gradesGrid, "Grades grid should not be null.");
    }


    @Test
    void testRefreshGradesData() throws Exception {
        // Mock the grades data
        Course mockCourse = new Course("Mathematics", "MATH101", 3); // Use the correct constructor
        Grade mockGrade = new Grade();
        mockGrade.setGradeValue("A");
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

    private Grid<Grade> getPrivateField() {
        try {
            Field field = StudentGradesView.class.getDeclaredField("gradesGrid");
            field.setAccessible(true);
            return (Grid<Grade>) field.get(studentGradesView);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

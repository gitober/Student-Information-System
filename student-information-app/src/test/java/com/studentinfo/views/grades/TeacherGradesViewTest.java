package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.StudentService;
import com.studentinfo.services.DateService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TeacherGradesViewTest {

    private TeacherGradesView teacherGradesView;
    private GradeService gradeService;
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        // Mock the services
        gradeService = Mockito.mock(GradeService.class);
        courseService = Mockito.mock(CourseService.class);
        StudentService studentService = Mockito.mock(StudentService.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);
        DateService dateService = Mockito.mock(DateService.class);

        // Mock service methods
        Course mockCourse = new Course("Mathematics", "MATH101", 3);
        when(courseService.getAllCourses()).thenReturn(List.of(mockCourse));
        when(gradeService.getGradesByCourseId(mockCourse.getCourseId())).thenReturn(Collections.emptyList());

        // Mock MessageSource for translations with a Locale default
        when(messageSource.getMessage("grades.management.title", null, Locale.getDefault())).thenReturn("Grades Management");
        when(messageSource.getMessage("grades.management.description", null, Locale.getDefault()))
                .thenReturn("Manage and update student grades for selected courses.");

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        teacherGradesView = new TeacherGradesView(gradeService, courseService, studentService, dateService, messageSource);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }

    @Test
    public void testTeacherGradesViewComponents() {
        // Ensure the title and description are set correctly
        H2 title = (H2) teacherGradesView.getContent().getChildren()
                .filter(component -> component instanceof H2)
                .findFirst()
                .orElse(null);
        assertNotNull(title, "Title component should not be null.");

        // Check if the description is set correctly
        Paragraph description = (Paragraph) teacherGradesView.getContent().getChildren()
                .filter(component -> component instanceof Paragraph)
                .findFirst()
                .orElse(null);
        assertNotNull(description, "Description component should not be null.");

        // Ensure the course ComboBox is present
        ComboBox<Course> courseComboBox = getComboBoxField();
        assertNotNull(courseComboBox, "Course ComboBox should not be null.");

        // Ensure the grades grid is initialized
        Grid<Grade> gradesGrid = getPrivateField();
        assertNotNull(gradesGrid, "Grades grid should not be null.");
    }

    @Test
    public void testRefreshGradesData() throws Exception {
        // Prepare a mock course and grade
        Course mockCourse = new Course("Mathematics", "MATH101", 3);
        when(courseService.getAllCourses()).thenReturn(List.of(mockCourse));

        Grade mockGrade = new Grade();
        mockGrade.setGrade("A");
        mockGrade.setCourse(mockCourse);
        when(gradeService.getGradesByCourseId(mockCourse.getCourseId())).thenReturn(List.of(mockGrade));

        // Set items in the ComboBox before setting a value
        ComboBox<Course> courseComboBox = getComboBoxField();
        courseComboBox.setItems(List.of(mockCourse));
        courseComboBox.setValue(mockCourse);

        // Use reflection to call the private refreshGradesData method
        Method refreshDataMethod = TeacherGradesView.class.getDeclaredMethod("refreshGradesData");
        refreshDataMethod.setAccessible(true);
        refreshDataMethod.invoke(teacherGradesView);

        // Verify that the gradesGrid is updated with the mock grade
        Grid<Grade> gradesGrid = getPrivateField();
        ListDataProvider<Grade> dataProvider = (ListDataProvider<Grade>) gradesGrid.getDataProvider();

        // Check the size of items in the data provider
        assertEquals(1, dataProvider.getItems().size(), "Grades grid should contain one grade.");
    }

    private ComboBox<Course> getComboBoxField() {
        try {
            Field field = TeacherGradesView.class.getDeclaredField("courseComboBox");
            field.setAccessible(true);
            return (ComboBox<Course>) field.get(teacherGradesView);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Grid<Grade> getPrivateField() {
        try {
            Field field = TeacherGradesView.class.getDeclaredField("gradesGrid");
            field.setAccessible(true);
            return (Grid<Grade>) field.get(teacherGradesView);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

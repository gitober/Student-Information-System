package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.StudentService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.data.provider.ListDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

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

        // Mock service methods
        Course mockCourse = new Course("Mathematics", "MATH101", 3);
        when(courseService.getAllCourses()).thenReturn(List.of(mockCourse));
        when(gradeService.getGradesByCourseId(mockCourse.getCourseId())).thenReturn(Collections.emptyList());

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked services
        teacherGradesView = new TeacherGradesView(gradeService, courseService, studentService);

        // Set up a ComboBox directly (if it exists in your class)
        // Alternatively, use reflection if it needs to be private
        try {
            Field comboBoxField = TeacherGradesView.class.getDeclaredField("courseComboBox"); // Assuming it's named courseComboBox
            comboBoxField.setAccessible(true);
            ComboBox<Course> mockComboBox = new ComboBox<>();
            comboBoxField.set(teacherGradesView, mockComboBox);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Could not set ComboBox field.", e);
        }
    }

    @Test
    public void testTeacherGradesViewComponents() {
        // Check if the title and description are set correctly
        H2 title = (H2) teacherGradesView.getContent().getChildren()
                .filter(component -> component instanceof H2)
                .findFirst()
                .orElse(null);
        assertNotNull(title, "Title component should not be null.");
        assertEquals("Grades Management", title.getText(), "Title should be 'Grades Management'.");

        // Check if the description is set correctly
        String descriptionText = teacherGradesView.getContent().getChildren()
                .filter(component -> component instanceof Paragraph)
                .map(component -> ((Paragraph) component).getText())
                .findFirst()
                .orElse(null);
        assertEquals("Manage and update student grades for selected courses.", descriptionText,
                "Description should be 'Manage and update student grades for selected courses.'");

        // Check if the course ComboBox is present
        ComboBox<Course> courseComboBox = getComboBoxField(); // Assuming it's named courseComboBox
        assertNotNull(courseComboBox, "Course ComboBox should not be null.");

        // Check if the grades grid is initialized
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
        courseComboBox.setItems(List.of(mockCourse)); // Set the items in the ComboBox
        courseComboBox.setValue(mockCourse); // Now you can set the value

        // Use reflection to call the private refreshGradesData method
        Method refreshDataMethod = TeacherGradesView.class.getDeclaredMethod("refreshGradesData");
        refreshDataMethod.setAccessible(true);
        refreshDataMethod.invoke(teacherGradesView); // Call the method

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

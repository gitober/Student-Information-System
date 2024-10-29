package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Grade;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@CssImport("./themes/studentinformationapp/views/grades-view/student-grades-view.css")
@SpringComponent
@UIScope
public class StudentGradesView extends Composite<VerticalLayout> {

    private final GradeService gradeService;
    private final UserService userService;
    private final MessageSource messageSource;
    private final Grid<Grade> gradesGrid;

    @Autowired
    public StudentGradesView(GradeService gradeService, UserService userService, MessageSource messageSource) {
        this.gradeService = gradeService;
        this.userService = userService;
        this.messageSource = messageSource;

        getContent().addClassName("student-grades-view-container");

        // Use current locale for messages
        Locale currentLocale = LocaleContextHolder.getLocale();

        // Page title
        H2 title = new H2(messageSource.getMessage("grades.title", null, currentLocale));
        title.addClassName("student-grades-view-title");

        Paragraph description = new Paragraph(messageSource.getMessage("grades.description", null, currentLocale));
        description.addClassName("student-grades-view-description");

        // Search bar to filter courses
        TextField searchField = new TextField(messageSource.getMessage("grades.search", null, currentLocale));
        searchField.addClassName("student-grades-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterGrades(event.getValue()));

        // Grid for displaying grades
        gradesGrid = new Grid<>();
        gradesGrid.addClassName("student-grades-view-grid");

        // Configure columns with keys for CSS styling
        gradesGrid.addColumn(grade -> grade.getCourse().getCourseName())
                .setHeader(messageSource.getMessage("grades.course", null, currentLocale))
                .setKey("course-name");
        gradesGrid.addColumn(Grade::getGrade)
                .setHeader(messageSource.getMessage("grades.grade", null, currentLocale))
                .setKey("grade");
        gradesGrid.addColumn(grade -> grade.getGradingDay().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .setHeader(messageSource.getMessage("grades.date", null, currentLocale))
                .setKey("grading-date");

        // Fetch and display grades for the current student
        Long studentNumber = userService.getCurrentStudentNumber();
        if (studentNumber != null) {
            refreshGradesData(studentNumber);
        } else {
            System.out.println(messageSource.getMessage("grades.view.error.student.not.found", null, currentLocale));
        }

        // Add components to the layout
        getContent().add(title, description, searchField, gradesGrid);
    }

    // Method to fetch grades and populate the grid
    private void refreshGradesData(Long studentNumber) {
        List<Grade> studentGrades = gradeService.getGradesByStudentNumber(studentNumber);
        gradesGrid.setItems(studentGrades); // Use actual data from service
    }

    // Method to filter grades based on the search term
    private void filterGrades(String searchTerm) {
        List<Grade> filteredGrades = gradesGrid.getListDataView().getItems().toList()
                .stream()
                .filter(grade -> grade.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        gradesGrid.setItems(filteredGrades);
    }
}

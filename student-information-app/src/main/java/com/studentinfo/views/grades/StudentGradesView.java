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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@CssImport("./themes/studentinformationapp/views/grades-view/student-grades-view.css")
@SpringComponent
@UIScope
public class StudentGradesView extends Composite<VerticalLayout> {

    private static final Logger logger = LoggerFactory.getLogger(StudentGradesView.class);
    private final transient GradeService gradeService;
    private final Grid<Grade> gradesGrid;

    @Autowired
    public StudentGradesView(GradeService gradeService, UserService userService, MessageSource messageSource) {
        this.gradeService = gradeService;

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
                .setKey("course-name")
                .getElement().getClassList().add("student-grades-view-course-column"); // Updated to set CSS class

        gradesGrid.addColumn(Grade::getGradeValue) // Updated to getGradeValue
                .setHeader(messageSource.getMessage("grades.grade", null, currentLocale))
                .setKey("grade")
                .getElement().getClassList().add("student-grades-view-grade-column"); // Updated to set CSS class

        gradesGrid.addColumn(grade -> formatDate(grade.getGradingDay()))
                .setHeader(messageSource.getMessage("grades.date", null, currentLocale))
                .setKey("grading-date")
                .getElement().getClassList().add("student-grades-view-date-column"); // Updated to set CSS class

        // Fetch and display grades for the current student
        Long studentNumber = userService.getCurrentStudentNumber();
        if (studentNumber != null) {
            refreshGradesData(studentNumber);
        } else {
            if (logger.isErrorEnabled()) {
                logger.error(messageSource.getMessage("grades.view.error.student.not.found", null, currentLocale));
            }
        }

        // Add components to the layout
        getContent().add(title, description, searchField, gradesGrid);
    }

    // Method to fetch grades and populate the grid
    private void refreshGradesData(Long studentNumber) {
        List<Grade> studentGrades = gradeService.getGradesByStudentNumber(studentNumber);
        gradesGrid.setItems(studentGrades);
    }

    // Method to filter grades based on the search term
    private void filterGrades(String searchTerm) {
        List<Grade> filteredGrades = gradesGrid.getListDataView().getItems().toList()
                .stream()
                .filter(grade -> grade.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();

        gradesGrid.setItems(filteredGrades);
    }

    private String formatDate(java.time.LocalDate date) {
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                locale.getLanguage().equals("ch") ? "yyyy年MM月dd日" : "dd/MM/yyyy",
                locale
        );
        return date.format(formatter);
    }
}

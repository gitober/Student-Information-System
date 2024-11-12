package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.entity.Student;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.StudentService;
import com.studentinfo.services.DateService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.util.List;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/grades-view/teacher-grades-view.css")
public class TeacherGradesView extends Composite<VerticalLayout> {

    private static final Logger logger = LoggerFactory.getLogger(TeacherGradesView.class);

    private final transient GradeService gradeService;
    private final transient CourseService courseService;
    private final transient StudentService studentService;
    private final transient MessageSource messageSource;

    private final Grid<Grade> gradesGrid;
    private transient List<Grade> gradeEntries;
    private final ComboBox<Course> courseComboBox;

    @Autowired
    public TeacherGradesView(GradeService gradeService, CourseService courseService, StudentService studentService, DateService dateService, MessageSource messageSource) {
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.studentService = studentService;
        this.messageSource = messageSource;

        getContent().addClassName("teacher-grades-view-container");

        // Page title
        H2 title = new H2(getMessage("grades.management.title"));
        title.addClassName("teacher-grades-view-title");

        // Description
        Paragraph description = new Paragraph(getMessage("grades.management.description"));
        description.addClassName("teacher-grades-view-description");

        // Course ComboBox
        courseComboBox = new ComboBox<>(getMessage("grades.management.select.course"));
        courseComboBox.addClassName("teacher-grades-view-course-combobox");
        courseComboBox.setItemLabelGenerator(Course::getCourseName);
        refreshCourseData();
        courseComboBox.addValueChangeListener(event -> refreshGradesData());

        // Search field
        TextField searchField = new TextField(getMessage("grades.management.search.student"));
        searchField.addClassName("teacher-grades-view-search-field");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterGradesByStudent(event.getValue()));

        // Grades Grid
        gradesGrid = new Grid<>(Grade.class, false);
        gradesGrid.addClassName("teacher-grades-view-grid");

        // Columns for the grid
        gradesGrid.addColumn(grade -> grade.getCourse().getCourseName())
                .setHeader(getMessage("grades.management.course.column"))
                .setKey("course-column");

        gradesGrid.addColumn(grade -> {
                    Student student = studentService.getStudentByNumber(grade.getStudentNumber()).orElse(null);
                    return student != null ? student.getFirstName() + " " + student.getLastName() : getMessage("grades.management.unknown.student");
                }).setHeader(getMessage("grades.management.student.column"))
                .setKey("student-column");


        gradesGrid.addColumn(Grade::getGradeValue)
                .setHeader(getMessage("grades.management.grade.column"))
                .setKey("grade-column");

        gradesGrid.addComponentColumn(this::createEditAndDeleteButtons)
                .setHeader(getMessage("grades.management.actions.column"))
                .setKey("action-column");

        // Add Grade Button
        Button addGradeButton = new Button(getMessage("grades.management.add.grade.button"));
        addGradeButton.addClassName("teacher-grades-view-add-grade-button");
        addGradeButton.addClickListener(event -> openAddGradeDialog());

        // Add components to the view
        getContent().add(title, description, courseComboBox, searchField, gradesGrid, addGradeButton);

        // Load real data
        refreshGradesData();
    }

    // Retrieve localized message
    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    // Refresh grades data based on selected course
    private void refreshGradesData() {
        Course selectedCourse = courseComboBox.getValue();
        if (selectedCourse != null) {
            gradeEntries = gradeService.getGradesByCourseId(selectedCourse.getCourseId());
        } else {
            gradeEntries = List.of();
        }
        gradesGrid.setItems(gradeEntries);
    }

    // Refresh the course ComboBox with the latest courses
    private void refreshCourseData() {
        List<Course> allCourses = courseService.getAllCourses();
        courseComboBox.setItems(allCourses);
        if (!allCourses.isEmpty()) {
            courseComboBox.setValue(allCourses.getFirst()); // Optionally, auto-select the first course
        }
    }

    // Filter grades by student name
    private void filterGradesByStudent(String searchTerm) {
        List<Grade> filteredGrades = gradeEntries.stream()
                .filter(entry ->
                        studentService.getStudentByNumber(entry.getStudentNumber())
                                .map(student -> (student.getFirstName() + " " + student.getLastName()).toLowerCase().contains(searchTerm.toLowerCase()))
                                .orElse(false) // If no student is found, return false
                )
                .toList(); // Replacing collect(Collectors.toList()) with toList()
        gradesGrid.setItems(filteredGrades);
    }

    // Dialog for adding a new grade
    private void openAddGradeDialog() {
        Dialog addGradeDialog = new Dialog();
        addGradeDialog.addClassName("teacher-grades-view-add-grade-dialog");

        // Course ComboBox inside the dialog
        ComboBox<Course> dialogCourseComboBox = new ComboBox<>(getMessage("grades.management.select.course"));
        dialogCourseComboBox.addClassName("teacher-grades-view-course-combobox-dialog");
        dialogCourseComboBox.setItemLabelGenerator(Course::getCourseName);
        dialogCourseComboBox.setItems(courseService.getAllCourses());

        // Student ComboBox inside the dialog
        ComboBox<Student> studentComboBox = new ComboBox<>(getMessage("grades.management.select.student"));
        studentComboBox.addClassName("teacher-grades-view-student-combobox-dialog");
        studentComboBox.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());

        dialogCourseComboBox.addValueChangeListener(event -> {
            Course selectedCourse = event.getValue();
            if (selectedCourse != null) {
                List<Student> enrolledStudents = studentService.getStudentsByCourseId(selectedCourse.getCourseId());
                studentComboBox.setItems(enrolledStudents);
            } else {
                studentComboBox.clear();
            }
        });

        TextField gradeField = new TextField(getMessage("grades.management.grade.input"));
        gradeField.addClassName("teacher-grades-view-grade-field");
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        Button saveButton = new Button(getMessage("grades.management.save.button"));
        saveButton.addClassName("teacher-grades-view-save-button");
        saveButton.addClickListener(event -> {
            Course selectedCourse = dialogCourseComboBox.getValue();
            Student selectedStudent = studentComboBox.getValue();
            String gradeValue = gradeField.getValue();

            if (selectedCourse == null || selectedStudent == null || gradeValue.isEmpty()) {
                Notification.show(getMessage("grades.management.validation.error"));
                return;
            }

            Grade newGrade = new Grade();
            newGrade.setCourse(selectedCourse);
            newGrade.setStudentNumber(selectedStudent.getStudentNumber());
            newGrade.setGradeValue(gradeValue);
            newGrade.setGradingDay(LocalDate.now());

            try {
                gradeService.saveGrade(newGrade);
                Notification.show(getMessage("grades.management.add.success"));
                refreshGradesData();
                refreshCourseData();
            } catch (Exception e) {
                logger.error("Error saving grade: {}", e.getMessage());
                Notification.show(getMessage("grades.management.add.error"));
            }

            addGradeDialog.close();
        });

        Button cancelButton = new Button(getMessage("grades.management.cancel.button"));
        cancelButton.addClassName("teacher-grades-view-cancel-button");
        cancelButton.addClickListener(event -> addGradeDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-dialog-buttons");

        HorizontalLayout comboBoxLayout = new HorizontalLayout(dialogCourseComboBox, studentComboBox);
        comboBoxLayout.addClassName("teacher-grades-view-combobox-layout");
        comboBoxLayout.setSpacing(true);

        addGradeDialog.add(new H2(getMessage("grades.management.add.title")), comboBoxLayout, gradeField, dialogButtons);
        addGradeDialog.open();
    }

    // Helper method to create edit and delete buttons for each grade row
    private HorizontalLayout createEditAndDeleteButtons(Grade grade) {
        Button editButton = new Button(getMessage("grades.management.edit.button"));
        editButton.addClassName("teacher-grades-view-edit-button");
        editButton.addClickListener(event -> openEditGradeDialog(grade));

        Button deleteButton = new Button(getMessage("grades.management.delete.button"));
        deleteButton.addClassName("teacher-grades-view-delete-button");
        deleteButton.addClickListener(event -> {
            try {
                gradeService.deleteGrade(grade.getGradeId());
                Notification.show(getMessage("grades.management.delete.success"));
                refreshGradesData();
            } catch (Exception e) {
                logger.error("Error deleting grade: {}", e.getMessage());
                Notification.show(getMessage("grades.management.delete.error"));
            }
        });

        HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
        actionButtons.addClassName("teacher-grades-view-action-buttons");
        return actionButtons;
    }

    // Dialog for editing a grade
    private void openEditGradeDialog(Grade grade) {
        Dialog editGradeDialog = new Dialog();
        editGradeDialog.addClassName("teacher-grades-view-edit-grade-dialog");

        TextField gradeField = new TextField(getMessage("grades.management.grade.input"));
        gradeField.setValue(grade.getGradeValue());
        gradeField.addClassName("teacher-grades-view-edit-grade-field");
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        Button saveButton = new Button(getMessage("grades.management.save.button"));
        saveButton.addClassName("teacher-grades-view-edit-save-button");
        saveButton.addClickListener(event -> {
            String gradeValue = gradeField.getValue();

            if (gradeValue.isEmpty()) {
                Notification.show(getMessage("grades.management.validation.error"));
                return;
            }

            grade.setGradeValue(gradeValue);
            try {
                gradeService.saveGrade(grade);
                Notification.show(getMessage("grades.management.update.success"));
                refreshGradesData();
            } catch (Exception e) {
                logger.error("Error updating grade: {}", e.getMessage());
                Notification.show(getMessage("grades.management.update.error"));
            }

            editGradeDialog.close();
        });

        Button cancelButton = new Button(getMessage("grades.management.cancel.button"));
        cancelButton.addClassName("teacher-grades-view-edit-cancel-button");
        cancelButton.addClickListener(event -> editGradeDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-edit-dialog-buttons");

        editGradeDialog.add(new H2(getMessage("grades.management.edit.title")), gradeField, dialogButtons);
        editGradeDialog.open();
    }
}

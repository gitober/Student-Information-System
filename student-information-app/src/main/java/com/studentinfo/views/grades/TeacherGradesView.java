package com.studentinfo.views.grades;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.entity.Student;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.StudentService;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/grades-view/teacher-grades-view.css")
public class TeacherGradesView extends Composite<VerticalLayout> {

    private final GradeService gradeService;
    private final CourseService courseService;
    private final StudentService studentService;

    private Grid<Grade> gradesGrid;
    private List<Grade> gradeEntries;
    private ComboBox<Course> courseComboBox;

    public TeacherGradesView(GradeService gradeService, CourseService courseService, StudentService studentService) {
        this.gradeService = gradeService;
        this.courseService = courseService;
        this.studentService = studentService;

        getContent().addClassName("teacher-grades-view-container");

        // Page title
        H2 title = new H2("Grades Management");
        title.addClassName("teacher-grades-view-title");

        // User manual/description
        Paragraph description = new Paragraph("Manage and update student grades for selected courses.");
        description.addClassName("teacher-grades-view-description");

        // Dropdown for selecting the course
        courseComboBox = new ComboBox<>("Select Course");
        courseComboBox.addClassName("teacher-grades-view-course-combobox");
        courseComboBox.setItemLabelGenerator(Course::getCourseName);
        courseComboBox.setItems(courseService.getAllCourses());
        courseComboBox.addValueChangeListener(event -> refreshGradesData());

        // Search bar to filter grades by student
        TextField searchField = new TextField("Search by Student");
        searchField.addClassName("teacher-grades-view-search-field");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterGradesByStudent(event.getValue()));

        // Grid for displaying grades
        gradesGrid = new Grid<>(Grade.class, false);
        gradesGrid.addClassName("teacher-grades-view-grid");

        // Add columns to the grid
        gradesGrid.addColumn(grade -> grade.getCourse().getCourseName())
                .setHeader("Course")
                .setClassNameGenerator(entry -> "teacher-grades-view-course-column");

        // Display student's full name instead of student number
        gradesGrid.addColumn(grade -> {
                    Optional<Student> studentOpt = Optional.ofNullable(studentService.getStudentByNumber(grade.getStudentNumber()));
                    return studentOpt.map(student -> student.getFirstName() + " " + student.getLastName())
                            .orElse("Unknown Student");
                }).setHeader("Student")
                .setClassNameGenerator(entry -> "teacher-grades-view-student-column");

        gradesGrid.addColumn(Grade::getGrade)
                .setHeader("Grade")
                .setClassNameGenerator(entry -> "teacher-grades-view-grade-column");

        // Add action buttons to the grid
        gradesGrid.addComponentColumn(this::createEditAndDeleteButtons).setHeader("Actions")
                .setClassNameGenerator(entry -> "teacher-grades-view-action-column");

        // Load real data from the GradeService
        refreshGradesData();

        // Add "Add Grade" button
        Button addGradeButton = new Button("Add Grade");
        addGradeButton.addClassName("teacher-grades-view-add-grade-button");
        addGradeButton.addClickListener(event -> openAddGradeDialog());

        // Add components to the view
        getContent().add(title, description, courseComboBox, searchField, gradesGrid, addGradeButton);
    }

    private void refreshGradesData() {
        Course selectedCourse = courseComboBox.getValue(); // Get the selected course
        if (selectedCourse != null) {
            gradeEntries = gradeService.getGradesByCourseId(selectedCourse.getCourseId());
        } else {
            gradeEntries = List.of(); // Clear grades if no course is selected
        }
        gradesGrid.setItems(gradeEntries); // Set grid data with real grades
    }

    private void filterGradesByStudent(String searchTerm) {
        List<Grade> filteredGrades = gradeEntries.stream()
                .filter(entry -> {
                    Optional<Student> studentOpt = Optional.ofNullable(studentService.getStudentByNumber(entry.getStudentNumber()));
                    return studentOpt.map(student ->
                                    (student.getFirstName() + " " + student.getLastName()).toLowerCase().contains(searchTerm.toLowerCase()))
                            .orElse(false);
                })
                .collect(Collectors.toList());

        gradesGrid.setItems(filteredGrades);
    }

    private void openAddGradeDialog() {
        Dialog addGradeDialog = new Dialog();
        addGradeDialog.addClassName("teacher-grades-view-add-grade-dialog");

        // Dropdown for selecting the course (only inside the dialog)
        ComboBox<Course> courseComboBox = new ComboBox<>("Select Course");
        courseComboBox.addClassName("teacher-grades-view-course-combobox-dialog");
        courseComboBox.setItemLabelGenerator(Course::getCourseName);
        courseComboBox.setItems(courseService.getAllCourses());

        // Dropdown for selecting the student
        ComboBox<Student> studentComboBox = new ComboBox<>("Select Student");
        studentComboBox.addClassName("teacher-grades-view-student-combobox-dialog");
        studentComboBox.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());

        // Populate students once a course is selected
        courseComboBox.addValueChangeListener(event -> {
            Course selectedCourse = event.getValue();
            if (selectedCourse != null) {
                List<Student> enrolledStudents = studentService.getStudentsByCourseId(selectedCourse.getCourseId());
                studentComboBox.setItems(enrolledStudents);
            } else {
                studentComboBox.clear();
            }
        });

        // Text field for entering a numeric grade between 1-5 or "Fail"
        TextField gradeField = new TextField("Grade (1.0 - 5.0 or 'Fail')");
        gradeField.addClassName("teacher-grades-view-grade-field");
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        // Save button to save the new grade
        Button saveButton = new Button("Save");
        saveButton.addClassName("teacher-grades-view-save-button");
        saveButton.addClickListener(event -> {
            Course selectedCourse = courseComboBox.getValue();
            Student selectedStudent = studentComboBox.getValue();
            String gradeValue = gradeField.getValue();

            if (selectedCourse == null || selectedStudent == null || gradeValue.isEmpty()) {
                Notification.show("Please select a course, a student, and enter a valid grade.");
                return;
            }

            // Validate if grade is a number between 1.0 and 5.0 or "Fail"
            if (!gradeValue.equalsIgnoreCase("Fail")) {
                try {
                    double numericGrade = Double.parseDouble(gradeValue);
                    if (numericGrade < 1.0 || numericGrade > 5.0) {
                        Notification.show("Grade must be between 1.0 and 5.0 or 'Fail'.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    Notification.show("Invalid grade format. Please enter a number between 1.0 and 5.0 or 'Fail'.");
                    return;
                }
            }

            // Create a new grade entity
            Grade newGrade = new Grade();
            newGrade.setCourse(selectedCourse);
            newGrade.setStudentNumber(selectedStudent.getStudentNumber());
            newGrade.setGrade(gradeValue);
            newGrade.setGradingDay(LocalDate.now());

            // Save the grade via the GradeService
            gradeService.saveGrade(newGrade);

            // Refresh the grid with updated data
            refreshGradesData();

            // Notify user of successful addition
            Notification.show("New grade added successfully.");

            // Close the dialog
            addGradeDialog.close();
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("teacher-grades-view-cancel-button");
        cancelButton.addClickListener(event -> addGradeDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-dialog-buttons");

        // Add components to the dialog
        addGradeDialog.add(new H2("Add New Grade"), courseComboBox, studentComboBox, gradeField, dialogButtons);
        addGradeDialog.open();
    }

    private HorizontalLayout createEditAndDeleteButtons(Grade grade) {
        Button editButton = new Button("Edit");
        editButton.addClassName("teacher-grades-view-edit-button");
        editButton.addClickListener(event -> openEditGradeDialog(grade));

        Button deleteButton = new Button("Delete");
        deleteButton.addClassName("teacher-grades-view-delete-button");
        deleteButton.addClickListener(event -> {
            gradeService.deleteGrade(grade.getGradeId());
            refreshGradesData();
            Notification.show("Grade deleted successfully.");
        });

        HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
        actionButtons.addClassName("teacher-grades-view-action-buttons");
        return actionButtons;
    }

    private void openEditGradeDialog(Grade grade) {
        Dialog editGradeDialog = new Dialog();
        editGradeDialog.addClassName("teacher-grades-view-edit-grade-dialog");

        // Text field for editing the grade
        TextField gradeField = new TextField("Grade (1.0 - 5.0 or 'Fail')");
        gradeField.setValue(grade.getGrade());
        gradeField.addClassName("teacher-grades-view-edit-grade-field");
        gradeField.setValueChangeMode(ValueChangeMode.EAGER);

        // Save button to update the grade
        Button saveButton = new Button("Save");
        saveButton.addClassName("teacher-grades-view-edit-save-button");
        saveButton.addClickListener(event -> {
            String gradeValue = gradeField.getValue();

            if (gradeValue.isEmpty()) {
                Notification.show("Please enter a grade.");
                return;
            }

            // Validate if grade is a number between 1.0 and 5.0 or "Fail"
            if (!gradeValue.equalsIgnoreCase("Fail")) {
                try {
                    double numericGrade = Double.parseDouble(gradeValue);
                    if (numericGrade < 1.0 || numericGrade > 5.0) {
                        Notification.show("Grade must be between 1.0 and 5.0 or 'Fail'.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    Notification.show("Invalid grade format. Please enter a number between 1.0 and 5.0 or 'Fail'.");
                    return;
                }
            }

            // Update the grade entity
            grade.setGrade(gradeValue);
            gradeService.saveGrade(grade);

            // Refresh the grid with updated data
            refreshGradesData();

            // Notify user of successful update
            Notification.show("Grade updated successfully.");

            // Close the dialog
            editGradeDialog.close();
        });

        // Cancel button
        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("teacher-grades-view-edit-cancel-button");
        cancelButton.addClickListener(event -> editGradeDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-edit-dialog-buttons");

        // Add components to the dialog
        editGradeDialog.add(new H2("Edit Grade"), gradeField, dialogButtons);
        editGradeDialog.open();
    }
}

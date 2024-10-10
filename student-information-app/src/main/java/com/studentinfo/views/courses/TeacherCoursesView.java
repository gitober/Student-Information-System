package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.data.entity.Course;
import com.studentinfo.services.TeacherService;
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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/courses-view/teacher-courses-view.css")
public class TeacherCoursesView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private List<Course> courses; // Store the courses data
    private final Grid<Course> coursesGrid;

    @Autowired
    public TeacherCoursesView(CourseService courseService, TeacherService teacherService) {
        this.courseService = courseService;
        this.teacherService = teacherService;

        // Main layout setup
        getContent().addClassName("teacher-courses-view-container");

        // Page title
        H2 title = new H2("Course Management");
        title.addClassName("teacher-courses-view-title");

        Paragraph description = new Paragraph("Manage your courses here. You can add, edit, view, and delete courses.");
        description.addClassName("teacher-courses-view-description");

        // Search bar to filter courses by course name or plan
        TextField searchField = new TextField("Search Courses");
        searchField.addClassName("teacher-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Configure courses grid
        coursesGrid = new Grid<>(Course.class, false);
        coursesGrid.addClassName("teacher-courses-view-grid");
        coursesGrid.addColumn(Course::getCourseName).setHeader("Course Name").setClassNameGenerator(course -> "teacher-courses-view-course-column");
        coursesGrid.addColumn(Course::getCoursePlan).setHeader("Course Plan").setClassNameGenerator(course -> "teacher-courses-view-plan-column");
        coursesGrid.addColumn(Course::getFormattedDateRange).setHeader("Date Range").setClassNameGenerator(course -> "teacher-courses-view-daterange-column");

        // Add action buttons (Edit, View, Delete)
        coursesGrid.addComponentColumn(course -> {
            Button editButton = new Button("Edit");
            editButton.addClassName("teacher-courses-view-edit-button");
            editButton.addClickListener(event -> openEditDialog(course));

            Button viewDetailsButton = new Button("View Details");
            viewDetailsButton.addClassName("teacher-courses-view-details-button");
            viewDetailsButton.addClickListener(event -> openViewDetailsDialog(course));

            Button deleteButton = new Button("Delete");
            deleteButton.addClassName("teacher-courses-view-delete-button");
            deleteButton.addClickListener(event -> openDeleteConfirmationDialog(course));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, viewDetailsButton, deleteButton);
            actionButtons.addClassName("teacher-courses-view-action-buttons");
            return actionButtons;
        }).setHeader("Actions");

        // Add course button (opens the "Add New Course" dialog)
        Button addCourseButton = new Button("Add Course");
        addCourseButton.addClassName("teacher-courses-view-add-button");
        addCourseButton.addClickListener(event -> openAddCourseDialog());

        // Adding components to the view
        getContent().add(title, description, searchField, coursesGrid, addCourseButton);

        // Load the courses during initialization
        refreshCourseData();
    }

    // Method to refresh the course data
    private void refreshCourseData() {
        courses = courseService.getAllCourses();
        if (courses != null && !courses.isEmpty()) {
            courses.sort((c1, c2) -> Long.compare(c2.getCourseId(), c1.getCourseId())); // Sort by courseId descending
            coursesGrid.setItems(courses); // Ensure the courses are loaded into the grid
        } else {
            coursesGrid.setItems(List.of()); // Handle the case where no courses are found
        }
        coursesGrid.getDataProvider().refreshAll(); // Force grid to refresh
    }

    // Method to filter courses based on the search term
    private void filterCourses(String searchTerm) {
        List<Course> filteredCourses = courses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course.getCoursePlan().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        coursesGrid.setItems(filteredCourses);
    }

    // Method to open the Edit dialog
    private void openEditDialog(Course course) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-courses-view-edit-dialog");

        // Create and configure input fields for editing course details
        TextField courseNameField = new TextField("Course Name");
        courseNameField.setValue(course.getCourseName());
        courseNameField.addClassName("teacher-courses-view-course-name-field");

        TextField coursePlanField = new TextField("Course Plan");
        coursePlanField.setValue(course.getCoursePlan());
        coursePlanField.addClassName("teacher-courses-view-plan-field");

        // DatePicker for the start date
        DatePicker startDatePicker = new DatePicker("Start Date");
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.addClassName("teacher-courses-view-start-date-picker");

        // Calculate end date using the duration
        DatePicker endDatePicker = new DatePicker("End Date");
        endDatePicker.setValue(startDatePicker.getValue().plusDays(course.getDuration()));
        endDatePicker.addClassName("teacher-courses-view-end-date-picker");

        // ComboBox for selecting teachers
        ComboBox<Teacher> teacherComboBox = new ComboBox<>("Select Teacher");
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);
        teacherComboBox.setValue(course.getTeachers().isEmpty() ? null : course.getTeachers().get(0));
        teacherComboBox.addClassName("teacher-courses-view-add-teacher");

        // Button to save the changes
        Button saveButton = new Button("Save");
        saveButton.addClassName("teacher-courses-view-save-button");
        saveButton.addClickListener(event -> {
            course.setCourseName(courseNameField.getValue());
            course.setCoursePlan(coursePlanField.getValue());

            // Calculate duration based on selected dates
            long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(startDatePicker.getValue(), endDatePicker.getValue());
            course.setDuration((int) durationInDays);

            Teacher selectedTeacher = teacherComboBox.getValue();
            course.setTeachers(List.of(selectedTeacher));

            Course savedCourse = courseService.saveCourse(course, course.getTeachers());
            if (savedCourse != null) {
                refreshCourseData();  // Refresh the grid
                Notification.show("Course updated");
            } else {
                Notification.show("Error updating course. Please try again.");
            }
            editDialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("teacher-courses-view-cancel-button");
        cancelButton.addClickListener(event -> editDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-courses-view-dialog-buttons");

        editDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, teacherComboBox, dialogButtons);
        editDialog.open();
    }

    private void openViewDetailsDialog(Course course) {
        Dialog detailsDialog = new Dialog();
        detailsDialog.addClassName("teacher-courses-view-details-dialog");

        TextField courseNameField = new TextField("Course Name");
        courseNameField.setValue(course.getCourseName());
        courseNameField.setReadOnly(true);
        courseNameField.addClassName("teacher-courses-view-details-course-name");

        TextField coursePlanField = new TextField("Course Plan");
        coursePlanField.setValue(course.getCoursePlan());
        coursePlanField.setReadOnly(true);
        coursePlanField.addClassName("teacher-courses-view-details-course-plan");

        DatePicker startDatePicker = new DatePicker("Start Date");
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setReadOnly(true);
        startDatePicker.addClassName("teacher-courses-view-details-start-date");

        DatePicker endDatePicker = new DatePicker("End Date");
        endDatePicker.setValue(LocalDate.now().plusDays(course.getDuration()));
        endDatePicker.setReadOnly(true);
        endDatePicker.addClassName("teacher-courses-view-details-end-date");

        Grid<Student> studentGrid = new Grid<>(Student.class, false);
        studentGrid.addClassName("teacher-courses-view-student-grid");
        studentGrid.addColumn(Student::getFirstName).setHeader("First Name").setClassNameGenerator(student -> "teacher-courses-view-student-firstname");
        studentGrid.addColumn(Student::getLastName).setHeader("Last Name").setClassNameGenerator(student -> "teacher-courses-view-student-lastname");
        studentGrid.addColumn(Student::getEmail).setHeader("Email").setClassNameGenerator(student -> "teacher-courses-view-student-email");
        studentGrid.addColumn(Student::getPhoneNumber).setHeader("Phone Number").setClassNameGenerator(student -> "teacher-courses-view-student-phonenumber");

        List<Student> enrolledStudents = courseService.getEnrolledStudentsByCourseId(course.getCourseId());
        studentGrid.setItems(enrolledStudents);

        Button closeButton = new Button("Close");
        closeButton.addClassName("teacher-courses-view-close-button");
        closeButton.addClickListener(event -> detailsDialog.close());

        detailsDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, new H2("Enrolled Students"), studentGrid, closeButton);
        detailsDialog.open();
    }

    private void openDeleteConfirmationDialog(Course course) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("teacher-courses-view-delete-dialog");

        confirmationDialog.add(new H2("Confirm Course Deletion"));

        Button confirmButton = new Button("Confirm");
        confirmButton.addClassName("teacher-courses-view-confirm-button");
        confirmButton.addClickListener(event -> {
            try {
                courseService.deleteCourse(course.getCourseId());
                refreshCourseData(); // Refresh grid
                Notification.show("Course deleted");
            } catch (Exception e) {
                Notification.show("Failed to delete course: " + e.getMessage());
            }
            confirmationDialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("teacher-courses-view-cancel-delete-button");
        cancelButton.addClickListener(event -> confirmationDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
        confirmationDialog.add(dialogButtons);
        confirmationDialog.open();
    }

    private void openAddCourseDialog() {
        Dialog addCourseDialog = new Dialog();
        addCourseDialog.addClassName("teacher-courses-view-add-dialog");

        TextField courseNameField = new TextField("Course Name");
        courseNameField.addClassName("teacher-courses-view-add-course-name");

        TextField coursePlanField = new TextField("Course Plan");
        coursePlanField.addClassName("teacher-courses-view-add-course-plan");

        DatePicker startDatePicker = new DatePicker("Start Date");
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.addClassName("teacher-courses-view-add-start-date");

        DatePicker endDatePicker = new DatePicker("End Date");
        endDatePicker.setValue(startDatePicker.getValue().plusDays(30));
        endDatePicker.addClassName("teacher-courses-view-add-end-date");

        ComboBox<Teacher> teacherComboBox = new ComboBox<>("Select Teacher");
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);

        Button saveButton = new Button("Save");
        saveButton.addClassName("teacher-courses-view-add-save-button");
        saveButton.addClickListener(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
                Notification.show("Invalid date range. Please select valid dates.");
                return;
            }

            Teacher selectedTeacher = teacherComboBox.getValue();
            if (selectedTeacher == null) {
                Notification.show("Please select a teacher.");
                return;
            }

            long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);

            Course newCourse = new Course(courseNameField.getValue(), coursePlanField.getValue(), (int) durationInDays);

            try {
                courseService.saveCourse(newCourse, List.of(selectedTeacher));
                refreshCourseData(); // Refresh the grid and sort
                Notification.show("New course added");
            } catch (Exception e) {
                Notification.show("Error adding course: " + e.getMessage());
            }

            addCourseDialog.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.addClassName("teacher-courses-view-add-cancel-button");
        cancelButton.addClickListener(event -> addCourseDialog.close());

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        addCourseDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, teacherComboBox, dialogButtons);
        addCourseDialog.open();
    }
}

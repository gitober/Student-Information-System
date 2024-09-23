package com.studentinfo.views.courses;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./themes/studentinformationapp/views/courses-view/teacher-courses-view.css")


public class TeacherCoursesView extends Composite<VerticalLayout> {

    private List<Course> courses; // Store the courses data
    private Grid<Course> coursesGrid;

    public TeacherCoursesView() {
        // Main layout setup
        getContent().addClassName("teacher-courses-view-container");

        // Page title
        H2 title = new H2("Course Management");
        title.addClassName("teacher-courses-view-title");

        // Search bar to filter courses by course name, subject, or duration
        TextField searchField = new TextField("Search Courses");
        searchField.addClassName("teacher-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Placeholder for courses grid (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
        coursesGrid = new Grid<>(Course.class, false);
        coursesGrid.addColumn(Course::getCourseName).setHeader("Course").setClassNameGenerator(course -> "teacher-courses-view-course-column");
        coursesGrid.addColumn(Course::getSubject).setHeader("Subject").setClassNameGenerator(course -> "teacher-courses-view-subject-column");
        coursesGrid.addColumn(Course::getEnrolledStudents).setHeader("Students").setClassNameGenerator(course -> "teacher-courses-view-students-column");
        coursesGrid.addColumn(Course::getDuration).setHeader("Duration").setClassNameGenerator(course -> "teacher-courses-view-duration-column");
        coursesGrid.addClassName("teacher-courses-view-grid");

        // Add mock data (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
        courses = Arrays.asList(
                new Course("Math 101", "Mathematics", "10 students", "3 months"),
                new Course("Physics 101", "Physics", "8 students", "4 months"),
                new Course("Chemistry 101", "Chemistry", "12 students", "2 months")
        );
        coursesGrid.setItems(courses);

        // Add edit, view, and delete buttons for each course
        coursesGrid.addComponentColumn(course -> {
            Button editButton = new Button("Edit");
            editButton.addClassName("teacher-courses-view-edit-button");

            Button viewDetailsButton = new Button("View Details");
            viewDetailsButton.addClassName("teacher-courses-view-details-button");

            Button deleteButton = new Button("Delete");
            deleteButton.addClassName("teacher-courses-view-delete-button");

            // Edit Button functionality
            editButton.addClickListener(event -> openEditDialog(course));

            // View Details Button functionality
            viewDetailsButton.addClickListener(event -> openViewDetailsDialog(course));

            // Delete Button functionality
            deleteButton.addClickListener(event -> openDeleteConfirmationDialog(course));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, viewDetailsButton, deleteButton);
            actionButtons.addClassName("teacher-courses-view-action-buttons");
            return actionButtons;
        }).setHeader("Actions").setClassNameGenerator(course -> "teacher-courses-view-actions-column");

        // Add course button (opens the "Add New Course" dialog)
        Button addCourseButton = new Button("Add Course");
        addCourseButton.addClassName("teacher-courses-view-add-button");
        addCourseButton.addClickListener(event -> openAddCourseDialog());

        // Adding components to the view
        getContent().add(title, searchField, coursesGrid, addCourseButton);
    }

    // Mock Course class (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
    public static class Course {
        private String courseName;
        private String subject;
        private String enrolledStudents;
        private String duration;

        public Course(String courseName, String subject, String enrolledStudents, String duration) {
            this.courseName = courseName;
            this.subject = subject;
            this.enrolledStudents = enrolledStudents;
            this.duration = duration;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getSubject() {
            return subject;
        }

        public String getEnrolledStudents() {
            return enrolledStudents;
        }

        public String getDuration() {
            return duration;
        }
    }

    // Method to filter courses based on the search term
    private void filterCourses(String searchTerm) {
        // Filter the courses by course name, subject, students, or duration
        List<Course> filteredCourses = courses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course.getSubject().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course.getEnrolledStudents().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course.getDuration().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Update the grid with the filtered data
        coursesGrid.setItems(filteredCourses);
    }

    // Method to open the Edit dialog
    private void openEditDialog(Course course) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-courses-view-edit-dialog");

        // Text fields for editing course details
        TextField courseNameField = new TextField("Course Name");
        courseNameField.setValue(course.getCourseName());
        courseNameField.addClassName("teacher-courses-view-course-name-field");

        TextField subjectField = new TextField("Subject");
        subjectField.setValue(course.getSubject());
        subjectField.addClassName("teacher-courses-view-subject-field");

        Button saveButton = new Button("Save", event -> {
            Notification.show("Course updated");
            editDialog.close();
        });
        saveButton.addClassName("teacher-courses-view-save-button");

        Button cancelButton = new Button("Cancel", event -> editDialog.close());
        cancelButton.addClassName("teacher-courses-view-cancel-edit-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-courses-view-dialog-buttons");

        editDialog.add(courseNameField, subjectField, dialogButtons);
        editDialog.open();
    }

    // Method to open the View Details dialog
    private void openViewDetailsDialog(Course course) {
        Dialog detailsDialog = new Dialog();
        detailsDialog.addClassName("teacher-courses-view-details-dialog");

        // Placeholder content for course details
        TextField courseNameField = new TextField("Course Name");
        courseNameField.setValue(course.getCourseName());
        courseNameField.addClassName("teacher-courses-view-course-name-details");

        TextField subjectField = new TextField("Subject");
        subjectField.setValue(course.getSubject());
        subjectField.addClassName("teacher-courses-view-subject-details");

        TextField studentsField = new TextField("Enrolled Students");
        studentsField.setValue(course.getEnrolledStudents());
        studentsField.addClassName("teacher-courses-view-students-details");

        TextField durationField = new TextField("Duration");
        durationField.setValue(course.getDuration());
        durationField.addClassName("teacher-courses-view-duration-details");

        Button closeButton = new Button("Close", event -> detailsDialog.close());
        closeButton.addClassName("teacher-courses-view-close-details-button");

        detailsDialog.add(courseNameField, subjectField, studentsField, durationField, closeButton);
        detailsDialog.open();
    }

    // Method to open the Delete confirmation dialog
    private void openDeleteConfirmationDialog(Course course) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("teacher-courses-view-delete-dialog");

        confirmationDialog.add(new H2("Confirm Course Deletion"));

        Button confirmButton = new Button("Confirm", event -> {
            Notification.show("Course deleted");
            confirmationDialog.close();
        });
        confirmButton.addClassName("teacher-courses-view-confirm-delete-button");

        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());
        cancelButton.addClassName("teacher-courses-view-cancel-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
        dialogButtons.addClassName("teacher-courses-view-dialog-buttons");

        confirmationDialog.add(dialogButtons);
        confirmationDialog.open();
    }

    // Method to open the "Add New Course" dialog
    private void openAddCourseDialog() {
        Dialog addCourseDialog = new Dialog();
        addCourseDialog.addClassName("teacher-courses-view-add-course-dialog");

        // Text fields for adding new course details
        TextField courseNameField = new TextField("Course Name");
        courseNameField.addClassName("teacher-courses-view-course-name-field");

        TextField subjectField = new TextField("Subject");
        subjectField.addClassName("teacher-courses-view-subject-field");

        TextField durationField = new TextField("Duration");
        durationField.addClassName("teacher-courses-view-duration-field");

        Button saveButton = new Button("Save", event -> {
            // Logic to add the new course to the backend (currently mock data)
            Notification.show("New course added");
            addCourseDialog.close();
        });
        saveButton.addClassName("teacher-courses-view-save-button");

        Button cancelButton = new Button("Cancel", event -> addCourseDialog.close());
        cancelButton.addClassName("teacher-courses-view-cancel-add-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-courses-view-dialog-buttons");

        addCourseDialog.add(courseNameField, subjectField, durationField, dialogButtons);
        addCourseDialog.open();
    }
}

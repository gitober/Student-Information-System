package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.UserService;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Attendance;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/courses-view/student-courses-view.css")
public class StudentCoursesView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final UserService userService;
    private List<Course> enrolledCourses;
    private List<Course> availableCourses;
    private Grid<Course> enrolledCoursesGrid;
    private Grid<Course> availableCoursesGrid;

    @Autowired
    public StudentCoursesView(CourseService courseService, AttendanceService attendanceService, UserService userService) {
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.userService = userService;

        getContent().addClassName("student-courses-view-container");

        // Page title
        H2 title = new H2("My Courses");
        title.addClassName("student-courses-view-title");

        Paragraph description = new Paragraph("View and enroll in courses below.");
        description.addClassName("student-courses-view-description");

        // Search bar to filter courses by course name
        TextField searchField = new TextField("Search Courses");
        searchField.addClassName("student-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Initialize the grid for enrolled courses
        enrolledCoursesGrid = new Grid<>(Course.class);
        enrolledCoursesGrid.removeAllColumns(); // Clear existing columns

        // Add necessary columns
        enrolledCoursesGrid.addColumn(Course::getCourseName).setHeader("Course Name");
        enrolledCoursesGrid.addColumn(Course::getCoursePlan).setHeader("Course Plan");
        enrolledCoursesGrid.addColumn(course -> course.getFormattedDateRange()).setHeader("Duration");
        enrolledCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.get(0).getFirstName() + " " + teachers.get(0).getLastName();
            } else {
                return "No teacher assigned";
            }
        }).setHeader("Teacher");

        // Button to view attendance for each course
        enrolledCoursesGrid.addComponentColumn(course -> {
            Button attendanceButton = new Button("View Attendance");
            attendanceButton.addClassName("student-courses-view-attendance-button"); // Add this line for styling
            attendanceButton.addClickListener(event -> openAttendanceDialog(course));
            return attendanceButton;
        }).setHeader("Attendance");

        // Initialize the grid for available courses
        availableCoursesGrid = new Grid<>(Course.class);
        availableCoursesGrid.removeAllColumns(); // Clear existing columns

        // Add only the necessary columns
        availableCoursesGrid.addColumn(Course::getCourseName).setHeader("Course Name");
        availableCoursesGrid.addColumn(Course::getCoursePlan).setHeader("Course Plan");
        availableCoursesGrid.addColumn(course -> course.getFormattedDateRange()).setHeader("Duration");
        availableCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.get(0).getFirstName() + " " + teachers.get(0).getLastName();
            } else {
                return "No teacher assigned";
            }
        }).setHeader("Teacher");

        // Add "Enroll" button for each course
        availableCoursesGrid.addComponentColumn(course -> {
            Button enrollButton = new Button("Enroll");
            enrollButton.addClassName("student-courses-view-enroll-button");
            enrollButton.addClickListener(event -> enrollInCourse(course));
            return enrollButton;
        }).setHeader("Actions");


        // Fetch the current student's number
        Long studentNumber = userService.getCurrentStudentNumber();

        if (studentNumber == null) {
            Notification.show("Error: Unable to retrieve student information.");
            return;
        }

        // Fetch enrolled and available courses from the backend
        refreshCourseData(studentNumber);

        // Add components to layout
        getContent().add(title, description, searchField, new H2("Enrolled Courses"), enrolledCoursesGrid,
                new H2("Available Courses"), availableCoursesGrid);
    }

    private void refreshCourseData(Long studentNumber) {
        enrolledCourses = courseService.getEnrolledCourses(studentNumber);
        availableCourses = courseService.getAvailableCourses();

        // Update the grids
        enrolledCoursesGrid.setItems(enrolledCourses);
        availableCoursesGrid.setItems(availableCourses);
    }

    // Method to filter both enrolled and available courses based on the search term
    private void filterCourses(String searchTerm) {
        String lowerCaseSearchTerm = searchTerm.toLowerCase();

        // Filter enrolled courses
        List<Course> filteredEnrolledCourses = enrolledCourses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getCoursePlan().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getFormattedDateRange().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getTeachers().stream()
                                .anyMatch(teacher -> (teacher.getFirstName() + " " + teacher.getLastName()).toLowerCase().contains(lowerCaseSearchTerm))
                ).collect(Collectors.toList());

        // Filter available courses
        List<Course> filteredAvailableCourses = availableCourses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getCoursePlan().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getFormattedDateRange().toLowerCase().contains(lowerCaseSearchTerm) ||
                        course.getTeachers().stream()
                                .anyMatch(teacher -> (teacher.getFirstName() + " " + teacher.getLastName()).toLowerCase().contains(lowerCaseSearchTerm))
                ).collect(Collectors.toList());

        // Update the grids
        enrolledCoursesGrid.setItems(filteredEnrolledCourses);
        availableCoursesGrid.setItems(filteredAvailableCourses);
    }

    // Method to enroll in a course with a confirmation dialog
    private void enrollInCourse(Course course) {
        // Create a confirmation dialog
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("student-courses-view-confirmation-dialog");

        // Dialog content
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("student-courses-view-dialog-layout");

        H2 dialogTitle = new H2("Confirm Enrollment");
        dialogTitle.addClassName("student-courses-view-dialog-title");
        dialogLayout.add(dialogTitle);

        // Course field (read-only)
        TextField courseField = new TextField("Course", course.getCourseName(), "");
        courseField.addClassName("student-courses-view-course-field");
        courseField.setReadOnly(true);
        dialogLayout.add(courseField);

        // Teacher field (read-only)
        TextField teacherField = new TextField("Teacher",
                course.getTeachers().isEmpty() ? "No teacher assigned" :
                        course.getTeachers().get(0).getFirstName() + " " + course.getTeachers().get(0).getLastName(),
                "");
        teacherField.addClassName("student-courses-view-teacher-field");
        teacherField.setReadOnly(true);
        dialogLayout.add(teacherField);

        // Confirm button
        Button confirmButton = new Button("Confirm", event -> {
            // Fetch the current student's number
            Long studentNumber = userService.getCurrentStudentNumber();

            if (studentNumber == null) {
                Notification.show("Error: Unable to retrieve student information.");
                confirmationDialog.close();
                return;
            }

            // Ensure batchId and coursePayment are handled properly.
            Long batchId = null; // Replace with a valid batch ID if needed
            double coursePayment = 0.0; // Adjust based on your business logic

            try {
                // Debug log before enrolling
                System.out.println("Debug: Enrolling student " + studentNumber + " in course ID " + course.getCourseId());

                // Enroll the student in the selected course
                courseService.enrollInCourse(studentNumber, batchId, course.getCourseId(), coursePayment);

                // Refresh the course data after enrollment
                refreshCourseData(studentNumber);
                // Show notification to confirm enrollment
                Notification.show("Successfully enrolled in " + course.getCourseName());
            } catch (Exception e) {
                // Show error notification if enrollment fails
                Notification.show("Enrollment failed: " + e.getMessage());
                e.printStackTrace(); // Log stack trace for debugging
            }

            // Close the dialog
            confirmationDialog.close();
        });
        confirmButton.addClassName("student-courses-view-confirm-button");

        // Cancel button
        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());
        cancelButton.addClassName("student-courses-view-cancel-button");

        // Add buttons to the dialog with adjusted styles
        HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
        dialogButtons.addClassName("student-courses-view-dialog-buttons");
        dialogButtons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER); // Center align buttons
        dialogButtons.setSpacing(true); // Add spacing between buttons
        dialogLayout.add(dialogButtons);

        // Add layout to the dialog
        confirmationDialog.add(dialogLayout);

        // Open the dialog
        confirmationDialog.open();
    }


    private void openAttendanceDialog(Course course) {
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-courses-view-attendance-dialog");

        // Title for the dialog
        H2 title = new H2("Attendance for " + course.getCourseName());
        attendanceDialog.add(title);

        // Fetch attendance records for the selected course
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByCourseId(course.getCourseId());

        // Create a grid to display attendance records
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class);
        attendanceGrid.removeAllColumns(); // Clear existing columns

        attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader("Date");
        attendanceGrid.addColumn(Attendance::getAttendanceStatus).setHeader("Status");

        // Set items in the attendance grid
        attendanceGrid.setItems(attendanceRecords);

        // Add attendance grid to the dialog
        attendanceDialog.add(attendanceGrid);

        // Close button with a custom class name
        Button closeButton = new Button("Close", event -> attendanceDialog.close());
        closeButton.addClassName("student-courses-view-attendance-close-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(closeButton);
        attendanceDialog.add(dialogButtons);

        // Open the attendance dialog
        attendanceDialog.open();
    }




}
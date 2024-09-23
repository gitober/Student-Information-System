package com.studentinfo.views.courses;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CssImport("./themes/studentinformationapp/views/courses-view/student-courses-view.css")
public class StudentCoursesView extends Composite<VerticalLayout> {

    private List<String[]> enrolledCourses; // Store the enrolled courses data
    private List<String[]> availableCourses; // Store the available courses data
    private Grid<String[]> enrolledCoursesGrid;
    private Grid<String[]> availableCoursesGrid;
    private Map<String, List<AttendanceRecord>> courseAttendanceMap; // Map to store attendance records for each course

    public StudentCoursesView() {
        getContent().addClassName("student-courses-view-container");

        // Page title
        H2 title = new H2("My Courses");
        title.addClassName("student-courses-view-title");

        // Search bar to filter courses by course name
        TextField searchField = new TextField("Search Courses");
        searchField.addClassName("student-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Grid for courses the student is enrolled in
        enrolledCoursesGrid = new Grid<>();
        enrolledCoursesGrid.addColumn(course -> course[0]).setHeader("Course").setClassNameGenerator(course -> "student-courses-view-course-column");
        enrolledCoursesGrid.addColumn(course -> course[1]).setHeader("Subject").setClassNameGenerator(course -> "student-courses-view-subject-column");
        enrolledCoursesGrid.addColumn(course -> course[2]).setHeader("Teacher").setClassNameGenerator(course -> "student-courses-view-teacher-column");

        // Convert duration (stored as an integer) to a readable date range
        enrolledCoursesGrid.addColumn(course -> convertDurationToDateRange(course[3])).setHeader("Duration").setClassNameGenerator(course -> "student-courses-view-duration-column");

        // Add "View Attendance" button for each course
        enrolledCoursesGrid.addComponentColumn(course -> {
            Button viewAttendanceButton = new Button("View Attendance");
            viewAttendanceButton.addClickListener(event -> openAttendanceDialog(course[0])); // Course[0] is the course name
            viewAttendanceButton.addClassName("student-courses-view-attendance-button");
            return viewAttendanceButton;
        }).setHeader("Actions");

        // Mock data for enrolled courses
        enrolledCourses = Arrays.asList(
                new String[]{"Math 101", "Mathematics", "Mr. Smith", "90"},  // Duration: 90 days
                new String[]{"Physics 101", "Physics", "Dr. Johnson", "120"}  // Duration: 120 days
        );

        // Mock data for available courses
        availableCourses = Arrays.asList(
                new String[]{"Chemistry 101", "Chemistry", "Dr. Brown", "60"},  // Duration: 60 days
                new String[]{"Biology 101", "Biology", "Dr. Green", "45"}  // Duration: 45 days
        );

        // Mock data for attendance (multiple dates for each course)
        courseAttendanceMap = new HashMap<>();
        courseAttendanceMap.put("Math 101", Arrays.asList(
                new AttendanceRecord(LocalDate.of(2023, 9, 1), "Present"),
                new AttendanceRecord(LocalDate.of(2023, 9, 3), "Absent"),
                new AttendanceRecord(LocalDate.of(2023, 9, 5), "Present")
        ));
        courseAttendanceMap.put("Physics 101", Arrays.asList(
                new AttendanceRecord(LocalDate.of(2023, 9, 2), "Present"),
                new AttendanceRecord(LocalDate.of(2023, 9, 4), "Absent")
        ));

        enrolledCoursesGrid.setItems(enrolledCourses);
        enrolledCoursesGrid.addClassName("student-courses-view-grid");

        // Grid for available courses to enroll
        availableCoursesGrid = new Grid<>();
        availableCoursesGrid.addColumn(course -> course[0]).setHeader("Course").setClassNameGenerator(course -> "student-courses-view-available-course-column");
        availableCoursesGrid.addColumn(course -> course[1]).setHeader("Subject").setClassNameGenerator(course -> "student-courses-view-available-subject-column");
        availableCoursesGrid.addColumn(course -> course[2]).setHeader("Teacher").setClassNameGenerator(course -> "student-courses-view-available-teacher-column");
        availableCoursesGrid.addColumn(course -> convertDurationToDateRange(course[3])).setHeader("Duration").setClassNameGenerator(course -> "student-courses-view-available-duration-column");

        // Add "Enroll" button for each available course
        availableCoursesGrid.addComponentColumn(course -> {
            Button enrollButton = new Button("Enroll");
            enrollButton.addClickListener(event -> enrollInCourse(course)); // Enroll in the selected course
            enrollButton.addClassName("student-courses-view-enroll-button");
            return enrollButton;
        }).setHeader("Actions");

        availableCoursesGrid.setItems(availableCourses);
        availableCoursesGrid.addClassName("student-courses-view-available-grid");

        // Add components to the layout
        getContent().add(title, searchField, new H2("Enrolled Courses"), enrolledCoursesGrid, new H2("Available Courses"), availableCoursesGrid);
    }

    // Method to convert duration to a date range (mock example)
    private String convertDurationToDateRange(String duration) {
        int durationDays = Integer.parseInt(duration); // Convert duration to integer
        LocalDate startDate = LocalDate.now(); // Assume the course starts today
        LocalDate endDate = startDate.plusDays(durationDays); // Calculate the end date based on duration
        return startDate.toString() + " - " + endDate.toString(); // Return the date range as a string
    }

    // Method to open the dialog showing attendance records for a course
    private void openAttendanceDialog(String courseName) {
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-courses-view-attendance-dialog");

        // Dialog title
        attendanceDialog.add(new H2("Attendance Records for " + courseName));

        // Get attendance records for the course
        List<AttendanceRecord> attendanceRecords = courseAttendanceMap.get(courseName);

        // Create a grid to display attendance records
        Grid<AttendanceRecord> attendanceGrid = new Grid<>(AttendanceRecord.class, false);
        attendanceGrid.addColumn(AttendanceRecord::getDate).setHeader("Date");
        attendanceGrid.addColumn(AttendanceRecord::getStatus).setHeader("Status");
        attendanceGrid.setItems(attendanceRecords);
        attendanceGrid.addClassName("student-courses-view-attendance-grid");

        // Add the grid to the dialog
        attendanceDialog.add(attendanceGrid);

        // Close button
        Button closeButton = new Button("Close", event -> attendanceDialog.close());
        closeButton.addClassName("student-courses-view-close-attendance-dialog-button");
        attendanceDialog.add(closeButton);

        // Open the dialog
        attendanceDialog.open();
    }

    // Method to enroll in a course (using mock data)
    private void enrollInCourse(String[] course) {
        // Mock data: Add the course to the enrolled courses list
        enrolledCourses.add(course); // Add the course to enrolled courses
        availableCourses.remove(course); // Remove the course from available courses

        // Refresh the grids with the updated mock data
        enrolledCoursesGrid.setItems(enrolledCourses); // Update enrolled courses grid
        availableCoursesGrid.setItems(availableCourses); // Update available courses grid

        // Show notification to confirm mock enrollment
        Notification.show("Successfully enrolled in " + course[0]);
    }


    // Mock AttendanceRecord class
    public static class AttendanceRecord {
        private LocalDate date;
        private String status;

        public AttendanceRecord(LocalDate date, String status) {
            this.date = date;
            this.status = status;
        }

        public LocalDate getDate() {
            return date;
        }

        public String getStatus() {
            return status;
        }
    }

    // Method to filter courses based on the search term
    private void filterCourses(String searchTerm) {
        List<String[]> filteredEnrolledCourses = enrolledCourses.stream()
                .filter(course -> course[0].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[1].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[2].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[3].toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        List<String[]> filteredAvailableCourses = availableCourses.stream()
                .filter(course -> course[0].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[1].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[2].toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course[3].toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        enrolledCoursesGrid.setItems(filteredEnrolledCourses);
        availableCoursesGrid.setItems(filteredAvailableCourses);
    }
}

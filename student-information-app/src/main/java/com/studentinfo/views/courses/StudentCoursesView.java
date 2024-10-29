package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.entity.Attendance;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.UserService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/courses-view/student-courses-view.css")
public class StudentCoursesView extends Composite<VerticalLayout> {

    private static final Logger logger = LoggerFactory.getLogger(StudentCoursesView.class);

    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final UserService userService;
    private final MessageSource messageSource;
    private List<Course> enrolledCourses;
    private List<Course> availableCourses;
    private final Grid<Course> enrolledCoursesGrid;
    private final Grid<Course> availableCoursesGrid;

    @Autowired
    public StudentCoursesView(CourseService courseService, AttendanceService attendanceService, UserService userService, MessageSource messageSource) {
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.userService = userService;
        this.messageSource = messageSource;

        getContent().addClassName("student-courses-view-container");

        // Page title
        H2 title = new H2(messageSource.getMessage("my.courses.title", null, Locale.getDefault()));
        title.addClassName("student-courses-view-title");

        Paragraph description = new Paragraph(messageSource.getMessage("my.courses.description", null, Locale.getDefault()));
        description.addClassName("student-courses-view-description");

        // Search bar to filter courses by course name
        TextField searchField = new TextField(messageSource.getMessage("my.courses.search.label", null, Locale.getDefault()));
        searchField.addClassName("student-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Initialize the grid for enrolled courses
        enrolledCoursesGrid = new Grid<>(Course.class);
        enrolledCoursesGrid.removeAllColumns();
        enrolledCoursesGrid.addColumn(Course::getCourseName).setHeader(messageSource.getMessage("my.courses.enrolled.column.name", null, Locale.getDefault()));
        enrolledCoursesGrid.addColumn(Course::getCoursePlan).setHeader(messageSource.getMessage("my.courses.enrolled.column.plan", null, Locale.getDefault()));
        enrolledCoursesGrid.addColumn(Course::getFormattedDateRange).setHeader(messageSource.getMessage("my.courses.enrolled.column.duration", null, Locale.getDefault()));
        enrolledCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.get(0).getFirstName() + " " + teachers.get(0).getLastName();
            } else {
                return messageSource.getMessage("my.courses.no.teacher", null, Locale.getDefault());
            }
        }).setHeader(messageSource.getMessage("my.courses.enrolled.column.teacher", null, Locale.getDefault()));

        // Button to view attendance for each course
        enrolledCoursesGrid.addComponentColumn(course -> {
            Button attendanceButton = new Button(messageSource.getMessage("my.courses.attendance.button", null, Locale.getDefault()));
            attendanceButton.addClassName("student-courses-view-attendance-button");
            attendanceButton.addClickListener(event -> openAttendanceDialog(course));
            return attendanceButton;
        }).setHeader(messageSource.getMessage("my.courses.attendance.column", null, Locale.getDefault()));

        // Initialize the grid for available courses
        availableCoursesGrid = new Grid<>(Course.class);
        availableCoursesGrid.removeAllColumns();
        availableCoursesGrid.addColumn(Course::getCourseName).setHeader(messageSource.getMessage("my.courses.available.column.name", null, Locale.getDefault()));
        availableCoursesGrid.addColumn(Course::getCoursePlan).setHeader(messageSource.getMessage("my.courses.available.column.plan", null, Locale.getDefault()));
        availableCoursesGrid.addColumn(Course::getFormattedDateRange).setHeader(messageSource.getMessage("my.courses.available.column.duration", null, Locale.getDefault()));
        availableCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.get(0).getFirstName() + " " + teachers.get(0).getLastName();
            } else {
                return messageSource.getMessage("my.courses.no.teacher", null, Locale.getDefault());
            }
        }).setHeader(messageSource.getMessage("my.courses.available.column.teacher", null, Locale.getDefault()));

        // Add "Enroll" button for each course
        availableCoursesGrid.addComponentColumn(course -> {
            Button enrollButton = new Button(messageSource.getMessage("my.courses.enroll.button", null, Locale.getDefault()));
            enrollButton.addClassName("student-courses-view-enroll-button");
            enrollButton.addClickListener(event -> enrollInCourse(course));
            return enrollButton;
        }).setHeader(messageSource.getMessage("my.courses.available.column.actions", null, Locale.getDefault()));

        // Fetch the current student's number
        Long studentNumber = userService.getCurrentStudentNumber();

        if (studentNumber == null) {
            Notification.show(messageSource.getMessage("my.courses.error.student.not.found", null, Locale.getDefault()));
            return;
        }

        // Fetch enrolled and available courses from the backend
        refreshCourseData(studentNumber);

        // Add components to layout
        getContent().add(title, description, searchField, new H2(messageSource.getMessage("my.courses.enrolled.title", null, Locale.getDefault())), enrolledCoursesGrid,
                new H2(messageSource.getMessage("my.courses.available.title", null, Locale.getDefault())), availableCoursesGrid);
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

        H2 dialogTitle = new H2(messageSource.getMessage("my.courses.dialog.confirm.title", null, Locale.getDefault()));
        dialogTitle.addClassName("student-courses-view-dialog-title");
        dialogLayout.add(dialogTitle);

        // Course field (read-only)
        TextField courseField = new TextField(messageSource.getMessage("my.courses.dialog.course.label", null, Locale.getDefault()), course.getCourseName(), "");
        courseField.addClassName("student-courses-view-dialog-course-field");
        courseField.setReadOnly(true);
        dialogLayout.add(courseField);

        // Confirmation buttons
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        Button confirmButton = new Button(messageSource.getMessage("my.courses.dialog.confirm.button", null, Locale.getDefault()));
        confirmButton.addClickListener(event -> {
            // Enroll the student in the selected course without batchId
            Long studentNumber = userService.getCurrentStudentNumber();
            courseService.enrollInCourse(studentNumber, null, course.getCourseId(), 0); // Adjust the method call as needed
            Notification.show(messageSource.getMessage("my.courses.enroll.success", null, Locale.getDefault()));
            confirmationDialog.close();
            refreshCourseData(studentNumber);
        });

        Button cancelButton = new Button(messageSource.getMessage("my.courses.dialog.cancel.button", null, Locale.getDefault()));
        cancelButton.addClickListener(event -> confirmationDialog.close());

        buttonLayout.add(confirmButton, cancelButton);
        dialogLayout.add(buttonLayout);
        confirmationDialog.add(dialogLayout);
        confirmationDialog.open();
    }


    // Method to open attendance dialog
    private void openAttendanceDialog(Course course) {
        // Create attendance dialog
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-courses-view-attendance-dialog");

        // Attendance title
        H2 attendanceTitle = new H2(messageSource.getMessage("my.courses.attendance.title", null, Locale.getDefault()));
        attendanceDialog.add(attendanceTitle);

        // Retrieve the current student's number
        Long studentNumber = userService.getCurrentStudentNumber(); // Ensure this method exists and returns the correct student number

        // Fetch attendance records
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByStudentNumberAndCourseId(studentNumber, course.getCourseId());

        // Create grid for attendance records
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class);
        attendanceGrid.removeAllColumns(); // Clear existing columns
        attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader(messageSource.getMessage("my.courses.attendance.column.date", null, Locale.getDefault()));
        attendanceGrid.addColumn(Attendance::getAttendanceStatus).setHeader(messageSource.getMessage("my.courses.attendance.column.status", null, Locale.getDefault()));

        // Set items to the grid
        attendanceGrid.setItems(attendanceRecords);

        // Add the grid to the dialog
        attendanceDialog.add(attendanceGrid);
        attendanceDialog.open();
    }

}

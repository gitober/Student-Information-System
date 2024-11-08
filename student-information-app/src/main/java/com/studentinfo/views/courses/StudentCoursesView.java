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
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

        Locale currentLocale = LocaleContextHolder.getLocale(); // Use the current locale

        // Page title
        H2 title = new H2(messageSource.getMessage("my.courses.title", null, currentLocale));
        title.addClassName("student-courses-view-title");

        Paragraph description = new Paragraph(messageSource.getMessage("my.courses.description", null, currentLocale));
        description.addClassName("student-courses-view-description");

        // Search bar to filter courses by course name
        TextField searchField = new TextField(messageSource.getMessage("my.courses.search.label", null, currentLocale));
        searchField.addClassName("student-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Initialize the grid for enrolled courses
        enrolledCoursesGrid = new Grid<>(Course.class);
        enrolledCoursesGrid.removeAllColumns();
        enrolledCoursesGrid.addColumn(Course::getCourseName).setHeader(messageSource.getMessage("my.courses.enrolled.column.name", null, currentLocale));
        enrolledCoursesGrid.addColumn(Course::getCoursePlan).setHeader(messageSource.getMessage("my.courses.enrolled.column.plan", null, currentLocale));
        enrolledCoursesGrid.addColumn(course -> formatDateRange(LocalDate.now(), LocalDate.now().plusDays(course.getDuration())))
                .setHeader(messageSource.getMessage("my.courses.enrolled.column.duration", null, currentLocale));
        enrolledCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.getFirst().getFirstName() + " " + teachers.getFirst().getLastName();
            } else {
                return messageSource.getMessage("my.courses.no.teacher", null, currentLocale);
            }
        }).setHeader(messageSource.getMessage("my.courses.enrolled.column.teacher", null, currentLocale));

        // Button to view attendance for each course
        enrolledCoursesGrid.addComponentColumn(course -> {
            Button attendanceButton = new Button(messageSource.getMessage("my.courses.attendance.button", null, currentLocale));
            attendanceButton.addClassName("student-courses-view-attendance-button");
            attendanceButton.addClickListener(event -> openAttendanceDialog(course));
            return attendanceButton;
        }).setHeader(messageSource.getMessage("my.courses.attendance.column", null, currentLocale));

        // Initialize the grid for available courses
        availableCoursesGrid = new Grid<>(Course.class);
        availableCoursesGrid.removeAllColumns();
        availableCoursesGrid.addColumn(Course::getCourseName).setHeader(messageSource.getMessage("my.courses.available.column.name", null, currentLocale));
        availableCoursesGrid.addColumn(Course::getCoursePlan).setHeader(messageSource.getMessage("my.courses.available.column.plan", null, currentLocale));
        availableCoursesGrid.addColumn(course -> formatDateRange(LocalDate.now(), LocalDate.now().plusDays(course.getDuration())))
                .setHeader(messageSource.getMessage("my.courses.available.column.duration", null, currentLocale));
        availableCoursesGrid.addColumn(course -> {
            List<Teacher> teachers = course.getTeachers();
            if (!teachers.isEmpty()) {
                return teachers.getFirst().getFirstName() + " " + teachers.getFirst().getLastName();
            } else {
                return messageSource.getMessage("my.courses.no.teacher", null, currentLocale);
            }
        }).setHeader(messageSource.getMessage("my.courses.available.column.teacher", null, currentLocale));

        // Add "Enroll" button for each course
        availableCoursesGrid.addComponentColumn(course -> {
            Button enrollButton = new Button(messageSource.getMessage("my.courses.enroll.button", null, currentLocale));
            enrollButton.addClassName("student-courses-view-enroll-button");
            enrollButton.addClickListener(event -> enrollInCourse(course));
            return enrollButton;
        }).setHeader(messageSource.getMessage("my.courses.available.column.actions", null, currentLocale));

        // Fetch the current student's number
        Long studentNumber = userService.getCurrentStudentNumber();

        if (studentNumber == null) {
            Notification.show(messageSource.getMessage("my.courses.error.student.not.found", null, currentLocale));
            return;
        }

        // Fetch enrolled and available courses from the backend
        refreshCourseData(studentNumber);

        // Add components to layout
        getContent().add(title, description, searchField, new H2(messageSource.getMessage("my.courses.enrolled.title", null, currentLocale)), enrolledCoursesGrid,
                new H2(messageSource.getMessage("my.courses.available.title", null, currentLocale)), availableCoursesGrid);
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

    private void enrollInCourse(Course course) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("student-courses-view-confirmation-dialog");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("student-courses-view-dialog-layout");

        Locale currentLocale = LocaleContextHolder.getLocale();
        H2 dialogTitle = new H2(messageSource.getMessage("my.courses.dialog.confirm.title", null, currentLocale));
        dialogTitle.addClassName("student-courses-view-dialog-title");
        dialogLayout.add(dialogTitle);

        TextField courseField = new TextField(messageSource.getMessage("my.courses.dialog.course.label", null, currentLocale), course.getCourseName(), "");
        courseField.addClassName("student-courses-view-dialog-course-field");
        courseField.setReadOnly(true);
        dialogLayout.add(courseField);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("student-courses-view-dialog-button-layout");
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button confirmButton = new Button(messageSource.getMessage("my.courses.dialog.confirm.button", null, currentLocale));
        confirmButton.addClassName("student-courses-view-confirm-button");
        confirmButton.addClickListener(event -> {
            Long studentNumber = userService.getCurrentStudentNumber();
            courseService.enrollInCourse(studentNumber, null, course.getCourseId(), 0);
            Notification.show(messageSource.getMessage("my.courses.enroll.success", null, currentLocale));
            confirmationDialog.close();
            refreshCourseData(studentNumber);
        });

        Button cancelButton = new Button(messageSource.getMessage("my.courses.dialog.cancel.button", null, currentLocale));
        cancelButton.addClassName("student-courses-view-cancel-button");
        cancelButton.addClickListener(event -> confirmationDialog.close());

        buttonLayout.add(confirmButton, cancelButton);
        dialogLayout.add(buttonLayout);
        confirmationDialog.add(dialogLayout);
        confirmationDialog.open();
    }


    private void openAttendanceDialog(Course course) {
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-courses-view-attendance-dialog");

        Locale currentLocale = LocaleContextHolder.getLocale();

        // Title for the dialog
        H2 attendanceTitle = new H2(messageSource.getMessage("my.courses.attendance.title", null, currentLocale));
        attendanceTitle.addClassName("student-courses-view-attendance-title"); // Add CSS class for the title
        attendanceDialog.add(attendanceTitle);

        Long studentNumber = userService.getCurrentStudentNumber();
        List<Attendance> attendanceRecords = attendanceService.getAttendanceByStudentNumberAndCourseId(studentNumber, course.getCourseId());

        // Grid setup for attendance
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class);
        attendanceGrid.addClassName("student-courses-view-attendance-grid");
        attendanceGrid.removeAllColumns();
        attendanceGrid.addColumn(Attendance::getAttendanceDate)
                .setHeader(messageSource.getMessage("my.courses.attendance.column.date", null, currentLocale))
                .setKey("attendance-date-column");
        attendanceGrid.addColumn(Attendance::getAttendanceStatus)
                .setHeader(messageSource.getMessage("my.courses.attendance.column.status", null, currentLocale))
                .setKey("attendance-status-column");

        attendanceGrid.setItems(attendanceRecords);
        attendanceDialog.add(attendanceGrid);

        // Optionally, add a close button for better UX
        Button closeButton = new Button(messageSource.getMessage("my.courses.attendance.close.button", null, currentLocale));
        closeButton.addClassName("student-courses-view-attendance-close-button"); // Add CSS class for the button
        closeButton.addClickListener(event -> attendanceDialog.close());
        attendanceDialog.add(closeButton);

        attendanceDialog.open();
    }


    public String formatDateRange(LocalDate startDate, LocalDate endDate) {
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                locale.getLanguage().equals("ch") ? "yyyy年MM月dd日" : "dd/MM/yyyy",
                locale
        );
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

}

package com.studentinfo.views.TeacherAttendanceTrackingView;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.TeacherService;
import com.studentinfo.views.mainlayout.MainLayout;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "teacher/attendance-tracking", layout = MainLayout.class)
@PageTitle("Attendance Tracking")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherAttendanceTrackingView/teacher-attendance-view.css")
public class TeacherAttendanceTrackingView extends VerticalLayout {

    private final TeacherService teacherService;
    private final AuthenticatedUser authenticatedUser;

    private List<AttendanceRecord> attendanceRecords;
    private Grid<AttendanceRecord> attendanceGrid;

    @Autowired
    public TeacherAttendanceTrackingView(TeacherService teacherService, AuthenticatedUser authenticatedUser) {
        this.teacherService = teacherService;
        this.authenticatedUser = authenticatedUser;

        // Set layout properties
        setSizeFull();
        setPadding(false);
        setMargin(false);
        setSpacing(false);
        setAlignItems(Alignment.START);
        setJustifyContentMode(JustifyContentMode.START);
        addClassName("teacher-attendance-view-container");

        // Add padding to prevent content from sticking to or going under the header
        getStyle().set("padding-top", "60px"); // Adjust based on header height

        // Create and add the page header with title and description
        add(createPageHeader());

        // Search bar for filtering attendance records
        TextField searchField = new TextField("Search by Student or Course");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterAttendance(event.getValue()));
        searchField.addClassName("teacher-attendance-search");

        // Configure and add the grid
        configureGrid();

        // Add mock data (Replace with backend later)
        attendanceRecords = Arrays.asList(
                new AttendanceRecord(1L, "Math 101", "Alice", "Present", LocalDate.now()),
                new AttendanceRecord(2L, "Physics 101", "Bob", "Absent", LocalDate.now().minusDays(1)),
                new AttendanceRecord(3L, "Chemistry 101", "Charlie", "Present", LocalDate.now().minusDays(2))
        );
        attendanceGrid.setItems(attendanceRecords);

        // Add "Add Attendance" button
        Button addAttendanceButton = new Button("Add Attendance");
        addAttendanceButton.addClassName("teacher-attendance-add-button");
        addAttendanceButton.addClickListener(event -> openAddAttendanceDialog());

        // Add components to the view
        add(searchField, attendanceGrid, addAttendanceButton);
    }

    // Method to create the page header layout (title + description)
    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("page-header");

        // Add the header text (title)
        H2 headerText = new H2("Attendance Tracking");
        headerText.addClassName("attendance-tracking-header");

        // Add description text
        Paragraph description = new Paragraph("Track and update student attendance records.");
        description.addClassName("attendance-tracking-description");

        headerLayout.add(headerText, description);
        headerLayout.setPadding(false); // Adjust padding if necessary
        headerLayout.setAlignItems(Alignment.START); // Align to start

        return headerLayout;
    }

    // Method to configure the grid for displaying attendance
    private void configureGrid() {
        attendanceGrid = new Grid<>(AttendanceRecord.class, false);

        attendanceGrid.addColumn(AttendanceRecord::getCourse)
                .setHeader("Course")
                .setAutoWidth(true)
                .setClassNameGenerator(record -> "attendance-grid-course-column");

        attendanceGrid.addColumn(AttendanceRecord::getStudent)
                .setHeader("Student")
                .setAutoWidth(true)
                .setClassNameGenerator(record -> "attendance-grid-student-column");

        attendanceGrid.addColumn(AttendanceRecord::getStatus)
                .setHeader("Status")
                .setAutoWidth(true)
                .setClassNameGenerator(record -> "attendance-grid-status-column");

        attendanceGrid.addColumn(AttendanceRecord::getAttendanceDate)
                .setHeader("Date")
                .setAutoWidth(true)
                .setClassNameGenerator(record -> "attendance-grid-date-column");

        attendanceGrid.addClassName("attendance-grid");

        // Add mark attendance button for each entry
        attendanceGrid.addComponentColumn(record -> {
            Button markButton = new Button("Mark Attendance");
            markButton.addClassName("attendance-mark-button");

            // Add listeners for buttons
            markButton.addClickListener(event -> openMarkAttendanceDialog(record));

            HorizontalLayout actionButtons = new HorizontalLayout(markButton);
            return actionButtons;
        }).setHeader("Actions");
    }

    // Method to filter attendance records based on the search term
    private void filterAttendance(String searchTerm) {
        List<AttendanceRecord> filteredRecords = attendanceRecords.stream()
                .filter(record -> record.getCourse().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        record.getStudent().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        attendanceGrid.setItems(filteredRecords);
    }

    // Method to open the Mark Attendance dialog
    private void openMarkAttendanceDialog(AttendanceRecord record) {
        Dialog markDialog = new Dialog();
        markDialog.addClassName("mark-attendance-dialog");

        // Dropdown for attendance status
        ComboBox<String> statusField = new ComboBox<>("Attendance Status");
        statusField.setItems("Present", "Absent");
        statusField.setValue(record.getStatus());
        statusField.addClassName("mark-attendance-status-field");

        // Attendance date field
        TextField dateField = new TextField("Date");
        dateField.setValue(record.getAttendanceDate().toString());
        dateField.addClassName("mark-attendance-date-field");

        // Buttons
        Button markButton = new Button("Mark", event -> {
            record.setStatus(statusField.getValue());
            record.setAttendanceDate(LocalDate.parse(dateField.getValue()));  // Update the record's date
            attendanceGrid.getDataProvider().refreshAll();  // Refresh the grid
            markDialog.close();
        });
        markButton.addClassName("mark-attendance-save-button");

        Button cancelButton = new Button("Cancel", event -> markDialog.close());
        cancelButton.addClassName("mark-attendance-cancel-button");

        markDialog.add(
                new H2("Mark Attendance"),
                statusField,
                dateField,
                new HorizontalLayout(markButton, cancelButton)
        );
        markDialog.open();
    }

    // Method to open the Add Attendance dialog
    private void openAddAttendanceDialog() {
        Dialog addDialog = new Dialog();
        addDialog.addClassName("add-attendance-dialog");

        // Text fields for course and student
        TextField courseField = new TextField("Course");
        courseField.addClassName("add-attendance-course-field");

        TextField studentField = new TextField("Student");
        studentField.addClassName("add-attendance-student-field");

        // Dropdown for attendance status
        ComboBox<String> statusField = new ComboBox<>("Attendance Status");
        statusField.setItems("Present", "Absent");
        statusField.addClassName("add-attendance-status-field");

        // Attendance date field
        TextField dateField = new TextField("Date");
        dateField.setValue(LocalDate.now().toString());  // Default to today's date
        dateField.addClassName("add-attendance-date-field");

        // Buttons
        Button addButton = new Button("Add", event -> {
            Notification.show("Attendance added successfully.");
            addDialog.close();
        });
        addButton.addClassName("add-attendance-save-button");

        Button cancelButton = new Button("Cancel", event -> addDialog.close());
        cancelButton.addClassName("add-attendance-cancel-button");

        addDialog.add(
                new H2("Add Attendance"),
                courseField,
                studentField,
                statusField,
                dateField,
                new HorizontalLayout(addButton, cancelButton)
        );
        addDialog.open();
    }

    // Mock AttendanceRecord class (Replace with backend later)
    public static class AttendanceRecord {
        private Long id;
        private String course;
        private String student;
        private String status;
        private LocalDate attendanceDate;

        public AttendanceRecord(Long id, String course, String student, String status, LocalDate attendanceDate) {
            this.id = id;
            this.course = course;
            this.student = student;
            this.status = status;
            this.attendanceDate = attendanceDate;
        }

        public String getCourse() {
            return course;
        }

        public String getStudent() {
            return student;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getAttendanceDate() {
            return attendanceDate;
        }

        public void setAttendanceDate(LocalDate attendanceDate) {
            this.attendanceDate = attendanceDate;
        }
    }
}

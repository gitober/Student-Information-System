package com.studentinfo.views.TeacherAttendanceView;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "teacher/attendance-tracking", layout = MainLayout.class)
@PageTitle("Attendance Tracking")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherAttendanceView/teacher-attendance-view.css")
public class TeacherAttendanceView extends Composite<VerticalLayout> {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final AuthenticatedUser authenticatedUser;

    private List<Attendance> attendanceRecords;
    private Grid<Attendance> attendanceGrid;
    private TextField searchField;
    private Button addAttendanceButton;

    @Autowired
    public TeacherAttendanceView(TeacherService teacherService, CourseService courseService, AuthenticatedUser authenticatedUser) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.authenticatedUser = authenticatedUser;

        // Fetch attendance records for the authenticated teacher
        attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());

        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-attendance-view-container");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        mainLayout.add(createPageHeader());

        configureSearchBar();
        configureGrid();

        if (attendanceRecords != null && !attendanceRecords.isEmpty()) {
            attendanceGrid.setItems(attendanceRecords);
        }

        addAttendanceButton = new Button("Add Attendance", event -> openAddAttendanceDialog());
        addAttendanceButton.addClassName("teacher-attendance-add-button");
        mainLayout.add(searchField, attendanceGrid, addAttendanceButton); // Add components to the layout
    }

    // Method to get the currently authenticated teacher's ID
    private Long getCurrentTeacherId() {
        String username = getCurrentAuthenticatedUsername();
        return teacherService.getTeacherByUsername(username)
                .orElseThrow(() -> new RuntimeException("Teacher not found for username: " + username))
                .getId();
    }

    private String getCurrentAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("teacher-attendance-page-header");

        H2 headerText = new H2("Attendance Tracking");
        headerText.addClassName("teacher-attendance-header");

        Paragraph description = new Paragraph("Track and update student attendance records.");
        description.addClassName("teacher-attendance-description");

        headerLayout.add(headerText, description);
        headerLayout.setPadding(false);
        headerLayout.setAlignItems(FlexComponent.Alignment.START);
        headerLayout.setWidthFull();
        return headerLayout;
    }

    private void configureSearchBar() {
        searchField = new TextField("Search by Course or Student");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterAttendance(event.getValue()));
        searchField.setWidthFull();
        searchField.addClassName("teacher-attendance-search");
    }

    private void configureGrid() {
        attendanceGrid = new Grid<>(Attendance.class, false);
        attendanceGrid.setItems(attendanceRecords); // Set real data here

        // Display the course name
        attendanceGrid.addColumn(record -> record.getCourse().getCourseName())
                .setHeader("Course")
                .setAutoWidth(true);

        // Display the full name of the student
        attendanceGrid.addColumn(record -> record.getStudent().getFirstName() + " " + record.getStudent().getLastName())
                .setHeader("Student")
                .setAutoWidth(true);

        attendanceGrid.addColumn(Attendance::getAttendanceStatus)
                .setHeader("Status")
                .setAutoWidth(true);

        attendanceGrid.addColumn(Attendance::getAttendanceDate)
                .setHeader("Date")
                .setAutoWidth(true);

        attendanceGrid.addComponentColumn(attendanceRecord -> {
            Button editButton = new Button("Edit");
            editButton.addClassName("teacher-attendance-edit-button");
            editButton.addClickListener(event -> openEditAttendanceDialog(attendanceRecord));

            Button deleteButton = new Button("Delete");
            deleteButton.addClassName("teacher-attendance-delete-button");
            deleteButton.addClickListener(event -> confirmDeleteAttendance(attendanceRecord));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
            actionButtons.addClassName("teacher-attendance-action-buttons");
            return actionButtons;
        }).setHeader("Actions");

        attendanceGrid.setHeight("400px");
        attendanceGrid.setWidthFull();
        attendanceGrid.addClassName("teacher-attendance-grid");
    }

    private void filterAttendance(String searchTerm) {
        List<Attendance> filteredRecords = attendanceRecords.stream()
                .filter(record -> record.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        (record.getStudent().getFirstName() + " " + record.getStudent().getLastName()).toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        attendanceGrid.setItems(filteredRecords);
    }

    private void openAddAttendanceDialog() {
        Dialog addDialog = new Dialog();
        addDialog.addClassName("teacher-attendance-add-dialog");

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("teacher-attendance-dialog-layout");

        // Add a message or title to guide the user
        Paragraph instruction = new Paragraph("Note: You can only add attendance for courses you are assigned to as a teacher.");
        instruction.addClassName("teacher-attendance-dialog-instruction");

        // Student ComboBox
        ComboBox<Student> studentComboBox = new ComboBox<>("Student");
        studentComboBox.setItems(teacherService.getAllStudents());
        studentComboBox.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());
        studentComboBox.addClassName("teacher-attendance-student-combobox");

        // Course ComboBox
        ComboBox<Course> courseComboBox = new ComboBox<>("Course");
        courseComboBox.addClassName("teacher-attendance-course-combobox");

        // When a student is selected, update the courses list
        studentComboBox.addValueChangeListener(event -> {
            Student selectedStudent = event.getValue();
            if (selectedStudent != null) {
                List<Course> enrolledCourses = courseService.getEnrolledCourses(selectedStudent.getStudentNumber());
                courseComboBox.setItems(enrolledCourses);
                courseComboBox.setItemLabelGenerator(Course::getCourseName);
            }
        });

        // Status ComboBox
        ComboBox<String> statusField = new ComboBox<>("Status", "Present", "Absent");
        statusField.addClassName("teacher-attendance-status-combobox");

        // Date Picker
        DatePicker datePicker = new DatePicker("Date");
        datePicker.setPlaceholder("dd.MM.yyyy");
        datePicker.setValue(LocalDate.now());
        datePicker.addClassName("teacher-attendance-date-picker");

        // Save button
        Button saveButton = new Button("Save", event -> {
            try {
                Attendance newRecord = new Attendance();
                newRecord.setCourse(courseComboBox.getValue());
                newRecord.setStudent(studentComboBox.getValue());
                newRecord.setAttendanceDate(datePicker.getValue());
                newRecord.setAttendanceStatus(statusField.getValue());

                teacherService.saveAttendanceRecord(newRecord);
                refreshAttendanceData(); // Call this method to refresh the grid
                addDialog.close();
            } catch (Exception e) {
                Notification.show("Invalid input. Please check the details.");
            }
        });
        saveButton.addClassName("teacher-attendance-save-button");

        // Close button
        Button closeButton = new Button("Close", event -> addDialog.close());
        closeButton.addClassName("teacher-attendance-close-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, closeButton);
        dialogButtons.addClassName("teacher-attendance-dialog-buttons");

        dialogLayout.add(instruction, studentComboBox, courseComboBox, statusField, datePicker, dialogButtons);
        addDialog.add(dialogLayout);
        addDialog.open();
    }



    // Refresh the attendance data
    private void refreshAttendanceData() {
        attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());
        System.out.println("Updated attendance records: " + attendanceRecords); // Debugging statement
        attendanceGrid.setItems(attendanceRecords);
    }




    private void openEditAttendanceDialog(Attendance record) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-attendance-dialog-edit-button");

        VerticalLayout dialogLayout = new VerticalLayout();

        TextField courseField = new TextField("Course");
        TextField studentField = new TextField("Student");
        ComboBox<String> statusField = new ComboBox<>("Status", "Present", "Absent");
        TextField dateField = new TextField("Date");

        courseField.setValue(record.getCourse().getCourseName());
        studentField.setValue(record.getStudent().getFirstName() + " " + record.getStudent().getLastName());
        statusField.setValue(record.getAttendanceStatus());
        dateField.setValue(record.getAttendanceDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Assign more unique class names to buttons for styling
        Button saveButton = new Button("Save", event -> {
            record.setAttendanceStatus(statusField.getValue());
            teacherService.saveAttendanceRecord(record);
            attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());
            attendanceGrid.setItems(attendanceRecords);
            editDialog.close();
        });
        saveButton.addClassName("teacher-attendance-dialog-edit-button-save");

        Button closeButton = new Button("Close", event -> editDialog.close());
        closeButton.addClassName("teacher-attendance-dialog-edit-button-close");

        dialogLayout.add(courseField, studentField, statusField, dateField, new HorizontalLayout(saveButton, closeButton));
        editDialog.add(dialogLayout);
        editDialog.open();
    }


    private void confirmDeleteAttendance(Attendance record) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.addClassName("teacher-attendance-delete-dialog");

        confirmDialog.add(new Paragraph("Are you sure you want to delete this attendance record?"));

        Button confirmButton = new Button("Delete", event -> {
            teacherService.deleteAttendanceRecord(record);
            attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());
            attendanceGrid.setItems(attendanceRecords);
            confirmDialog.close();
        });
        confirmButton.addClassName("teacher-attendance-delete-button");  // Apply the delete button class

        Button cancelButton = new Button("Cancel", event -> confirmDialog.close());
        cancelButton.addClassName("teacher-attendance-close-button");  // Apply the close button class

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        confirmDialog.add(buttonsLayout);
        confirmDialog.open();
    }
}
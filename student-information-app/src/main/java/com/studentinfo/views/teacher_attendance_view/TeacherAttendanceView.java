package com.studentinfo.views.teacher_attendance_view;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
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
import com.vaadin.flow.component.html.H3;
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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


@Route(value = "teacher/attendance-tracking", layout = MainLayout.class)
@PageTitle("Attendance Tracking")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/teacher_attendance_view/teacher-attendance-view.css")
public class TeacherAttendanceView extends Composite<VerticalLayout> {

    private final transient TeacherService teacherService;
    private final transient CourseService courseService;
    private final transient MessageSource messageSource;

    private transient List<Attendance> attendanceRecords;
    private final Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class, false);
    private TextField searchField;

    // Key for the close button in the dialog
    private static final String CLOSE_BUTTON_KEY = "teacher.attendance.close";

    private static final String STATUS_PRESENT_KEY = "teacher.attendance.status.present";

    private static final String STATUS_ABSENT_KEY = "teacher.attendance.status.absent";

    @Autowired
    public TeacherAttendanceView(TeacherService teacherService, CourseService courseService,
                                 AuthenticatedUser authenticatedUser, MessageSource messageSource) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.messageSource = messageSource;

        configureGrid();

        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-attendance-view-container");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        mainLayout.add(createPageHeader());
        configureSearchBar();

        refreshAttendanceData(); // Initial load with sorted data

        Button addAttendanceButton = new Button(getMessage("teacher.attendance.add.button"), event -> openAddAttendanceDialog());
        addAttendanceButton.addClassName("teacher-attendance-add-button");
        mainLayout.add(searchField, attendanceGrid, addAttendanceButton);
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("teacher-attendance-page-header");

        H2 headerText = new H2(getMessage("teacher.attendance.header"));
        headerText.addClassName("teacher-attendance-header");

        Paragraph description = new Paragraph(getMessage("teacher.attendance.description"));
        description.addClassName("teacher-attendance-description");

        headerLayout.add(headerText, description);
        headerLayout.setPadding(false);
        headerLayout.setAlignItems(FlexComponent.Alignment.START);
        headerLayout.setWidthFull();
        return headerLayout;
    }

    private void configureSearchBar() {
        searchField = new TextField(getMessage("teacher.attendance.search.placeholder"));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterAttendance(event.getValue()));
        searchField.setWidthFull();
        searchField.addClassName("teacher-attendance-search");
    }

    private void configureGrid() {
        attendanceGrid.addColumn(attendanceRecord -> attendanceRecord.getCourse() != null
                        ? attendanceRecord.getCourse().getCourseName()
                        : "Unknown Course")
                .setHeader(getMessage("teacher.attendance.grid.course"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(attendanceRecord -> attendanceRecord.getStudent() != null
                        ? attendanceRecord.getStudent().getFirstName() + " " + attendanceRecord.getStudent().getLastName()
                        : "Unknown Student")
                .setHeader(getMessage("teacher.attendance.grid.student"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(Attendance::getAttendanceStatus)
                .setHeader(getMessage("teacher.attendance.grid.status"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(attendanceRecord -> {
                    DateTimeFormatter formatter = getDateTimeFormatter();
                    return attendanceRecord.getAttendanceDate() != null
                            ? attendanceRecord.getAttendanceDate().format(formatter)
                            : "No Date";
                })
                .setHeader(getMessage("teacher.attendance.grid.date"))
                .setAutoWidth(true);

        attendanceGrid.addComponentColumn(attendanceRecord -> {
            Button editButton = new Button(getMessage("teacher.attendance.edit.button"));
            editButton.addClassName("teacher-attendance-edit-button");
            editButton.addClickListener(event -> openEditAttendanceDialog(attendanceRecord));

            Button deleteButton = new Button(getMessage("teacher.attendance.delete.button"));
            deleteButton.addClassName("teacher-attendance-delete-button");
            deleteButton.addClickListener(event -> confirmDeleteAttendance(attendanceRecord));

            return new HorizontalLayout(editButton, deleteButton);
        }).setHeader(getMessage("teacher.attendance.grid.actions"));

        attendanceGrid.setHeight("400px");
        attendanceGrid.setWidthFull();
        attendanceGrid.addClassName("teacher-attendance-grid");
    }

    private void filterAttendance(String searchTerm) {
        List<Attendance> filteredRecords = attendanceRecords.stream()
                .filter(attendanceRecord -> attendanceRecord.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        (attendanceRecord.getStudent().getFirstName() + " " + attendanceRecord.getStudent().getLastName()).toLowerCase().contains(searchTerm.toLowerCase()))
                .toList();
        attendanceGrid.setItems(filteredRecords);
    }

    private void openAddAttendanceDialog() {
        Dialog addDialog = new Dialog();
        addDialog.addClassName("teacher-attendance-add-dialog");
        addDialog.setModal(false);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("teacher-attendance-dialog-layout");

        // Generalized instruction message
        Paragraph instruction = new Paragraph(getMessage("teacher.attendance.add.instruction"));
        instruction.addClassName("teacher-attendance-dialog-instruction");

        ComboBox<Student> studentComboBox = new ComboBox<>(getMessage("teacher.attendance.student"));
        studentComboBox.setItems(teacherService.getAllStudents());
        studentComboBox.setItemLabelGenerator(student -> student.getFirstName() + " " + student.getLastName());
        studentComboBox.addClassName("teacher-attendance-student-combobox");

        ComboBox<Course> courseComboBox = new ComboBox<>(getMessage("teacher.attendance.course"));
        courseComboBox.addClassName("teacher-attendance-course-combobox");

        studentComboBox.addValueChangeListener(event -> {
            Student selectedStudent = event.getValue();
            if (selectedStudent != null) {
                List<Course> enrolledCourses = courseService.getEnrolledCourses(selectedStudent.getStudentNumber());
                courseComboBox.setItems(enrolledCourses);
                courseComboBox.setItemLabelGenerator(Course::getCourseName);
            }
        });

        ComboBox<String> statusField = new ComboBox<>(getMessage("teacher.attendance.status"));
        statusField.addClassName("teacher-attendance-status-combobox");
        statusField.setItems(getMessage(STATUS_PRESENT_KEY), getMessage(STATUS_ABSENT_KEY));
        statusField.setValue(getMessage(STATUS_PRESENT_KEY)); // Default value

        DatePicker datePicker = new DatePicker(getMessage("teacher.attendance.date"));
        datePicker.setValue(LocalDate.now());
        datePicker.addClassName("teacher-attendance-date-picker");

        Button saveButton = new Button(getMessage("teacher.attendance.save"), event -> {
            try {
                Attendance newRecord = new Attendance();
                newRecord.setCourse(courseComboBox.getValue());
                newRecord.setStudent(studentComboBox.getValue());
                newRecord.setAttendanceDate(datePicker.getValue());
                newRecord.setAttendanceStatus(getOriginalStatus(statusField.getValue()));

                teacherService.saveAttendanceRecord(newRecord);
                refreshAttendanceData();  // Refresh the grid to show the new record
                addDialog.close();
            } catch (Exception e) {
                Notification.show(getMessage("teacher.attendance.error.invalid.input"));
            }
        });
        saveButton.addClassName("teacher-attendance-save-button");

        Button closeButton = new Button(getMessage(CLOSE_BUTTON_KEY), event -> addDialog.close());
        closeButton.addClassName(CLOSE_BUTTON_KEY);

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, closeButton);
        dialogButtons.addClassName("teacher-attendance-dialog-buttons");

        dialogLayout.add(instruction, studentComboBox, courseComboBox, statusField, datePicker, dialogButtons);
        addDialog.add(dialogLayout);
        addDialog.open();
    }

    private void refreshAttendanceData() {
        // Fetch all attendance records from the service
        attendanceRecords = teacherService.getAttendanceRecords();

        // Sort attendance records by date in descending order (newest first)
        attendanceRecords.sort(Comparator.comparing(Attendance::getAttendanceDate, Comparator.reverseOrder()));

        // Update the grid with the sorted list
        attendanceGrid.setItems(attendanceRecords);
    }

    private void openEditAttendanceDialog(Attendance attendanceRecord) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-attendance-dialog-edit");
        editDialog.setModal(false);

        // Set the dialog to be non-modal (removes the grey overlay)
        editDialog.setModal(false);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.addClassName("teacher-attendance-dialog-layout");

        // Course field
        TextField courseField = new TextField(getMessage("teacher.attendance.course"));
        courseField.setReadOnly(true);
        courseField.addClassName("teacher-attendance-course-field");
        courseField.setValue(attendanceRecord.getCourse() != null
                ? attendanceRecord.getCourse().getCourseName()
                : getMessage("teacher.attendance.unknown.course"));

        // Student field
        TextField studentField = new TextField(getMessage("teacher.attendance.student"));
        studentField.setReadOnly(true);
        studentField.addClassName("teacher-attendance-student-field");
        studentField.setValue(attendanceRecord.getStudent() != null
                ? attendanceRecord.getStudent().getFirstName() + " " + attendanceRecord.getStudent().getLastName()
                : getMessage("teacher.attendance.unknown.student"));

        // Status field with translated options
        ComboBox<String> statusField = new ComboBox<>(getMessage(STATUS_PRESENT_KEY));
        statusField.addClassName("teacher-attendance-status-combobox");
        statusField.setItems(getMessage(STATUS_PRESENT_KEY), getMessage(STATUS_ABSENT_KEY));
        statusField.setValue(getTranslatedStatus(attendanceRecord.getAttendanceStatus()));

        // Date field
        TextField dateField = new TextField(getMessage("teacher.attendance.date"));
        dateField.setReadOnly(true);
        dateField.addClassName("teacher-attendance-date-field");
        dateField.setValue(attendanceRecord.getAttendanceDate() != null
                ? attendanceRecord.getAttendanceDate().format(getDateTimeFormatter())
                : getMessage("teacher.attendance.no.date"));

        // Save button
        Button saveButton = new Button(getMessage("teacher.attendance.save"), event -> {
            attendanceRecord.setAttendanceStatus(getOriginalStatus(statusField.getValue()));
            teacherService.saveAttendanceRecord(attendanceRecord);
            refreshAttendanceData();
            editDialog.close();
        });
        saveButton.addClassName("teacher-attendance-save-button");

        // Close button
        Button closeButton = new Button(getMessage(CLOSE_BUTTON_KEY), event -> editDialog.close());
        closeButton.addClassName("teacher-attendance-close-button");

        // Layout for buttons
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, closeButton);
        buttonLayout.addClassName("teacher-attendance-dialog-buttons");

        dialogLayout.add(courseField, studentField, statusField, dateField, buttonLayout);
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void confirmDeleteAttendance(Attendance attendanceRecord) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.addClassName("teacher-attendance-delete-dialog");
        confirmDialog.setModal(false);

        // Add a title for deletion confirmation
        H3 title = new H3(getMessage("teacher.attendance.delete.confirm.title"));
        title.addClassName("teacher-attendance-delete-title");
        confirmDialog.add(title);

        Button confirmButton = new Button(getMessage("teacher.attendance.delete.button"), event -> {
            teacherService.deleteAttendanceRecord(attendanceRecord);
            refreshAttendanceData();
            confirmDialog.close();
        });
        confirmButton.addClassName("teacher-attendance-delete-button");

        Button cancelButton = new Button(getMessage(CLOSE_BUTTON_KEY), event -> confirmDialog.close());
        cancelButton.addClassName("teacher-attendance-close-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        confirmDialog.add(buttonsLayout);
        confirmDialog.open();
    }

    // Helper method to translate status from internal value to displayed value
    private String getTranslatedStatus(String status) {
        if ("Present".equals(status)) {
            return getMessage(STATUS_PRESENT_KEY);
        } else if ("Absent".equals(status)) {
            return getMessage(STATUS_ABSENT_KEY);
        }
        return getMessage("teacher.attendance.status.unknown");
    }

    // Helper method to map displayed value back to internal status
    private String getOriginalStatus(String displayedStatus) {
        if (getMessage(STATUS_PRESENT_KEY).equals(displayedStatus)) {
            return "Present";
        } else if (getMessage(STATUS_ABSENT_KEY).equals(displayedStatus)) {
            return "Absent";
        }
        return null; // or handle as needed
    }

    private DateTimeFormatter getDateTimeFormatter() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale.getLanguage().equals("ch")
                ? DateTimeFormatter.ofPattern("yyyy年MM月dd日", locale)
                : DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
    }
}

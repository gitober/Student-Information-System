package com.studentinfo.views.TeacherAttendanceView;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Route(value = "teacher/attendance-tracking", layout = MainLayout.class)
@PageTitle("Attendance Tracking")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherAttendanceView/teacher-attendance-view.css")
public class TeacherAttendanceView extends Composite<VerticalLayout> {

    private final TeacherService teacherService;
    private final CourseService courseService;
    private final MessageSource messageSource;

    private List<Attendance> attendanceRecords;
    private final Grid<Attendance> attendanceGrid;
    private TextField searchField;

    @Autowired
    public TeacherAttendanceView(TeacherService teacherService, CourseService courseService, AuthenticatedUser authenticatedUser, MessageSource messageSource) {
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.messageSource = messageSource;

        attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());
        if (attendanceRecords == null) {
            attendanceRecords = Collections.emptyList();
        }

        attendanceGrid = new Grid<>(Attendance.class, false);
        configureGrid();

        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-attendance-view-container");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        mainLayout.add(createPageHeader());

        configureSearchBar();

        if (!attendanceRecords.isEmpty()) {
            attendanceGrid.setItems(attendanceRecords);
        }

        Button addAttendanceButton = new Button(getMessage("teacher.attendance.add.button"), event -> openAddAttendanceDialog());
        addAttendanceButton.addClassName("teacher-attendance-add-button");
        mainLayout.add(searchField, attendanceGrid, addAttendanceButton);
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    private Long getCurrentTeacherId() {
        String email = getCurrentAuthenticatedEmail();
        return teacherService.getTeacherByUsername(email)
                .orElseThrow(() -> new RuntimeException("Teacher not found for email: " + email))
                .getId();
    }

    private String getCurrentAuthenticatedEmail() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails
                ? userDetails.getUsername()
                : null;
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
        attendanceGrid.setItems(attendanceRecords);

        attendanceGrid.addColumn(record -> record.getCourse().getCourseName())
                .setHeader(getMessage("teacher.attendance.grid.course"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(record -> record.getStudent().getFirstName() + " " + record.getStudent().getLastName())
                .setHeader(getMessage("teacher.attendance.grid.student"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(Attendance::getAttendanceStatus)
                .setHeader(getMessage("teacher.attendance.grid.status"))
                .setAutoWidth(true);

        attendanceGrid.addColumn(record -> {
                    DateTimeFormatter formatter = getDateTimeFormatter();
                    return record.getAttendanceDate().format(formatter);
                }).setHeader(getMessage("teacher.attendance.grid.date"))
                .setAutoWidth(true);

        attendanceGrid.addComponentColumn(attendanceRecord -> {
            Button editButton = new Button(getMessage("teacher.attendance.edit.button"));
            editButton.addClassName("teacher-attendance-edit-button");
            editButton.addClickListener(event -> openEditAttendanceDialog(attendanceRecord));

            Button deleteButton = new Button(getMessage("teacher.attendance.delete.button"));
            deleteButton.addClassName("teacher-attendance-delete-button");
            deleteButton.addClickListener(event -> confirmDeleteAttendance(attendanceRecord));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
            actionButtons.addClassName("teacher-attendance-action-buttons");
            return actionButtons;
        }).setHeader(getMessage("teacher.attendance.grid.actions"));

        attendanceGrid.setHeight("400px");
        attendanceGrid.setWidthFull();
        attendanceGrid.addClassName("teacher-attendance-grid");
    }

    private DateTimeFormatter getDateTimeFormatter() {
        Locale locale = LocaleContextHolder.getLocale();
        return locale.getLanguage().equals("ch")
                ? DateTimeFormatter.ofPattern("yyyy年MM月dd日", locale)
                : DateTimeFormatter.ofPattern("dd.MM.yyyy", locale);
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

        ComboBox<String> statusField = new ComboBox<>(getMessage("teacher.attendance.status"), "Present", "Absent");
        statusField.addClassName("teacher-attendance-status-combobox");

        DatePicker datePicker = new DatePicker(getMessage("teacher.attendance.date"));
        datePicker.setValue(LocalDate.now());
        datePicker.addClassName("teacher-attendance-date-picker");

        Button saveButton = new Button(getMessage("teacher.attendance.save"), event -> {
            try {
                Attendance newRecord = new Attendance();
                newRecord.setCourse(courseComboBox.getValue());
                newRecord.setStudent(studentComboBox.getValue());
                newRecord.setAttendanceDate(datePicker.getValue());
                newRecord.setAttendanceStatus(statusField.getValue());

                teacherService.saveAttendanceRecord(newRecord);
                refreshAttendanceData();
                attendanceGrid.getDataProvider().refreshAll();
                addDialog.close();
            } catch (Exception e) {
                Notification.show(getMessage("teacher.attendance.error.invalid.input"));
            }
        });
        saveButton.addClassName("teacher-attendance-save-button");

        Button closeButton = new Button(getMessage("teacher.attendance.close"), event -> addDialog.close());
        closeButton.addClassName("teacher-attendance-close-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, closeButton);
        dialogButtons.addClassName("teacher-attendance-dialog-buttons");

        dialogLayout.add(instruction, studentComboBox, courseComboBox, statusField, datePicker, dialogButtons);
        addDialog.add(dialogLayout);
        addDialog.open();
    }

    private void refreshAttendanceData() {
        attendanceRecords = teacherService.getAttendanceRecordsForTeacher(getCurrentTeacherId());
        attendanceGrid.setItems(attendanceRecords);
    }

    private void openEditAttendanceDialog(Attendance record) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-attendance-dialog-edit-button");

        VerticalLayout dialogLayout = new VerticalLayout();

        TextField courseField = new TextField(getMessage("teacher.attendance.course"));
        TextField studentField = new TextField(getMessage("teacher.attendance.student"));
        ComboBox<String> statusField = new ComboBox<>(getMessage("teacher.attendance.status"), "Present", "Absent");
        TextField dateField = new TextField(getMessage("teacher.attendance.date"));

        courseField.setValue(record.getCourse().getCourseName());
        studentField.setValue(record.getStudent().getFirstName() + " " + record.getStudent().getLastName());
        statusField.setValue(record.getAttendanceStatus());
        dateField.setValue(record.getAttendanceDate().format(getDateTimeFormatter()));

        Button saveButton = new Button(getMessage("teacher.attendance.save"), event -> {
            record.setAttendanceStatus(statusField.getValue());
            teacherService.saveAttendanceRecord(record);
            refreshAttendanceData();
            editDialog.close();
        });
        saveButton.addClassName("teacher-attendance-dialog-edit-button-save");

        Button closeButton = new Button(getMessage("teacher.attendance.close"), event -> editDialog.close());
        closeButton.addClassName("teacher-attendance-dialog-edit-button-close");

        dialogLayout.add(courseField, studentField, statusField, dateField, new HorizontalLayout(saveButton, closeButton));
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void confirmDeleteAttendance(Attendance record) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.addClassName("teacher-attendance-delete-dialog");

        confirmDialog.add(new Paragraph(getMessage("teacher.attendance.grid.actions")));

        Button confirmButton = new Button(getMessage("teacher.attendance.delete.button"), event -> {
            teacherService.deleteAttendanceRecord(record);
            refreshAttendanceData();
            confirmDialog.close();
        });
        confirmButton.addClassName("teacher-attendance-delete-button");

        Button cancelButton = new Button(getMessage("teacher.attendance.close"), event -> confirmDialog.close());
        cancelButton.addClassName("teacher-attendance-close-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        confirmDialog.add(buttonsLayout);
        confirmDialog.open();
    }
}

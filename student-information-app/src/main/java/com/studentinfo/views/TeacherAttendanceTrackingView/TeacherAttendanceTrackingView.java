package com.studentinfo.views.TeacherAttendanceTrackingView;

import com.studentinfo.data.entity.AttendanceRecord;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.TeacherService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "teacher/attendance-tracking", layout = MainLayout.class)
@PageTitle("Attendance Tracking")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherAttendanceTrackingView/teacher-attendance-view.css")
public class TeacherAttendanceTrackingView extends Composite<VerticalLayout> {

    private final TeacherService teacherService;
    private final AuthenticatedUser authenticatedUser;

    private List<AttendanceRecord> attendanceRecords;
    private Grid<AttendanceRecord> attendanceGrid;
    private TextField searchField;
    private Button addAttendanceButton;

    @Autowired
    public TeacherAttendanceTrackingView(TeacherService teacherService, AuthenticatedUser authenticatedUser) {
        this.teacherService = teacherService;
        this.authenticatedUser = authenticatedUser;

        // Load attendance records
        attendanceRecords = teacherService.getAttendanceRecords();

        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-attendance-view-container");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        mainLayout.add(createPageHeader());

        configureSearchBar();
        configureGrid();

        attendanceGrid.setItems(attendanceRecords); // Set real data here

        addAttendanceButton = new Button("Add Attendance", event -> openAddAttendanceDialog());
        addAttendanceButton.addClassName("teacher-attendance-add-button");
        mainLayout.add(addAttendanceButton);
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
        attendanceGrid = new Grid<>(AttendanceRecord.class, false);
        attendanceGrid.setItems(attendanceRecords);  // Set real data here

        attendanceGrid.addColumn(record -> record.getCourse().getCourseName())
                .setHeader("Course")
                .setAutoWidth(true);

        attendanceGrid.addColumn(record -> record.getStudent().getFirstName() + " " + record.getStudent().getLastName())
                .setHeader("Student")
                .setAutoWidth(true);

        attendanceGrid.addColumn(AttendanceRecord::getAttendanceStatus)
                .setHeader("Status")
                .setAutoWidth(true);

        attendanceGrid.addColumn(AttendanceRecord::getAttendanceDate)
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
        List<AttendanceRecord> filteredRecords = attendanceRecords.stream()
                .filter(record -> record.getCourse().getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        (record.getStudent().getFirstName() + " " + record.getStudent().getLastName()).toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        attendanceGrid.setItems(filteredRecords);
    }

    private void openAddAttendanceDialog() {
        Dialog addDialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();

        TextField courseField = new TextField("Course");
        TextField studentField = new TextField("Student");
        ComboBox<String> statusField = new ComboBox<>("Status", "Present", "Absent");
        TextField dateField = new TextField("Date (dd.MM.yyyy)");

        Button saveButton = new Button("Save", event -> {
            try {
                AttendanceRecord newRecord = new AttendanceRecord();

                // DateTimeFormatter to parse the date field
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                newRecord.setAttendanceDate(LocalDate.parse(dateField.getValue(), formatter));
                newRecord.setAttendanceStatus(statusField.getValue());

                teacherService.saveAttendanceRecord(newRecord);
                attendanceRecords = teacherService.getAttendanceRecords();
                attendanceGrid.setItems(attendanceRecords);
                addDialog.close();
            } catch (DateTimeParseException e) {
                Notification.show("Invalid date format. Please use dd.MM.yyyy.");
            }
        });

        // Add close button ("X")
        Button closeButton = new Button("x", event -> addDialog.close());
        closeButton.addClassName("dialog-close-button");

        dialogLayout.add(closeButton, courseField, studentField, statusField, dateField, saveButton);
        addDialog.add(dialogLayout);
        addDialog.open();
    }

    private void openEditAttendanceDialog(AttendanceRecord record) {
        Dialog editDialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();

        TextField courseField = new TextField("Course");
        TextField studentField = new TextField("Student");
        ComboBox<String> statusField = new ComboBox<>("Status", "Present", "Absent");
        TextField dateField = new TextField("Date");

        courseField.setValue(record.getCourse().getCourseName());
        studentField.setValue(record.getStudent().getFirstName() + " " + record.getStudent().getLastName());
        statusField.setValue(record.getAttendanceStatus());
        dateField.setValue(record.getAttendanceDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        Button saveButton = new Button("Save", event -> {
            record.setAttendanceStatus(statusField.getValue());
            teacherService.saveAttendanceRecord(record);
            attendanceRecords = teacherService.getAttendanceRecords();
            attendanceGrid.setItems(attendanceRecords);
            editDialog.close();
        });

        // Add close button ("X")
        Button closeButton = new Button("x", event -> editDialog.close());
        closeButton.addClassName("dialog-close-button");

        dialogLayout.add(closeButton, courseField, studentField, statusField, dateField, saveButton);
        editDialog.add(dialogLayout);
        editDialog.open();
    }

    private void confirmDeleteAttendance(AttendanceRecord record) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Paragraph("Are you sure you want to delete this attendance record?"));

        Button confirmButton = new Button("Delete", event -> {
            teacherService.deleteAttendanceRecord(record);
            attendanceRecords = teacherService.getAttendanceRecords();
            attendanceGrid.setItems(attendanceRecords);
            confirmDialog.close();
        });
        Button cancelButton = new Button("Cancel", event -> confirmDialog.close());

        // Add close button ("X")
        Button closeButton = new Button("x", event -> confirmDialog.close());
        closeButton.addClassName("dialog-close-button");

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
        confirmDialog.add(closeButton, buttonsLayout);
        confirmDialog.open();
    }


}

package com.studentinfo.views.TeacherUpdateStudentProfileView;

import com.studentinfo.data.entity.Student;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.StudentService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "teacher/update-students", layout = MainLayout.class)
@PageTitle("Update Student Profiles")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherUpdateStudentProfile/teacher-update-student-profile.css")
public class TeacherUpdateStudentProfileView extends Composite<VerticalLayout> {

    private final StudentService studentService;
    private final MessageSource messageSource;
    private final List<Student> students;
    private Grid<Student> studentGrid;
    private TextField searchField;

    private TextField firstNameField;
    private TextField lastNameField;
    private EmailField emailField;
    private TextField phoneNumberField;
    private Button saveButton;

    private Student selectedStudent;

    @Autowired
    public TeacherUpdateStudentProfileView(StudentService studentService, AuthenticatedUser authenticatedUser, MessageSource messageSource) {
        this.studentService = studentService;
        this.messageSource = messageSource;

        students = studentService.list();
        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-update-student-profile-view");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        mainLayout.add(createPageHeader());
        configureSearchBar();
        configureGrid();

        mainLayout.add(searchField, studentGrid);
        configureForm();
        mainLayout.add(createFormLayout());
    }

    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("teacher-update-student-profile-page-header");

        H2 headerText = new H2(getMessage("teacher.update.student.header"));
        headerText.addClassName("teacher-update-student-profile-header");

        Paragraph description = new Paragraph(getMessage("teacher.update.student.description"));
        description.addClassName("teacher-update-student-profile-description");

        headerLayout.add(headerText, description);
        headerLayout.setPadding(false);
        headerLayout.setAlignItems(FlexComponent.Alignment.START);
        headerLayout.setWidthFull();
        return headerLayout;
    }

    private void configureSearchBar() {
        searchField = new TextField(getMessage("teacher.update.student.search.placeholder"));
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterStudents(event.getValue()));
        searchField.setWidthFull();
        searchField.addClassName("teacher-update-student-search");
    }

    private void configureGrid() {
        studentGrid = new Grid<>(Student.class, false);
        studentGrid.setItems(students);

        studentGrid.addColumn(Student::getFirstName)
                .setHeader(getMessage("teacher.update.student.grid.firstName"))
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getLastName)
                .setHeader(getMessage("teacher.update.student.grid.lastName"))
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getEmail)
                .setHeader(getMessage("teacher.update.student.grid.email"))
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getPhoneNumber)
                .setHeader(getMessage("teacher.update.student.grid.phone"))
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.setHeight("400px");
        studentGrid.setWidthFull();
        studentGrid.addClassName("teacher-update-student-grid");

        studentGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedStudent = event.getValue();
            if (selectedStudent != null) {
                populateForm(selectedStudent);
            }
        });
    }

    private void configureForm() {
        firstNameField = new TextField(getMessage("teacher.update.student.form.firstName"));
        lastNameField = new TextField(getMessage("teacher.update.student.form.lastName"));
        emailField = new EmailField(getMessage("teacher.update.student.form.email"));
        phoneNumberField = new TextField(getMessage("teacher.update.student.form.phone"));

        saveButton = new Button(getMessage("teacher.update.student.form.save"), event -> saveStudent());

        setVisibleForm(false);
    }

    private VerticalLayout createFormLayout() {
        VerticalLayout formLayout = new VerticalLayout(firstNameField, lastNameField, emailField, phoneNumberField, saveButton);
        formLayout.addClassName("teacher-update-student-form-container");
        formLayout.setWidthFull();
        formLayout.setSpacing(true);
        formLayout.setPadding(true);
        return formLayout;
    }

    private void filterStudents(String searchTerm) {
        clearForm();

        List<Student> filteredStudents = students.stream()
                .filter(student -> student.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        student.getLastName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        studentGrid.setItems(filteredStudents);
    }

    private void populateForm(Student student) {
        if (student != null) {
            firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
            lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
            emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
            phoneNumberField.setValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
            setVisibleForm(true);
        } else {
            clearForm();
        }
    }

    private void saveStudent() {
        if (selectedStudent != null) {
            selectedStudent.setFirstName(firstNameField.getValue());
            selectedStudent.setLastName(lastNameField.getValue());
            selectedStudent.setEmail(emailField.getValue());
            selectedStudent.setPhoneNumber(phoneNumberField.getValue());

            studentService.save(selectedStudent);
            studentGrid.getDataProvider().refreshAll();
            clearForm();
        }
    }

    private void clearForm() {
        selectedStudent = null;
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        phoneNumberField.clear();
        setVisibleForm(false);
    }

    private void setVisibleForm(boolean visible) {
        firstNameField.setVisible(visible);
        lastNameField.setVisible(visible);
        emailField.setVisible(visible);
        phoneNumberField.setVisible(visible);
        saveButton.setVisible(visible);
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }
}

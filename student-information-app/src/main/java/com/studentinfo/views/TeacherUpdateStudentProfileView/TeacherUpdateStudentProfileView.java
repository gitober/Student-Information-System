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

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "teacher/update-students", layout = MainLayout.class)
@PageTitle("Update Student Profiles")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherUpdateStudentProfile/teacher-update-student-profile.css")
public class TeacherUpdateStudentProfileView extends Composite<VerticalLayout> {

    private final StudentService studentService;
    private final AuthenticatedUser authenticatedUser;

    private List<Student> students;
    private Grid<Student> studentGrid;
    private TextField searchField;

    // Fields for editing student information
    private TextField firstNameField;
    private TextField lastNameField;
    private EmailField emailField;
    private TextField phoneNumberField;
    private Button saveButton;

    private Student selectedStudent;

    @Autowired
    public TeacherUpdateStudentProfileView(StudentService studentService, AuthenticatedUser authenticatedUser) {
        this.studentService = studentService;
        this.authenticatedUser = authenticatedUser;

        // Fetch students
        students = studentService.list();

        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.addClassName("teacher-update-student-profile-view");
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        // Add the page header
        mainLayout.add(createPageHeader());

        // Configure and add the search bar and grid
        configureSearchBar();
        configureGrid();

        // Add the search bar and grid to the layout
        mainLayout.add(searchField, studentGrid);

        // Add the form for updating the student profile
        configureForm();
        mainLayout.add(createFormLayout());
    }

    // Create the page header layout (title + description)
    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("teacher-update-student-profile-page-header");

        H2 headerText = new H2("Update Student Profiles");
        headerText.addClassName("teacher-update-student-profile-header");

        Paragraph description = new Paragraph("Here you can update the details of your students.");
        description.addClassName("teacher-update-student-profile-description");

        headerLayout.add(headerText, description);
        headerLayout.setPadding(false);
        headerLayout.setAlignItems(FlexComponent.Alignment.START);
        headerLayout.setWidthFull();  // Ensure the header takes full width
        return headerLayout;
    }

    // Configure the search bar
    private void configureSearchBar() {
        searchField = new TextField("Search by First Name or Last Name");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterStudents(event.getValue()));
        searchField.setWidthFull(); // Set width to full to match other elements
        searchField.addClassName("teacher-update-student-search");
    }

    // Configure the grid
    private void configureGrid() {
        studentGrid = new Grid<>(Student.class, false);
        studentGrid.setItems(students);

        studentGrid.addColumn(Student::getFirstName)
                .setHeader("First Name")
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getLastName)
                .setHeader("Last Name")
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getEmail)
                .setHeader("Email")
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.addColumn(Student::getPhoneNumber)
                .setHeader("Phone Number")
                .setAutoWidth(true)
                .setFlexGrow(1);

        studentGrid.setHeight("400px");
        studentGrid.setWidthFull(); // Make sure the grid takes the full width
        studentGrid.addClassName("teacher-update-student-grid");

        // Handle row selection for updating the profile
        studentGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedStudent = event.getValue();
            if (selectedStudent != null) {
                populateForm(selectedStudent);
            }
        });
    }

    // Configure the form layout
    private void configureForm() {
        firstNameField = new TextField("First Name");
        firstNameField.addClassName("teacher-update-student-firstname");

        lastNameField = new TextField("Last Name");
        lastNameField.addClassName("teacher-update-student-lastname");

        emailField = new EmailField("Email");
        emailField.addClassName("teacher-update-student-email");

        phoneNumberField = new TextField("Phone Number");
        phoneNumberField.addClassName("teacher-update-student-phone");

        saveButton = new Button("Save changes", event -> saveStudent());
        saveButton.addClassName("teacher-update-student-save-button");

        // Initially, the form is hidden
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

    // Filter students based on the search term
    private void filterStudents(String searchTerm) {
        clearForm();  // Clear the form whenever a search is made

        // Filter the students based on the search term
        List<Student> filteredStudents = students.stream()
                .filter(student -> student.getFirstName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        student.getLastName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Update the grid with the filtered students
        studentGrid.setItems(filteredStudents);
    }

    // Populate the form with selected student's data
    private void populateForm(Student student) {
        if (student != null) {
            firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
            lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
            emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
            phoneNumberField.setValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
            setVisibleForm(true);  // Show the form when a student is selected
        } else {
            clearForm();  // Clear the form if no student is selected
        }
    }

    // Save changes to the student
    private void saveStudent() {
        if (selectedStudent != null) {
            selectedStudent.setFirstName(firstNameField.getValue());
            selectedStudent.setLastName(lastNameField.getValue());
            selectedStudent.setEmail(emailField.getValue());
            selectedStudent.setPhoneNumber(phoneNumberField.getValue());

            // Save the updated student in the database
            studentService.save(selectedStudent);

            // Refresh the grid to reflect the changes
            studentGrid.getDataProvider().refreshAll();

            // Clear the form after saving
            clearForm();
        }
    }

    // Clear the form fields and reset the selected student
    private void clearForm() {
        selectedStudent = null;  // Reset the selected student
        firstNameField.clear();  // Clear the first name field
        lastNameField.clear();   // Clear the last name field
        emailField.clear();      // Clear the email field
        phoneNumberField.clear(); // Clear the phone number field

        // Hide the form after clearing
        setVisibleForm(false);
    }

    // Set form visibility
    private void setVisibleForm(boolean visible) {
        firstNameField.setVisible(visible);
        lastNameField.setVisible(visible);
        emailField.setVisible(visible);
        phoneNumberField.setVisible(visible);
        saveButton.setVisible(visible);
    }
}

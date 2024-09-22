package com.studentinfo.views.TeacherUpdateStudentProfileView;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.studentinfo.security.AuthenticatedUser;

import java.util.List;

@Route(value = "teacher/update-students", layout = MainLayout.class)
@PageTitle("Update Student Profiles")
@RolesAllowed("USER")
@CssImport("./themes/studentinformationapp/views/TeacherUpdateStudentProfile/teacher-update-student-profile.css")
public class TeacherUpdateStudentProfileView extends VerticalLayout {

    private final StudentService studentService;
    private final AuthenticatedUser authenticatedUser;

    private final Grid<Student> studentGrid = new Grid<>(Student.class);
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final TextField phoneNumberField = new TextField("Phone Number");
    private final EmailField emailField = new EmailField("Email");
    private final Button saveButton = new Button("Save changes", event -> saveStudent());
    private Student selectedStudent;

    @Autowired
    public TeacherUpdateStudentProfileView(StudentService studentService, AuthenticatedUser authenticatedUser) {
        this.studentService = studentService;
        this.authenticatedUser = authenticatedUser;

        // Set layout settings to center the content
        setPadding(false);
        setSpacing(false);
        setAlignItems(FlexComponent.Alignment.CENTER); // Center the content horizontally
        setJustifyContentMode(JustifyContentMode.CENTER); // Center the content vertically

        // Add padding to prevent content from sticking to or going under the header
        getStyle().set("padding-top", "60px"); // Adjust based on header height

        addClassName("teacher-update-student-profile-view"); // Add the CSS class for consistent styling

        // Add header for the page
        add(createPageHeader());

        configureGrid();
        configureForm();

        // Wrap form fields and button in a layout for proper centering
        VerticalLayout formLayout = new VerticalLayout(firstNameField, lastNameField, phoneNumberField, emailField, saveButton);
        formLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        formLayout.setSpacing(true);
        formLayout.setPadding(true);

        add(studentGrid, formLayout); // Add grid and centered form layout
        updateGrid();
    }

    private VerticalLayout createPageHeader() {
        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.addClassName("page-header");

        // Add header text
        headerLayout.add(new H1("Update Student Profiles"));

        // Add description text
        Paragraph description = new Paragraph("Here you can update the details of your students. Select a student from the grid to edit their profile information.");
        description.addClassName("page-description");

        headerLayout.add(description);
        headerLayout.setPadding(false); // Adjust padding if necessary

        return headerLayout;
    }

    private void configureGrid() {
        // Clear any automatically added columns
        studentGrid.removeAllColumns();

        // Define only the columns that are needed with headers and appropriate widths
        studentGrid.addColumn(Student::getFirstName).setHeader("First Name").setAutoWidth(true).setFlexGrow(1);
        studentGrid.addColumn(Student::getLastName).setHeader("Last Name").setAutoWidth(true).setFlexGrow(1);
        studentGrid.addColumn(Student::getEmail).setHeader("Email").setAutoWidth(true).setFlexGrow(1);
        studentGrid.addColumn(Student::getPhoneNumber).setHeader("Phone Number").setAutoWidth(true).setFlexGrow(1);

        // Set a fixed height for the grid to limit the number of visible rows
        studentGrid.setHeight("400px"); // Adjust as needed to make it visually appealing

        // Add a listener to detect when a row is selected
        studentGrid.asSingleSelect().addValueChangeListener(event -> {
            Student selectedStudent = event.getValue();
            if (selectedStudent != null) {
                populateForm(selectedStudent); // Populate the form with the selected student's data
            } else {
                clearForm(); // Clear the form if no student is selected
            }
        });
    }

    private void configureForm() {
        // Set the fields to fixed width
        firstNameField.setWidth("600px");
        lastNameField.setWidth("600px");
        phoneNumberField.setWidth("600px");
        emailField.setWidth("600px");
        saveButton.setWidthFull();

        // Hide the form fields initially
        setVisibleForm(false);
    }

    private void updateGrid() {
        List<Student> students = studentService.list();
        studentGrid.setItems(students);
    }

    private void populateForm(Student student) {
        selectedStudent = student;
        firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
        lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
        phoneNumberField.setValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
        emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
        setVisibleForm(true); // Make sure the form is visible
    }

    private void clearForm() {
        selectedStudent = null;
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        emailField.clear();
        setVisibleForm(false);
    }

    private void saveStudent() {
        if (selectedStudent != null) {
            selectedStudent.setFirstName(firstNameField.getValue());
            selectedStudent.setLastName(lastNameField.getValue());
            selectedStudent.setPhoneNumber(phoneNumberField.getValue());
            selectedStudent.setEmail(emailField.getValue());
            studentService.save(selectedStudent); // Save changes to the database
            updateGrid(); // Refresh the grid to reflect changes
            clearForm(); // Clear the form after saving
        }
    }

    private void setVisibleForm(boolean visible) {
        firstNameField.setVisible(visible);
        lastNameField.setVisible(visible);
        phoneNumberField.setVisible(visible);
        emailField.setVisible(visible);
        saveButton.setVisible(visible);
    }
}

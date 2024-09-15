package com.studentinfo.views.TeacherUpdateStudentProfileView;

import com.studentinfo.data.entity.Student;
import com.studentinfo.services.StudentService;
import com.studentinfo.views.mainlayout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "teacher/update-students", layout = MainLayout.class)
@PageTitle("Update Student Profiles")
@RolesAllowed("USER")
public class TeacherUpdateStudentProfileView extends VerticalLayout {

    private final StudentService studentService;
    private final Grid<Student> studentGrid = new Grid<>(Student.class);
    private final TextField firstNameField = new TextField("First Name");
    private final TextField lastNameField = new TextField("Last Name");
    private final TextField phoneNumberField = new TextField("Phone Number");
    private final EmailField emailField = new EmailField("Email");
    private Student selectedStudent;

    @Autowired
    public TeacherUpdateStudentProfileView(StudentService studentService) {
        this.studentService = studentService;
        setSizeFull();
        configureGrid();
        configureForm();

        add(studentGrid);
        add(firstNameField, lastNameField, phoneNumberField, emailField, createSaveButton());
        updateGrid();
    }

    private void configureGrid() {
        // Clear any automatically added columns
        studentGrid.removeAllColumns();

        // Define only the columns that are needed with headers and appropriate widths
        studentGrid.addColumn(Student::getFirstName).setHeader("First Name").setAutoWidth(true).setFlexGrow(0);
        studentGrid.addColumn(Student::getLastName).setHeader("Last Name").setAutoWidth(true).setFlexGrow(0);
        studentGrid.addColumn(Student::getEmail).setHeader("Email").setAutoWidth(true).setFlexGrow(0);
        studentGrid.addColumn(Student::getPhoneNumber).setHeader("Phone Number").setAutoWidth(true).setFlexGrow(0);

        // Set a fixed height for the grid to limit the number of visible rows
        studentGrid.setHeight("400px"); // Adjust as needed to make it visually appealing

        // Add a listener to detect when a row is selected
        studentGrid.asSingleSelect().addValueChangeListener(event -> {
            Student selectedStudent = event.getValue();
            if (selectedStudent != null) {
                populateForm(selectedStudent); // Populate the form with the selected student's data
                setVisibleForm(true); // Ensure the form is visible when a student is selected
            } else {
                setVisibleForm(false); // Hide the form if no student is selected
            }
        });
    }

    private void configureForm() {
        firstNameField.setWidthFull();
        lastNameField.setWidthFull();
        phoneNumberField.setWidthFull();
        emailField.setWidthFull();
        setVisibleForm(false); // Hide the form initially
    }

    private void updateGrid() {
        List<Student> students = studentService.list();
        studentGrid.setItems(students);
    }

    private void populateForm(Student student) {
        if (student != null) {
            selectedStudent = student;
            firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
            lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
            phoneNumberField.setValue(student.getPhoneNumber() != null ? student.getPhoneNumber() : "");
            emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
            setVisibleForm(true); // Make sure the form is visible
        } else {
            setVisibleForm(false); // Hide the form if no student is selected
        }
    }

    private Button createSaveButton() {
        Button saveButton = new Button("Save", event -> saveStudent());
        saveButton.setWidthFull();
        return saveButton;
    }

    private void saveStudent() {
        if (selectedStudent != null) {
            selectedStudent.setFirstName(firstNameField.getValue());
            selectedStudent.setLastName(lastNameField.getValue());
            selectedStudent.setPhoneNumber(phoneNumberField.getValue());
            selectedStudent.setEmail(emailField.getValue());
            studentService.save(selectedStudent); // Save changes to the database
            updateGrid(); // Refresh the grid to reflect changes
            setVisibleForm(false); // Hide the form after saving
        }
    }

    private void setVisibleForm(boolean visible) {
        firstNameField.setVisible(visible);
        lastNameField.setVisible(visible);
        phoneNumberField.setVisible(visible);
        emailField.setVisible(visible);
    }
}

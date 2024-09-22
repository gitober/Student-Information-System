package com.studentinfo.views.grades;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./themes/studentinformationapp/views/grades-view/teacher-grades-view.css")
public class TeacherGradesView extends Composite<VerticalLayout> {

    private List<GradeEntry> gradeEntries; // Store the grade data
    private Grid<GradeEntry> gradesGrid;

    public TeacherGradesView() {
        getContent().addClassName("teacher-grades-view-container");

        // Page title
        H2 title = new H2("Grades Management");
        title.addClassName("teacher-grades-view-title");

        // Search bar to filter grades by student or course
        TextField searchField = new TextField("Search by Student or Course");
        searchField.addClassName("teacher-grades-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterGrades(event.getValue()));

        // Grid for displaying grades (Course -> Student -> Grade)
        gradesGrid = new Grid<>(GradeEntry.class, false);
        gradesGrid.addColumn(GradeEntry::getCourse).setHeader("Course").setClassNameGenerator(entry -> "teacher-grades-view-course-column");
        gradesGrid.addColumn(GradeEntry::getStudent).setHeader("Student").setClassNameGenerator(entry -> "teacher-grades-view-student-column");
        gradesGrid.addColumn(GradeEntry::getGrade).setHeader("Grade").setClassNameGenerator(entry -> "teacher-grades-view-grade-column");
        gradesGrid.addClassName("teacher-grades-view-grid");

        // Add mock data (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
        gradeEntries = Arrays.asList(
                new GradeEntry("Math 101", "Alice", "A"),
                new GradeEntry("Physics 101", "Bob", "B"),
                new GradeEntry("Chemistry 101", "Charlie", "C")
        );
        gradesGrid.setItems(gradeEntries);

        // Add edit and delete buttons for each grade entry
        gradesGrid.addComponentColumn(entry -> {
            Button editButton = new Button("Edit");
            editButton.addClassName("teacher-grades-view-edit-button");

            Button deleteButton = new Button("Delete");
            deleteButton.addClassName("teacher-grades-view-delete-button");

            // Edit Button functionality
            editButton.addClickListener(event -> openEditGradeDialog(entry));

            // Delete Button functionality
            deleteButton.addClickListener(event -> openDeleteGradeConfirmationDialog(entry));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, deleteButton);
            actionButtons.addClassName("teacher-grades-view-action-buttons");
            return actionButtons;
        }).setHeader("Actions").setClassNameGenerator(entry -> "teacher-grades-view-actions-column");

        // Add Grade Button with a dialog
        Button addGradeButton = new Button("Add Grade");
        addGradeButton.addClassName("teacher-grades-view-add-button");
        addGradeButton.addClickListener(event -> openAddGradeDialog());

        // Adding components to the view
        getContent().add(title, searchField, gradesGrid, addGradeButton);
    }

    // Mock GradeEntry class (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
    public static class GradeEntry {
        private String course;
        private String student;
        private String grade;

        public GradeEntry(String course, String student, String grade) {
            this.course = course;
            this.student = student;
            this.grade = grade;
        }

        public String getCourse() {
            return course;
        }

        public String getStudent() {
            return student;
        }

        public String getGrade() {
            return grade;
        }
    }

    // Method to filter grades based on the search term
    private void filterGrades(String searchTerm) {
        List<GradeEntry> filteredGrades = gradeEntries.stream()
                .filter(entry -> entry.getCourse().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        entry.getStudent().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        // Update the grid with the filtered data
        gradesGrid.setItems(filteredGrades);
    }

    // Method to open the Edit Grade dialog
    private void openEditGradeDialog(GradeEntry entry) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-grades-view-edit-dialog");

        // Text field for editing grade
        TextField gradeField = new TextField("Grade");
        gradeField.setValue(entry.getGrade());
        gradeField.addClassName("teacher-grades-view-grade-field");

        Button saveButton = new Button("Save", event -> {
            Notification.show("Grade updated successfully.");
            editDialog.close();
        });
        saveButton.addClassName("teacher-grades-view-save-button");

        Button cancelButton = new Button("Cancel", event -> editDialog.close());
        cancelButton.addClassName("teacher-grades-view-cancel-edit-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(saveButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-dialog-buttons");

        editDialog.add(new H2("Edit Grade"), gradeField, dialogButtons);
        editDialog.open();
    }

    // Method to open the Add Grade dialog
    private void openAddGradeDialog() {
        Dialog addDialog = new Dialog();
        addDialog.addClassName("teacher-grades-view-add-dialog");

        // Text fields for adding a new grade
        TextField courseField = new TextField("Course");
        courseField.addClassName("teacher-grades-view-course-field");

        TextField studentField = new TextField("Student");
        studentField.addClassName("teacher-grades-view-student-field");

        TextField gradeField = new TextField("Grade");
        gradeField.addClassName("teacher-grades-view-grade-field");

        Button addButton = new Button("Add", event -> {
            Notification.show("New grade added successfully.");
            addDialog.close();
            // Logic to add the grade to the grid/backend
        });
        addButton.addClassName("teacher-grades-view-add-button");

        Button cancelButton = new Button("Cancel", event -> addDialog.close());
        cancelButton.addClassName("teacher-grades-view-cancel-add-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(addButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-dialog-buttons");

        addDialog.add(new H2("Add Grade"), courseField, studentField, gradeField, dialogButtons);
        addDialog.open();
    }

    // Method to open the Delete confirmation dialog
    private void openDeleteGradeConfirmationDialog(GradeEntry entry) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("teacher-grades-view-delete-dialog");

        confirmationDialog.add(new H2("Confirm Grade Deletion"));

        Button confirmButton = new Button("Confirm", event -> {
            Notification.show("Grade deleted successfully.");
            confirmationDialog.close();
        });
        confirmButton.addClassName("teacher-grades-view-confirm-delete-button");

        Button cancelButton = new Button("Cancel", event -> confirmationDialog.close());
        cancelButton.addClassName("teacher-grades-view-cancel-button");

        HorizontalLayout dialogButtons = new HorizontalLayout(confirmButton, cancelButton);
        dialogButtons.addClassName("teacher-grades-view-dialog-buttons");

        confirmationDialog.add(dialogButtons);
        confirmationDialog.open();
    }
}

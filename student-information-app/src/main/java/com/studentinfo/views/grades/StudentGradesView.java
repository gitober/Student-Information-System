package com.studentinfo.views.grades;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CssImport("./themes/studentinformationapp/views/grades-view/student-grades-view.css")
public class StudentGradesView extends Composite<VerticalLayout> {

    private List<Course> mockCoursesData;
    private Grid<Course> coursesGrid;

    public StudentGradesView() {
        getContent().addClassName("student-grades-view-container");

        // Page title
        H2 title = new H2("Grades Overview");
        title.addClassName("student-grades-view-title");

        // Search bar to filter courses
        TextField searchField = new TextField("Search Courses");
        searchField.addClassName("student-grades-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        // Grid for displaying courses
        coursesGrid = new Grid<>();
        coursesGrid.addColumn(Course::getCourseName).setHeader("Course").setClassNameGenerator(course -> "student-grades-view-course-column");
        coursesGrid.addColumn(Course::getGrade).setHeader("Grade").setClassNameGenerator(course -> "student-grades-view-grade-column");
        coursesGrid.addColumn(Course::getDate).setHeader("Date").setClassNameGenerator(course -> "student-grades-view-date-column");
        coursesGrid.addClassName("student-grades-view-grid");

        // Mock data for courses (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
        mockCoursesData = Arrays.asList(
                new Course("Math 101", "A", "2024-01-15"),
                new Course("Physics 101", "B", "2024-01-18"),
                new Course("Chemistry 101", "A-", "2024-02-01")
        );
        coursesGrid.setItems(mockCoursesData);

        // Add components to the layout
        getContent().add(title, searchField, coursesGrid);
    }

    // Method to filter courses based on the search term
    private void filterCourses(String searchTerm) {
        List<Course> filteredCourses = mockCoursesData.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());

        coursesGrid.setItems(filteredCourses);
    }

    // Inner class for Course (POISTA MOCK DATA MYÖHEMMIN KUN LISÄTÄÄN BACKEND)
    public static class Course {
        private String courseName;
        private String grade;
        private String date;

        public Course(String courseName, String grade, String date) {
            this.courseName = courseName;
            this.grade = grade;
            this.date = date;
        }

        public String getCourseName() {
            return courseName;
        }

        public String getGrade() {
            return grade;
        }

        public String getDate() {
            return date;
        }
    }
}

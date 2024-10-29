package com.studentinfo.views.courses;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.CourseTranslation;
import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.TeacherService;
import com.studentinfo.services.DateService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/courses-view/teacher-courses-view.css")
public class TeacherCoursesView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final DateService dateService;
    private List<Course> courses;
    private final Grid<Course> coursesGrid;

    @Autowired
    public TeacherCoursesView(CourseService courseService, TeacherService teacherService, DateService dateService, MessageSource messageSource) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.dateService = dateService;

        getContent().addClassName("teacher-courses-view-container");

        H2 title = new H2(messageSource.getMessage("courses.title", null, LocaleContextHolder.getLocale()));
        title.addClassName("teacher-courses-view-title");

        Paragraph description = new Paragraph(messageSource.getMessage("courses.description", null, LocaleContextHolder.getLocale()));
        description.addClassName("teacher-courses-view-description");

        TextField searchField = new TextField(messageSource.getMessage("courses.searchPlaceholder", null, LocaleContextHolder.getLocale()));
        searchField.addClassName("teacher-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        coursesGrid = new Grid<>(Course.class, false);
        coursesGrid.addClassName("teacher-courses-view-grid");
        coursesGrid.addColumn(Course::getCourseName).setHeader(messageSource.getMessage("courses.courseName", null, LocaleContextHolder.getLocale()));
        coursesGrid.addColumn(Course::getCoursePlan).setHeader(messageSource.getMessage("courses.coursePlan", null, LocaleContextHolder.getLocale()));

        // Use dateService to format date range based on locale
        coursesGrid.addColumn(course -> dateService.formatDateRange(LocalDate.now(), LocalDate.now().plusDays(course.getDuration())))
                .setHeader(messageSource.getMessage("courses.dateRange", null, LocaleContextHolder.getLocale()));

        coursesGrid.addComponentColumn(course -> {
            Button editButton = new Button(messageSource.getMessage("courses.edit", null, LocaleContextHolder.getLocale()));
            editButton.addClassName("teacher-courses-view-edit-button");
            editButton.addClickListener(event -> openEditDialog(course, messageSource));

            Button viewDetailsButton = new Button(messageSource.getMessage("courses.viewDetails", null, LocaleContextHolder.getLocale()));
            viewDetailsButton.addClassName("teacher-courses-view-details-button");
            viewDetailsButton.addClickListener(event -> openViewDetailsDialog(course, messageSource));

            Button deleteButton = new Button(messageSource.getMessage("courses.delete", null, LocaleContextHolder.getLocale()));
            deleteButton.addClassName("teacher-courses-view-delete-button");
            deleteButton.addClickListener(event -> openDeleteConfirmationDialog(course, messageSource));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, viewDetailsButton, deleteButton);
            actionButtons.addClassName("teacher-courses-view-action-buttons");
            return actionButtons;
        }).setHeader(messageSource.getMessage("courses.actions", null, LocaleContextHolder.getLocale()));

        Button addCourseButton = new Button(messageSource.getMessage("courses.addCourse", null, LocaleContextHolder.getLocale()));
        addCourseButton.addClassName("teacher-courses-view-add-button");
        addCourseButton.addClickListener(event -> openAddCourseDialog(messageSource));

        getContent().add(title, description, searchField, coursesGrid, addCourseButton);

        refreshCourseData();
    }

    private void refreshCourseData() {
        courses = courseService.getAllCourses();
        if (courses != null && !courses.isEmpty()) {
            courses.sort((c1, c2) -> Long.compare(c2.getCourseId(), c1.getCourseId()));
            coursesGrid.setItems(courses);
        } else {
            coursesGrid.setItems(List.of());
        }
        coursesGrid.getDataProvider().refreshAll();
    }

    private void filterCourses(String searchTerm) {
        List<Course> filteredCourses = courses.stream()
                .filter(course -> course.getCourseName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        course.getCoursePlan().toLowerCase().contains(searchTerm.toLowerCase()))
                .collect(Collectors.toList());
        coursesGrid.setItems(filteredCourses);
    }

    private void openEditDialog(Course course, MessageSource messageSource) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-courses-view-edit-dialog");

        TextField courseNameField = new TextField(messageSource.getMessage("courses.courseName", null, LocaleContextHolder.getLocale()));
        courseNameField.setValue(course.getCourseName());

        TextField coursePlanField = new TextField(messageSource.getMessage("courses.coursePlan", null, LocaleContextHolder.getLocale()));
        coursePlanField.setValue(course.getCoursePlan());

        DatePicker startDatePicker = new DatePicker(messageSource.getMessage("courses.startDate", null, LocaleContextHolder.getLocale()));
        startDatePicker.setValue(LocalDate.now());

        DatePicker endDatePicker = new DatePicker(messageSource.getMessage("courses.endDate", null, LocaleContextHolder.getLocale()));
        endDatePicker.setValue(startDatePicker.getValue().plusDays(course.getDuration()));

        ComboBox<Teacher> teacherComboBox = new ComboBox<>(messageSource.getMessage("courses.selectTeacher", null, LocaleContextHolder.getLocale()));
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);
        teacherComboBox.setValue(course.getTeachers().isEmpty() ? null : course.getTeachers().get(0));

        Button saveButton = new Button(messageSource.getMessage("courses.save", null, LocaleContextHolder.getLocale()));
        saveButton.addClickListener(event -> {
            course.setCourseName(courseNameField.getValue());
            course.setCoursePlan(coursePlanField.getValue());
            long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(startDatePicker.getValue(), endDatePicker.getValue());
            course.setDuration((int) durationInDays);
            course.setTeachers(List.of(teacherComboBox.getValue()));

            String selectedLocale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
            List<CourseTranslation> translations = createCourseTranslations(course, selectedLocale);

            Course savedCourse = courseService.saveCourse(course, course.getTeachers(), translations);
            if (savedCourse != null) {
                refreshCourseData();
                Notification.show(messageSource.getMessage("courses.updateSuccess", null, LocaleContextHolder.getLocale()));
            } else {
                Notification.show(messageSource.getMessage("courses.updateError", null, LocaleContextHolder.getLocale()));
            }
            editDialog.close();
        });

        Button cancelButton = new Button(messageSource.getMessage("courses.cancel", null, LocaleContextHolder.getLocale()));
        cancelButton.addClickListener(event -> editDialog.close());

        editDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, teacherComboBox, new HorizontalLayout(saveButton, cancelButton));
        editDialog.open();
    }

    private void openAddCourseDialog(MessageSource messageSource) {
        Dialog addCourseDialog = new Dialog();
        addCourseDialog.addClassName("teacher-courses-view-add-dialog");

        TextField courseNameField = new TextField(messageSource.getMessage("courses.courseName", null, LocaleContextHolder.getLocale()));
        TextField coursePlanField = new TextField(messageSource.getMessage("courses.coursePlan", null, LocaleContextHolder.getLocale()));
        DatePicker startDatePicker = new DatePicker(messageSource.getMessage("courses.startDate", null, LocaleContextHolder.getLocale()));
        startDatePicker.setValue(LocalDate.now());

        DatePicker endDatePicker = new DatePicker(messageSource.getMessage("courses.endDate", null, LocaleContextHolder.getLocale()));
        endDatePicker.setValue(startDatePicker.getValue().plusDays(30));

        ComboBox<Teacher> teacherComboBox = new ComboBox<>(messageSource.getMessage("courses.selectTeacher", null, LocaleContextHolder.getLocale()));
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);

        Button saveButton = new Button(messageSource.getMessage("courses.save", null, LocaleContextHolder.getLocale()));
        saveButton.addClickListener(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
                Notification.show(messageSource.getMessage("courses.invalidDateRange", null, LocaleContextHolder.getLocale()));
                return;
            }

            Teacher selectedTeacher = teacherComboBox.getValue();
            if (selectedTeacher == null) {
                Notification.show(messageSource.getMessage("courses.selectTeacherPrompt", null, LocaleContextHolder.getLocale()));
                return;
            }

            long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            Course newCourse = new Course(courseNameField.getValue(), coursePlanField.getValue(), (int) durationInDays);
            String selectedLocale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
            List<CourseTranslation> translations = createCourseTranslations(newCourse, selectedLocale);

            try {
                courseService.saveCourse(newCourse, List.of(selectedTeacher), translations);
                refreshCourseData();
                Notification.show(messageSource.getMessage("courses.addSuccess", null, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                Notification.show(messageSource.getMessage("courses.addError", null, LocaleContextHolder.getLocale()));
            }

            addCourseDialog.close();
        });

        Button cancelButton = new Button(messageSource.getMessage("courses.cancel", null, LocaleContextHolder.getLocale()));
        cancelButton.addClickListener(event -> addCourseDialog.close());

        addCourseDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, teacherComboBox, new HorizontalLayout(saveButton, cancelButton));
        addCourseDialog.open();
    }

    private List<CourseTranslation> createCourseTranslations(Course course, String selectedLocale) {
        List<CourseTranslation> translations = new ArrayList<>();
        String translatedValue = course.getCourseName();
        translations.add(new CourseTranslation(selectedLocale, "course_name", translatedValue));
        return translations;
    }

    private void openViewDetailsDialog(Course course, MessageSource messageSource) {
        Dialog detailsDialog = new Dialog();
        detailsDialog.addClassName("teacher-courses-view-details-dialog");

        TextField courseNameField = new TextField(messageSource.getMessage("courses.courseName", null, LocaleContextHolder.getLocale()));
        courseNameField.setValue(course.getCourseName());
        courseNameField.setReadOnly(true);

        TextField coursePlanField = new TextField(messageSource.getMessage("courses.coursePlan", null, LocaleContextHolder.getLocale()));
        coursePlanField.setValue(course.getCoursePlan());
        coursePlanField.setReadOnly(true);

        DatePicker startDatePicker = new DatePicker(messageSource.getMessage("courses.startDate", null, LocaleContextHolder.getLocale()));
        startDatePicker.setValue(LocalDate.now());
        startDatePicker.setReadOnly(true);

        DatePicker endDatePicker = new DatePicker(messageSource.getMessage("courses.endDate", null, LocaleContextHolder.getLocale()));
        endDatePicker.setValue(LocalDate.now().plusDays(course.getDuration()));
        endDatePicker.setReadOnly(true);

        Grid<Student> studentGrid = new Grid<>(Student.class, false);
        studentGrid.addColumn(Student::getFirstName).setHeader(messageSource.getMessage("courses.studentFirstName", null, LocaleContextHolder.getLocale()));
        studentGrid.addColumn(Student::getLastName).setHeader(messageSource.getMessage("courses.studentLastName", null, LocaleContextHolder.getLocale()));
        studentGrid.addColumn(Student::getEmail).setHeader(messageSource.getMessage("courses.studentEmail", null, LocaleContextHolder.getLocale()));
        studentGrid.addColumn(Student::getPhoneNumber).setHeader(messageSource.getMessage("courses.studentPhoneNumber", null, LocaleContextHolder.getLocale()));
        studentGrid.setItems(courseService.getEnrolledStudentsByCourseId(course.getCourseId()));

        Button closeButton = new Button(messageSource.getMessage("courses.close", null, LocaleContextHolder.getLocale()));
        closeButton.addClickListener(event -> detailsDialog.close());

        detailsDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, studentGrid, closeButton);
        detailsDialog.open();
    }

    private void openDeleteConfirmationDialog(Course course, MessageSource messageSource) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("teacher-courses-view-delete-dialog");

        confirmationDialog.add(new H2(messageSource.getMessage("courses.confirmDeletion", null, LocaleContextHolder.getLocale())));

        Button confirmButton = new Button(messageSource.getMessage("courses.confirm", null, LocaleContextHolder.getLocale()));
        confirmButton.addClickListener(event -> {
            try {
                courseService.deleteCourse(course.getCourseId());
                refreshCourseData();
                Notification.show(messageSource.getMessage("courses.deleteSuccess", null, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                Notification.show(messageSource.getMessage("courses.deleteError", null, LocaleContextHolder.getLocale()));
            }
            confirmationDialog.close();
        });

        Button cancelButton = new Button(messageSource.getMessage("courses.cancel", null, LocaleContextHolder.getLocale()));
        cancelButton.addClickListener(event -> confirmationDialog.close());

        confirmationDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmationDialog.open();
    }

}

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/courses-view/teacher-courses-view.css")
public class TeacherCoursesView extends Composite<VerticalLayout> {

    private final transient CourseService courseService;
    private final transient TeacherService teacherService;
    private transient List<Course> courses;
    private final Grid<Course> coursesGrid;

    private static final String TITLE_KEY = "courses.title";
    private static final String DESCRIPTION_KEY = "courses.description";
    private static final String SEARCH_PLACEHOLDER_KEY = "courses.searchPlaceholder";
    private static final String COURSE_NAME_KEY = "courses.courseName";
    private static final String COURSE_PLAN_KEY = "courses.coursePlan";
    private static final String DATE_RANGE_KEY = "courses.dateRange";
    private static final String EDIT_KEY = "courses.edit";
    private static final String VIEW_DETAILS_KEY = "courses.viewDetails";
    private static final String DELETE_KEY = "courses.delete";
    private static final String ACTIONS_KEY = "courses.actions";
    private static final String ADD_COURSE_KEY = "courses.addCourse";
    private static final String SAVE_KEY = "courses.save";
    private static final String CANCEL_KEY = "courses.cancel";
    private static final String START_DATE_KEY = "courses.startDate";
    private static final String END_DATE_KEY = "courses.endDate";
    private static final String SELECT_TEACHER_KEY = "courses.selectTeacher";
    private static final String UPDATE_SUCCESS_KEY = "courses.updateSuccess";
    private static final String UPDATE_ERROR_KEY = "courses.updateError";
    private static final String ADD_SUCCESS_KEY = "courses.addSuccess";
    private static final String ADD_ERROR_KEY = "courses.addError";
    private static final String CONFIRM_DELETION_KEY = "courses.confirmDeletion";
    private static final String CONFIRM_KEY = "courses.confirm";
    private static final String DELETE_SUCCESS_KEY = "courses.deleteSuccess";
    private static final String DELETE_ERROR_KEY = "courses.deleteError";
    private static final String CLOSE_KEY = "courses.close";
    private static final String INVALID_DATE_RANGE_KEY = "courses.invalidDateRange";
    private static final String SELECT_TEACHER_PROMPT_KEY = "courses.selectTeacherPrompt";
    private static final String STUDENT_FIRST_NAME_KEY = "courses.studentFirstName";
    private static final String STUDENT_LAST_NAME_KEY = "courses.studentLastName";
    private static final String STUDENT_EMAIL_KEY = "courses.studentEmail";
    private static final String STUDENT_PHONE_KEY = "courses.studentPhoneNumber";

    @Autowired
    public TeacherCoursesView(CourseService courseService,
                              TeacherService teacherService,
                              MessageSource messageSource,
                              DateService dateService) {
        this.courseService = courseService;
        this.teacherService = teacherService;

        getContent().addClassName("teacher-courses-view-container");

        H2 title = new H2(getMessage(TITLE_KEY, messageSource));
        title.addClassName("teacher-courses-view-title");

        Paragraph description = new Paragraph(getMessage(DESCRIPTION_KEY, messageSource));
        description.addClassName("teacher-courses-view-description");

        TextField searchField = new TextField(getMessage(SEARCH_PLACEHOLDER_KEY, messageSource));
        searchField.addClassName("teacher-courses-view-search");
        searchField.setValueChangeMode(ValueChangeMode.EAGER);
        searchField.addValueChangeListener(event -> filterCourses(event.getValue()));

        coursesGrid = new Grid<>(Course.class, false);
        coursesGrid.addClassName("teacher-courses-view-grid");
        coursesGrid.addColumn(Course::getCourseName).setHeader(getMessage(COURSE_NAME_KEY, messageSource));
        coursesGrid.addColumn(Course::getCoursePlan).setHeader(getMessage(COURSE_PLAN_KEY, messageSource));
        coursesGrid.addColumn(course -> formatDateRange(LocalDate.now(), LocalDate.now().plusDays(course.getDuration())))
                .setHeader(getMessage(DATE_RANGE_KEY, messageSource));

        coursesGrid.addComponentColumn(course -> {
            Button editButton = new Button(getMessage(EDIT_KEY, messageSource));
            editButton.addClassName("teacher-courses-view-edit-button");
            editButton.addClickListener(event -> openEditDialog(course, messageSource));

            Button viewDetailsButton = new Button(getMessage(VIEW_DETAILS_KEY, messageSource));
            viewDetailsButton.addClassName("teacher-courses-view-details-button");
            viewDetailsButton.addClickListener(event -> openViewDetailsDialog(course, messageSource));

            Button deleteButton = new Button(getMessage(DELETE_KEY, messageSource));
            deleteButton.addClassName("teacher-courses-view-delete-button");
            deleteButton.addClickListener(event -> openDeleteConfirmationDialog(course, messageSource));

            HorizontalLayout actionButtons = new HorizontalLayout(editButton, viewDetailsButton, deleteButton);
            actionButtons.addClassName("teacher-courses-view-action-buttons");
            return actionButtons;
        }).setHeader(getMessage(ACTIONS_KEY, messageSource));

        Button addCourseButton = new Button(getMessage(ADD_COURSE_KEY, messageSource));
        addCourseButton.addClassName("teacher-courses-view-add-button");
        addCourseButton.addClickListener(event -> openAddCourseDialog(messageSource));

        getContent().add(title, description, searchField, coursesGrid, addCourseButton);

        refreshCourseData();
    }


    private void setDatePickerLocale(DatePicker datePicker) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        datePicker.setLocale(currentLocale);
        datePicker.setI18n(new DatePicker.DatePickerI18n().setDateFormat(currentLocale.getLanguage().equals("ch") ? "yyyy-MM-dd" : "dd/MM/yyyy"));
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
                .toList(); // Using toList() directly

        coursesGrid.setItems(filteredCourses);
    }

    private void openEditDialog(Course course, MessageSource messageSource) {
        Dialog editDialog = new Dialog();
        editDialog.addClassName("teacher-courses-view-edit-dialog");

        TextField courseNameField = new TextField(getMessage(COURSE_NAME_KEY, messageSource));
        courseNameField.addClassName("teacher-courses-view-course-name-field");
        courseNameField.setValue(course.getCourseName());

        TextField coursePlanField = new TextField(getMessage(COURSE_PLAN_KEY, messageSource));
        coursePlanField.addClassName("teacher-courses-view-plan-field");
        coursePlanField.setValue(course.getCoursePlan());

        DatePicker startDatePicker = new DatePicker(getMessage(START_DATE_KEY, messageSource));
        startDatePicker.addClassName("teacher-courses-view-start-date-picker");
        setDatePickerLocale(startDatePicker);
        startDatePicker.setValue(LocalDate.now());

        DatePicker endDatePicker = new DatePicker(getMessage(END_DATE_KEY, messageSource));
        endDatePicker.addClassName("teacher-courses-view-end-date-picker");
        setDatePickerLocale(endDatePicker);
        endDatePicker.setValue(startDatePicker.getValue().plusDays(course.getDuration()));

        ComboBox<Teacher> teacherComboBox = new ComboBox<>(getMessage(SELECT_TEACHER_KEY, messageSource));
        teacherComboBox.addClassName("teacher-courses-view-teacher-combobox");
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);
        teacherComboBox.setValue(course.getTeachers().isEmpty() ? null : course.getTeachers().get(0));

        Button saveButton = new Button(getMessage(SAVE_KEY, messageSource));
        saveButton.addClassName("teacher-courses-view-save-button");
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
                Notification.show(getMessage(UPDATE_SUCCESS_KEY, messageSource));
            } else {
                Notification.show(getMessage(UPDATE_ERROR_KEY, messageSource));
            }
            editDialog.close();
        });

        Button cancelButton = new Button(getMessage(CANCEL_KEY, messageSource));
        cancelButton.addClassName("teacher-courses-view-cancel-button");
        cancelButton.addClickListener(event -> editDialog.close());

        editDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, teacherComboBox, new HorizontalLayout(saveButton, cancelButton));
        editDialog.open();
    }

    private void openAddCourseDialog(MessageSource messageSource) {
        Dialog addCourseDialog = new Dialog();
        addCourseDialog.addClassName("teacher-courses-view-add-dialog");

        TextField courseNameField = new TextField(getMessage(COURSE_NAME_KEY, messageSource));
        courseNameField.addClassName("teacher-courses-view-add-course-name");

        TextField coursePlanField = new TextField(getMessage(COURSE_PLAN_KEY, messageSource));
        coursePlanField.addClassName("teacher-courses-view-add-course-plan");

        DatePicker startDatePicker = new DatePicker(getMessage(START_DATE_KEY, messageSource));
        startDatePicker.addClassName("teacher-courses-view-add-start-date");
        setDatePickerLocale(startDatePicker);
        startDatePicker.setValue(LocalDate.now());

        DatePicker endDatePicker = new DatePicker(getMessage(END_DATE_KEY, messageSource));
        endDatePicker.addClassName("teacher-courses-view-add-end-date");
        setDatePickerLocale(endDatePicker);
        endDatePicker.setValue(startDatePicker.getValue().plusDays(30));

        ComboBox<Teacher> teacherComboBox = new ComboBox<>(getMessage(SELECT_TEACHER_KEY, messageSource));
        teacherComboBox.addClassName("teacher-courses-view-add-teacher-combobox");
        teacherComboBox.setItems(teacherService.getAllTeachers());
        teacherComboBox.setItemLabelGenerator(Teacher::getFullName);

        Button saveButton = new Button(getMessage(SAVE_KEY, messageSource));
        saveButton.addClassName("teacher-courses-view-add-save-button");
        saveButton.addClickListener(event -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
                Notification.show(getMessage(INVALID_DATE_RANGE_KEY, messageSource));
                return;
            }

            Teacher selectedTeacher = teacherComboBox.getValue();
            if (selectedTeacher == null) {
                Notification.show(getMessage(SELECT_TEACHER_PROMPT_KEY, messageSource));
                return;
            }

            long durationInDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
            Course newCourse = new Course(courseNameField.getValue(), coursePlanField.getValue(), (int) durationInDays);

            String selectedLocale = LocaleContextHolder.getLocale().getLanguage().toUpperCase();
            List<CourseTranslation> translations = createCourseTranslations(newCourse, selectedLocale);

            try {
                courseService.saveCourse(newCourse, List.of(selectedTeacher), translations);
                refreshCourseData();
                Notification.show(getMessage(ADD_SUCCESS_KEY, messageSource));
            } catch (Exception e) {
                Notification.show(getMessage(ADD_ERROR_KEY, messageSource));
            }

            addCourseDialog.close();
        });

        Button cancelButton = new Button(getMessage(CANCEL_KEY, messageSource));
        cancelButton.addClassName("teacher-courses-view-add-cancel-button");
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

        TextField courseNameField = new TextField(getMessage(COURSE_NAME_KEY, messageSource));
        courseNameField.addClassName("teacher-courses-view-course-name-field");
        courseNameField.setValue(course.getCourseName());
        courseNameField.setReadOnly(true);

        TextField coursePlanField = new TextField(getMessage(COURSE_PLAN_KEY, messageSource));
        coursePlanField.addClassName("teacher-courses-view-plan-field");
        coursePlanField.setValue(course.getCoursePlan());
        coursePlanField.setReadOnly(true);

        DatePicker startDatePicker = new DatePicker(getMessage(START_DATE_KEY, messageSource));
        startDatePicker.addClassName("teacher-courses-view-start-date-picker");
        setDatePickerLocale(startDatePicker);
        startDatePicker.setValue(LocalDate.now()); // Adjust to actual start date if available
        startDatePicker.setReadOnly(true);

        DatePicker endDatePicker = new DatePicker(getMessage(END_DATE_KEY, messageSource));
        endDatePicker.addClassName("teacher-courses-view-end-date-picker");
        setDatePickerLocale(endDatePicker);
        endDatePicker.setValue(LocalDate.now().plusDays(course.getDuration())); // Adjust to actual end date if available
        endDatePicker.setReadOnly(true);

        Grid<Student> studentGrid = new Grid<>(Student.class, false);
        studentGrid.addClassName("teacher-courses-view-student-grid");
        studentGrid.addColumn(Student::getFirstName).setHeader(getMessage(STUDENT_FIRST_NAME_KEY, messageSource));
        studentGrid.addColumn(Student::getLastName).setHeader(getMessage(STUDENT_LAST_NAME_KEY, messageSource));
        studentGrid.addColumn(Student::getEmail).setHeader(getMessage(STUDENT_EMAIL_KEY, messageSource));
        studentGrid.addColumn(Student::getPhoneNumber).setHeader(getMessage(STUDENT_PHONE_KEY, messageSource));
        studentGrid.setItems(courseService.getEnrolledStudentsByCourseId(course.getCourseId()));

        Button closeButton = new Button(getMessage(CLOSE_KEY, messageSource));
        closeButton.addClassName("teacher-courses-view-close-button");
        closeButton.addClickListener(event -> detailsDialog.close());

        detailsDialog.add(courseNameField, coursePlanField, startDatePicker, endDatePicker, studentGrid, closeButton);
        detailsDialog.open();
    }


    private void openDeleteConfirmationDialog(Course course, MessageSource messageSource) {
        Dialog confirmationDialog = new Dialog();
        confirmationDialog.addClassName("teacher-courses-view-delete-dialog");

        H2 confirmationTitle = new H2(getMessage(CONFIRM_DELETION_KEY, messageSource));
        confirmationTitle.addClassName("teacher-courses-view-delete-title");
        confirmationDialog.add(confirmationTitle);

        Button confirmButton = new Button(getMessage(CONFIRM_KEY, messageSource));
        confirmButton.addClassName("teacher-courses-view-confirm-button");
        confirmButton.addClickListener(event -> {
            try {
                courseService.deleteCourse(course.getCourseId());
                refreshCourseData();
                Notification.show(getMessage(DELETE_SUCCESS_KEY, messageSource));
            } catch (Exception e) {
                Notification.show(getMessage(DELETE_ERROR_KEY, messageSource));
            }
            confirmationDialog.close();
        });

        Button cancelButton = new Button(getMessage(CANCEL_KEY, messageSource));
     cancelButton.addClickListener(event -> confirmationDialog.close());

        HorizontalLayout buttonLayout = new HorizontalLayout(confirmButton, cancelButton);
        buttonLayout.addClassName("teacher-courses-view-dialog-buttons");
        confirmationDialog.add(buttonLayout);
        cancelButton.addClassName("teacher-courses-view-cancel-delete-button");

        confirmationDialog.open();
    }

    // Helper method to fetch message from MessageSource with a given key
    private String getMessage(String key, MessageSource messageSource) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }


    public String formatDateRange(LocalDate startDate, LocalDate endDate) {
        Locale locale = LocaleContextHolder.getLocale();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                locale.getLanguage().equals("ch") ? "yyyy年MM月dd日" : "dd/MM/yyyy",
                locale
        );
        return startDate.format(formatter) + " - " + endDate.format(formatter);
    }

}

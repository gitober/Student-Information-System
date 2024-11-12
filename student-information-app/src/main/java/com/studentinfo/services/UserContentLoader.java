package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.teacher_attendance_view.TeacherAttendanceView;
import com.studentinfo.views.courses.StudentCoursesView;
import com.studentinfo.views.courses.TeacherCoursesView;
import com.studentinfo.views.homeprofilepage.StudentDashboardView;
import com.studentinfo.views.homeprofilepage.TeacherDashboardView;
import com.studentinfo.views.grades.StudentGradesView;
import com.studentinfo.views.grades.TeacherGradesView;
import com.studentinfo.views.editprofile.StudentEditProfileView;
import com.studentinfo.views.editprofile.TeacherEditProfileView;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class UserContentLoader {

    private static final Logger logger = LoggerFactory.getLogger(UserContentLoader.class);

    // Dependencies
    private final AuthenticatedUser authenticatedUser;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final SubjectService subjectService;
    private final UserService userService;
    private final DateService dateService;
    private final MessageSource messageSource;

    // Views
    private final TeacherCoursesView teacherCoursesView;
    private final StudentCoursesView studentCoursesView;
    private final TeacherGradesView teacherGradesView;
    private final StudentGradesView studentGradesView;
    private final TeacherDashboardView teacherDashboardView;
    private final StudentDashboardView studentDashboardView;

    // Constants for messages
    private static final String ROLE_NOT_RECOGNIZED_MESSAGE = "Role not recognized. Please contact support.";
    private static final String USER_NOT_FOUND_MESSAGE = "User not found. Please log in again.";

    // Constructor for dependency injection
    @Autowired
    public UserContentLoader(
            AuthenticatedUser authenticatedUser,
            TeacherService teacherService,
            StudentService studentService,
            DepartmentService departmentService,
            SubjectService subjectService,
            UserService userService,
            DateService dateService,
            MessageSource messageSource,
            @Lazy TeacherCoursesView teacherCoursesView,
            @Lazy StudentCoursesView studentCoursesView,
            @Lazy TeacherGradesView teacherGradesView,
            @Lazy StudentGradesView studentGradesView,
            @Lazy TeacherDashboardView teacherDashboardView,
            @Lazy StudentDashboardView studentDashboardView,
            @Lazy TeacherAttendanceView teacherAttendanceView
    ) {
        this.authenticatedUser = authenticatedUser;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
        this.userService = userService;
        this.dateService = dateService;
        this.messageSource = messageSource;

        // Initialize views
        this.teacherCoursesView = teacherCoursesView;
        this.studentCoursesView = studentCoursesView;
        this.teacherGradesView = teacherGradesView;
        this.studentGradesView = studentGradesView;
        this.teacherDashboardView = teacherDashboardView;
        this.studentDashboardView = studentDashboardView;
    }

    // Methods to Load Content

    public void loadProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher && teacherDashboardView != null) {
                logger.info("Adding TeacherDashboardView to layout");
                layout.add(teacherDashboardView);
            } else if (user instanceof Student && studentDashboardView != null) {
                logger.info("Adding StudentDashboardView to layout");
                layout.add(studentDashboardView);
            } else {
                layout.add(new Paragraph(ROLE_NOT_RECOGNIZED_MESSAGE));
            }
        }, () -> layout.add(new Paragraph(USER_NOT_FOUND_MESSAGE)));
    }

    public void loadCoursesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                layout.add(teacherCoursesView);
            } else if (user instanceof Student) {
                layout.add(studentCoursesView);
            } else {
                layout.add(new Paragraph(ROLE_NOT_RECOGNIZED_MESSAGE));
            }
        }, () -> layout.add(new Paragraph(USER_NOT_FOUND_MESSAGE)));
    }

    public void loadGradesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                layout.add(teacherGradesView);
            } else if (user instanceof Student) {
                layout.add(studentGradesView);
            } else {
                layout.add(new Paragraph(ROLE_NOT_RECOGNIZED_MESSAGE));
            }
        }, () -> layout.add(new Paragraph(USER_NOT_FOUND_MESSAGE)));
    }

    public void loadEditProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            switch (user) {
                case Teacher teacher -> {
                    TeacherEditProfileView teacherView = new TeacherEditProfileView(teacher, departmentService, subjectService, dateService, messageSource);
                    teacherView.setSaveListener(teacherService::save);
                    layout.add(teacherView);
                }
                case Student student -> {
                    StudentEditProfileView studentView = new StudentEditProfileView(student, userService, messageSource);
                    studentView.setSaveListener(studentService::save);
                    layout.add(studentView);
                }
                default -> layout.add(new Paragraph(ROLE_NOT_RECOGNIZED_MESSAGE)); // Removed redundant braces
            }
        }, () -> layout.add(new Paragraph(USER_NOT_FOUND_MESSAGE)));
    }
}
package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.TeacherAttendanceView.TeacherAttendanceView;
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

@Component
public class UserContentLoader {

    // Dependencies
    private final AuthenticatedUser authenticatedUser;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final SubjectService subjectService;
    private final UserService userService;
    private final DateService dateService;
    private final MessageSource messageSource;

    // Lazy-Loaded Views
    @Autowired
    @Lazy
    private TeacherCoursesView teacherCoursesView;

    @Autowired
    @Lazy
    private StudentCoursesView studentCoursesView;

    @Autowired
    @Lazy
    private TeacherGradesView teacherGradesView;

    @Autowired
    @Lazy
    private StudentGradesView studentGradesView;

    @Autowired
    @Lazy
    private TeacherDashboardView teacherDashboardView;

    @Autowired
    @Lazy
    private StudentDashboardView studentDashboardView;

    @Autowired
    @Lazy
    private TeacherAttendanceView teacherAttendanceView;

    // Constructor for dependency injection
    @Autowired
    public UserContentLoader(AuthenticatedUser authenticatedUser, TeacherService teacherService,
                             StudentService studentService, DepartmentService departmentService,
                             SubjectService subjectService, UserService userService,
                             DateService dateService, MessageSource messageSource) {
        this.authenticatedUser = authenticatedUser;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
        this.userService = userService;
        this.dateService = dateService;
        this.messageSource = messageSource;
    }

    // Methods to Load Content

    // Load profile content based on user role
    public void loadProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher && teacherDashboardView != null) {
                layout.add(teacherDashboardView); // Use injected view
            } else if (user instanceof Student && studentDashboardView != null) {
                layout.add(studentDashboardView); // Use injected view
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> layout.add(new Paragraph("User not found. Please log in again.")));
    }

    // Load courses content based on user role
    public void loadCoursesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                layout.add(teacherCoursesView);
            } else if (user instanceof Student) {
                layout.add(studentCoursesView);
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> layout.add(new Paragraph("User not found. Please log in again.")));
    }

    // Load grades content based on user role
    public void loadGradesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                layout.add(teacherGradesView);
            } else if (user instanceof Student) {
                layout.add(studentGradesView);
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> layout.add(new Paragraph("User not found. Please log in again.")));
    }

    // Load edit profile content based on user role
    public void loadEditProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher teacher) {
                // Pass additional dependencies: dateService and messageSource
                TeacherEditProfileView teacherView = new TeacherEditProfileView(teacher, departmentService, subjectService, dateService, messageSource);
                teacherView.setSaveListener(teacherService::save);
                layout.add(teacherView);
            } else if (user instanceof Student student) {
                StudentEditProfileView studentView = new StudentEditProfileView(student, userService, messageSource); // Pass UserService and MessageSource
                studentView.setSaveListener(studentService::save);
                layout.add(studentView);
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> layout.add(new Paragraph("User not found. Please log in again.")));
    }

    // Load attendance content based on user role
    public void loadAttendanceContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                layout.add(teacherAttendanceView);
            } else {
                layout.add(new Paragraph("Role not recognized. Only teachers can access attendance tracking."));
            }
        }, () -> layout.add(new Paragraph("User not found. Please log in again.")));
    }
}

package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.StudentCoursesView;
import com.studentinfo.views.courses.TeacherCoursesView;
import com.studentinfo.views.profilepage.StudentDashboardView;
import com.studentinfo.views.profilepage.TeacherDashboardView;
import com.studentinfo.views.grades.StudentGradesView;
import com.studentinfo.views.grades.TeacherGradesView;
import com.studentinfo.views.editprofile.StudentEditProfileView;
import com.studentinfo.views.editprofile.TeacherEditProfileView;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserContentLoader {

    private final AuthenticatedUser authenticatedUser;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final DepartmentService departmentService;
    private final SubjectService subjectService;

    // Constructor injection for AuthenticatedUser, TeacherService, StudentService, DepartmentService, and SubjectService
    @Autowired
    public UserContentLoader(AuthenticatedUser authenticatedUser, TeacherService teacherService, StudentService studentService,
                             DepartmentService departmentService, SubjectService subjectService) {
        this.authenticatedUser = authenticatedUser;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
    }

    // Method to load profile content
    public void loadContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherDashboardView());
            } else if (user instanceof Student) {
                layout.add(new StudentDashboardView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }

    // Method to load courses content
    public void loadCoursesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherCoursesView());
            } else if (user instanceof Student) {
                layout.add(new StudentCoursesView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }

    // Method to load grades content
    public void loadGradesContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            // Check if the user is a Teacher or Student based on instance
            if (user instanceof Teacher) {
                layout.add(new TeacherGradesView());
            } else if (user instanceof Student) {
                layout.add(new StudentGradesView());
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }

    // Method to load edit profile content
    public void loadEditProfileContent(VerticalLayout layout) {
        authenticatedUser.get().ifPresentOrElse(user -> {
            if (user instanceof Teacher) {
                Teacher teacher = (Teacher) user;
                // Pass the Teacher, DepartmentService, and SubjectService to the view
                TeacherEditProfileView teacherView = new TeacherEditProfileView(teacher, departmentService, subjectService);
                teacherView.setSaveListener(updatedTeacher -> {
                    teacherService.save(updatedTeacher); // Save updated profile
                });
                layout.add(teacherView);
            } else if (user instanceof Student) {
                Student student = (Student) user;
                StudentEditProfileView studentView = new StudentEditProfileView(student);
                studentView.setSaveListener(updatedStudent -> {
                    studentService.save(updatedStudent); // Save updated profile
                });
                layout.add(studentView);
            } else {
                layout.add(new Paragraph("Role not recognized. Please contact support."));
            }
        }, () -> {
            layout.add(new Paragraph("User not found. Please log in again."));
        });
    }
}

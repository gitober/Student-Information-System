package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.data.entity.User;
import com.studentinfo.security.AuthenticatedUser;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserContentLoaderTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private TeacherService teacherService;

    @Mock
    private StudentService studentService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private SubjectService subjectService;

    @Mock
    private TeacherCoursesView teacherCoursesView;

    @Mock
    private StudentCoursesView studentCoursesView;

    @Mock
    private TeacherGradesView teacherGradesView;

    @Mock
    private StudentGradesView studentGradesView;

    @InjectMocks
    private UserContentLoader userContentLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Disabled
    void testLoadProfileContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadProfileContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertTrue(layout.getComponentAt(0) instanceof TeacherDashboardView);
    }

    @Test
    @Disabled
    void testLoadProfileContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadProfileContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertTrue(layout.getComponentAt(0) instanceof StudentDashboardView);
    }

    @Test
    void testLoadProfileContentForUnknownRole() {
        // Arrange
        User unknownUser = mock(User.class);
        when(authenticatedUser.get()).thenReturn(Optional.of(unknownUser));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadProfileContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertTrue(layout.getComponentAt(0) instanceof Paragraph);
    }

    @Test
    @Disabled
    void testLoadCoursesContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadCoursesContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertEquals(teacherCoursesView, layout.getComponentAt(0));
    }

    @Test
    @Disabled
    void testLoadCoursesContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadCoursesContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertEquals(studentCoursesView, layout.getComponentAt(0));
    }

    @Test
    @Disabled
    void testLoadGradesContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadGradesContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertEquals(teacherGradesView, layout.getComponentAt(0));
    }

    @Test
    @Disabled
    void testLoadGradesContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadGradesContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertEquals(studentGradesView, layout.getComponentAt(0));
    }

    @Test
    void testLoadEditProfileContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadEditProfileContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertTrue(layout.getComponentAt(0) instanceof TeacherEditProfileView);
    }

    @Test
    void testLoadEditProfileContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadEditProfileContent(layout);

        // Assert
        assertEquals(1, layout.getComponentCount());
        assertTrue(layout.getComponentAt(0) instanceof StudentEditProfileView);
    }
}
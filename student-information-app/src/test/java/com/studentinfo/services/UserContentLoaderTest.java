package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.StudentCoursesView;
import com.studentinfo.views.courses.TeacherCoursesView;
import com.studentinfo.views.homeprofilepage.StudentDashboardView;
import com.studentinfo.views.homeprofilepage.TeacherDashboardView;
import com.studentinfo.views.grades.StudentGradesView;
import com.studentinfo.views.grades.TeacherGradesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    private UserService userService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private TeacherCoursesView teacherCoursesView;

    @Mock
    private StudentCoursesView studentCoursesView;

    @Mock
    private TeacherGradesView teacherGradesView;

    @Mock
    private StudentGradesView studentGradesView;

    @Mock
    private TeacherDashboardView teacherDashboardView;

    @Mock
    private StudentDashboardView studentDashboardView;

    @InjectMocks
    private UserContentLoader userContentLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        reset(
                authenticatedUser,
                teacherService,
                studentService,
                departmentService,
                subjectService,
                userService,
                teacherCoursesView,
                studentCoursesView,
                teacherGradesView,
                studentGradesView,
                teacherDashboardView,
                studentDashboardView
        );
    }

    @Test
    void testLoadProfileContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadProfileContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertInstanceOf(TeacherDashboardView.class, addedComponent, "Expected TeacherDashboardView component for teacher");
    }

    @Test
    void testLoadProfileContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadProfileContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertInstanceOf(StudentDashboardView.class, addedComponent, "Expected StudentDashboardView component for student");
    }

    @Test
    void testLoadCoursesContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadCoursesContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertEquals(teacherCoursesView, addedComponent, "Expected TeacherCoursesView for teacher");
    }

    @Test
    void testLoadCoursesContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadCoursesContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertEquals(studentCoursesView, addedComponent, "Expected StudentCoursesView for student");
    }

    @Test
    void testLoadGradesContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadGradesContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertEquals(teacherGradesView, addedComponent, "Expected TeacherGradesView for teacher");
    }

    @Test
    void testLoadGradesContentForStudent() {
        // Arrange
        Student student = new Student();
        when(authenticatedUser.get()).thenReturn(Optional.of(student));
        VerticalLayout layout = mock(VerticalLayout.class);

        // Act
        userContentLoader.loadGradesContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertEquals(studentGradesView, addedComponent, "Expected StudentGradesView for student");
    }
}

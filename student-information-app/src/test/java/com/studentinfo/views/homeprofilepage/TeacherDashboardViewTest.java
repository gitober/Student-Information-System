package com.studentinfo.views.homeprofilepage;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.entity.Student;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.services.StudentService;
import com.studentinfo.views.teacher_attendance_view.TeacherAttendanceView;
import com.studentinfo.views.teacher_update_student_profile.TeacherUpdateStudentProfileView;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.Router;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.RouteRegistry;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TeacherDashboardViewTest {

    private TeacherDashboardView teacherDashboardView;

    @BeforeEach
    public void setUp() {
        // Mock services
        CourseService courseService = mock(CourseService.class);
        StudentService studentService = mock(StudentService.class);
        GradeService gradeService = mock(GradeService.class);
        MessageSource messageSource = mock(MessageSource.class);

        // Mock service methods
        when(studentService.list()).thenReturn(new ArrayList<>(List.of(new Student(), new Student())));
        when(gradeService.getAllGrades()).thenReturn(new ArrayList<>(List.of(new Grade(), new Grade(), new Grade())));
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>(List.of(new Course(), new Course())));

        // Mock translation messages
        when(messageSource.getMessage("teacher.dashboard.title", null, Locale.getDefault())).thenReturn("Teacher Dashboard");
        when(messageSource.getMessage("teacher.dashboard.students.title", null, Locale.getDefault())).thenReturn("Students");
        when(messageSource.getMessage("teacher.dashboard.students.count", null, Locale.getDefault())).thenReturn("Students Count");
        when(messageSource.getMessage("teacher.dashboard.grading.title", null, Locale.getDefault())).thenReturn("Grading");
        when(messageSource.getMessage("teacher.dashboard.grading.count", null, Locale.getDefault())).thenReturn("Grades Count");
        when(messageSource.getMessage("teacher.dashboard.courses.title", null, Locale.getDefault())).thenReturn("Manage Courses");
        when(messageSource.getMessage("teacher.dashboard.courses.count", null, Locale.getDefault())).thenReturn("Courses Count");
        when(messageSource.getMessage("teacher.dashboard.attendance.title", null, Locale.getDefault())).thenReturn("Manage Attendance");
        when(messageSource.getMessage("teacher.dashboard.attendance.description", null, Locale.getDefault())).thenReturn("Click to Manage");
        when(messageSource.getMessage("teacher.dashboard.links.title", null, Locale.getDefault())).thenReturn("Quick Links");
        when(messageSource.getMessage("teacher.dashboard.links.description", null, Locale.getDefault())).thenReturn("Access Tools");

        // Mock Vaadin environment
        UI ui = new UI();
        UI.setCurrent(ui);

        VaadinSession vaadinSessionMock = mock(VaadinSession.class);
        ui.getInternals().setSession(vaadinSessionMock);

        VaadinService vaadinServiceMock = mock(VaadinService.class);
        Router routerMock = mock(Router.class);
        RouteRegistry routeRegistryMock = mock(RouteRegistry.class);

        VaadinService.setCurrent(vaadinServiceMock);
        when(vaadinServiceMock.getRouter()).thenReturn(routerMock);
        when(routerMock.getRegistry()).thenReturn(routeRegistryMock);

        // Mock route URLs for RouterLinks
        when(routeRegistryMock.getTargetUrl(TeacherUpdateStudentProfileView.class, new RouteParameters()))
                .thenReturn(Optional.of("teacher-update-student-profile"));
        when(routeRegistryMock.getTargetUrl(GradesView.class, new RouteParameters()))
                .thenReturn(Optional.of("grades"));
        when(routeRegistryMock.getTargetUrl(CoursesView.class, new RouteParameters()))
                .thenReturn(Optional.of("courses"));
        when(routeRegistryMock.getTargetUrl(TeacherAttendanceView.class, new RouteParameters()))
                .thenReturn(Optional.of("attendance"));
        when(routeRegistryMock.getTargetUrl(EditProfileView.class, new RouteParameters()))
                .thenReturn(Optional.of("edit-profile"));

        // Instantiate TeacherDashboardView with mocked services
        teacherDashboardView = new TeacherDashboardView(courseService, studentService, gradeService, messageSource);
    }

    @AfterEach
    public void tearDown() {
        teacherDashboardView = null;
        UI.setCurrent(null);
        VaadinService.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    void testTeacherDashboardViewInitialization() {
        // Check if the main title is present
        H1 title = (H1) teacherDashboardView.getContent().getComponentAt(0);
        assertNotNull(title);
        assertEquals("Teacher Dashboard", title.getText());

        // Check if the dashboard grid is present
        FlexLayout dashboardGrid = (FlexLayout) teacherDashboardView.getContent().getComponentAt(1);
        assertNotNull(dashboardGrid);
        assertEquals(5, dashboardGrid.getComponentCount()); // Should have 5 dashboard cards

        // Check if the correct number of cards is displayed with expected titles
        checkCardTitle(dashboardGrid.getComponentAt(0), "Students");
        checkCardTitle(dashboardGrid.getComponentAt(1), "Grading");
        checkCardTitle(dashboardGrid.getComponentAt(2), "Manage Courses");
        checkCardTitle(dashboardGrid.getComponentAt(3), "Manage Attendance");
        checkCardTitle(dashboardGrid.getComponentAt(4), "Quick Links");
    }

    private void checkCardTitle(Component card, String expectedTitle) {
        if (card instanceof Div divCard) {
            RouterLink link = (RouterLink) divCard.getChildren()
                    .filter(component -> component instanceof RouterLink)
                    .findFirst()
                    .orElse(null);
            if (link != null) {
                Paragraph title = (Paragraph) link.getChildren()
                        .filter(component -> component instanceof Paragraph)
                        .findFirst()
                        .orElse(null);
                assertNotNull(title);
                assertEquals(expectedTitle, title.getText());
            } else {
                // Handle the case where there is no RouterLink
                Paragraph title = (Paragraph) divCard.getChildren()
                        .filter(component -> component instanceof Paragraph)
                        .findFirst()
                        .orElse(null);
                assertNotNull(title);
                assertEquals(expectedTitle, title.getText());
            }
        }
    }
}

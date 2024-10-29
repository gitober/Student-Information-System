package com.studentinfo.views.homeprofilepage;

import com.studentinfo.data.entity.Attendance;
import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Grade;
import com.studentinfo.data.entity.Student;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.vaadin.flow.component.Component;
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
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class StudentDashboardViewTest {

    private StudentDashboardView studentDashboardView;

    @BeforeEach
    public void setUp() {
        // Mock the services
        CourseService courseService = Mockito.mock(CourseService.class);
        GradeService gradeService = Mockito.mock(GradeService.class);
        AttendanceService attendanceService = Mockito.mock(AttendanceService.class);
        AuthenticatedUser authenticatedUser = Mockito.mock(AuthenticatedUser.class);

        // Mock message source
        MessageSource messageSource = createMessageSource();

        // Mock service methods
        when(courseService.getAllCourses()).thenReturn(new ArrayList<>(List.of(new Course(), new Course())));
        when(gradeService.getAllGrades()).thenReturn(new ArrayList<>(List.of(new Grade(), new Grade(), new Grade())));
        when(attendanceService.getAttendanceByCourseId(Mockito.anyLong())).thenReturn(new ArrayList<>(List.of(new Attendance(), new Attendance())));
        when(authenticatedUser.get()).thenReturn(Optional.of(new Student()));

        // Setting up a mocked Vaadin environment
        VaadinService vaadinServiceMock = Mockito.mock(VaadinService.class);
        VaadinSession vaadinSessionMock = Mockito.mock(VaadinSession.class);
        Router routerMock = Mockito.mock(Router.class);
        RouteRegistry routeRegistryMock = Mockito.mock(RouteRegistry.class);

        // Ensure VaadinService.getCurrent() returns the mocked VaadinService
        VaadinService.setCurrent(vaadinServiceMock);
        VaadinSession.setCurrent(vaadinSessionMock);

        // Ensure the router and route registry are set in the VaadinService
        Mockito.when(vaadinServiceMock.getRouter()).thenReturn(routerMock);
        Mockito.when(routerMock.getRegistry()).thenReturn(routeRegistryMock);

        // Register the routes in the mocked RouteRegistry
        Mockito.when(routeRegistryMock.getTargetUrl(CoursesView.class, new RouteParameters()))
                .thenReturn(Optional.of("courses"));
        Mockito.when(routeRegistryMock.getTargetUrl(GradesView.class, new RouteParameters()))
                .thenReturn(Optional.of("grades"));
        Mockito.when(routeRegistryMock.getTargetUrl(EditProfileView.class, new RouteParameters()))
                .thenReturn(Optional.of("edit-profile"));

        // Creating the StudentDashboardView instance with mocked services
        studentDashboardView = new StudentDashboardView(courseService, gradeService, attendanceService, authenticatedUser, messageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear any references to avoid memory leaks
        studentDashboardView = null;

        // Reset VaadinService and VaadinSession to avoid affecting other tests
        VaadinService.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    public void testStudentDashboardViewInitialization() {
        // Check if the main title is present
        H1 title = (H1) studentDashboardView.getContent().getComponentAt(0);
        assertNotNull(title);
        assertEquals("dashboard.welcome", title.getText()); // Adjusted to match the key used in your messages

        // Check if the dashboard grid is present
        FlexLayout dashboardGrid = (FlexLayout) studentDashboardView.getContent().getComponentAt(1);
        assertNotNull(dashboardGrid);
        assertEquals(4, dashboardGrid.getComponentCount()); // Should have 4 dashboard cards

        // Check if the correct number of cards is displayed with expected titles
        checkCardTitle(dashboardGrid.getComponentAt(0), "dashboard.courses.title");
        checkCardTitle(dashboardGrid.getComponentAt(1), "dashboard.grades.title");
        checkCardTitle(dashboardGrid.getComponentAt(2), "dashboard.attendance.title");
        checkCardTitle(dashboardGrid.getComponentAt(3), "dashboard.editprofile.title");
    }

    private void checkCardTitle(Component card, String expectedTitleKey) {
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
                assertEquals(expectedTitleKey, title.getText()); // Check title against the key
            } else {
                // Handle the case where there is no RouterLink
                Paragraph title = (Paragraph) divCard.getChildren()
                        .filter(component -> component instanceof Paragraph)
                        .findFirst()
                        .orElse(null);
                assertNotNull(title);
                assertEquals(expectedTitleKey, title.getText()); // Check title against the key
            }
        }
    }

    private MessageSource createMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // Base name for message bundles
        return messageSource;
    }
}

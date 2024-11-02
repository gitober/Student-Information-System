package com.studentinfo.views.homeprofilepage;

import com.studentinfo.data.entity.Course;
import com.studentinfo.data.entity.Student;
import com.studentinfo.services.AttendanceService;
import com.studentinfo.services.CourseService;
import com.studentinfo.services.GradeService;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.studentinfo.data.entity.Attendance;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/home-profile-page-view/student-profile-page-view.css")
public class StudentDashboardView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final AttendanceService attendanceService;
    private final AuthenticatedUser authenticatedUser;
    private final MessageSource messageSource;

    @Autowired
    public StudentDashboardView(CourseService courseService, GradeService gradeService,
                                AttendanceService attendanceService, AuthenticatedUser authenticatedUser,
                                MessageSource messageSource) {
        this.courseService = courseService;
        this.attendanceService = attendanceService;
        this.authenticatedUser = authenticatedUser;
        this.messageSource = messageSource;

        // Main layout setup
        getContent().addClassName("student-profile-page-view");

        // Page title
        H1 welcomeText = new H1(getMessage("dashboard.welcome"));
        welcomeText.addClassName("student-dashboard-welcome-text");
        getContent().add(welcomeText);

        // Fetch the current student's number
        Long studentNumber = getCurrentStudentNumber();

        // Retrieve the enrolled courses for the student
        List<Course> enrolledCourses = courseService.getEnrolledCourses(studentNumber);

        // Dashboard Grid Container
        FlexLayout dashboardGrid = new FlexLayout();
        dashboardGrid.addClassName("student-dashboard-grid");
        dashboardGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        dashboardGrid.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);

        // Add cards with navigation links
        dashboardGrid.add(createDashboardCard(
                getMessage("dashboard.courses.title"),
                courseService.getAllCourses().size() + " " + getMessage("dashboard.courses.count"),
                CoursesView.class, "./icons/Your_Courses.png"));

        dashboardGrid.add(createDashboardCard(
                getMessage("dashboard.grades.title"),
                gradeService.getAllGrades().size() + " " + getMessage("dashboard.grades.count"),
                GradesView.class, "./icons/Recent_Grades.png"));

        dashboardGrid.add(createDashboardCard(
                getMessage("dashboard.attendance.title"),
                getMessage("dashboard.attendance.description"),
                null, "./icons/Manage_Attendance.png"));

        dashboardGrid.add(createDashboardCard(
                getMessage("dashboard.editprofile.title"),
                getMessage("dashboard.editprofile.description"),
                EditProfileView.class, "./icons/Quick_Links.png"));

        getContent().add(dashboardGrid);
    }

    private Div createDashboardCard(String title, String description, Class<? extends Component> navigationTarget, String iconUrl) {
        Div card = new Div();
        card.addClassName("student-dashboard-card");

        // Unique class for each card based on title
        String uniqueClassName = "student-dashboard-card-" + title.toLowerCase().replace(" ", "-");
        card.addClassName(uniqueClassName);

        // Add the image icon at the top of the card
        Image cardIcon = new Image(iconUrl, title + " Icon");
        cardIcon.addClassName("student-dashboard-card-icon"); // New class for the icon
        card.add(cardIcon); // Add icon before title

        // Title
        Paragraph cardTitle = new Paragraph(title);
        cardTitle.addClassName("student-dashboard-card-title");

        // Description
        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassName("student-dashboard-card-description");

        if (navigationTarget != null) {
            RouterLink link = new RouterLink();
            link.addClassName("student-dashboard-card-link");
            link.setRoute(navigationTarget);
            link.add(cardIcon, cardTitle, cardDescription); // Include icon in the link
            card.add(link);
        } else {
            // Add click listener to open the dialog when the card is clicked
            card.addClickListener(event -> {
                if (title.equals(getMessage("dashboard.attendance.title"))) {
                    openAttendanceDialog(); // No arguments needed
                }
            });
            card.add(cardIcon, cardTitle, cardDescription); // Include icon when no navigation target
        }

        return card;
    }

    private void openAttendanceDialog() {
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-attendance-dialog");

        // Title for the dialog
        H1 title = new H1(getMessage("attendance.dialog.title"));
        title.addClassName("attendance-dialog-title");
        attendanceDialog.add(title);

        // Fetch the current student's number
        Long studentNumber = getCurrentStudentNumber();

        // Create a grid to display attendance records
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class);
        attendanceGrid.addClassName("student-attendance-grid"); // Existing class
        attendanceGrid.addClassName("custom-attendance-grid"); // New class for styling
        attendanceGrid.removeAllColumns(); // Clear existing columns
        attendanceGrid.addColumn(attendance -> attendance.getCourse().getCourseName()).setHeader(getMessage("attendance.grid.course"));
        attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader(getMessage("attendance.grid.date"));
        attendanceGrid.addColumn(Attendance::getAttendanceStatus).setHeader(getMessage("attendance.grid.status"));

        // Fetch attendance records for all enrolled courses
        List<Course> enrolledCourses = courseService.getEnrolledCourses(studentNumber);
        List<Attendance> attendanceRecords = enrolledCourses.stream()
                .flatMap(course -> attendanceService.getAttendanceByCourseId(course.getCourseId()).stream()
                        .filter(attendance -> attendance.getStudent() != null && attendance.getStudent().getStudentNumber().equals(studentNumber)))
                .collect(Collectors.toList());

        // Set items in the attendance grid
        attendanceGrid.setItems(attendanceRecords);

        // Close button
        Button closeButton = new Button(getMessage("attendance.dialog.close"), event -> attendanceDialog.close());
        closeButton.addClassName("attendance-dialog-close-button");
        attendanceDialog.add(new HorizontalLayout(closeButton));

        attendanceDialog.add(attendanceGrid); // Add the attendance grid to the dialog
        attendanceDialog.open();
    }

    private String getMessage(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    private Long getCurrentStudentNumber() {
        return authenticatedUser.get().map(user -> {
            if (user instanceof Student) {
                return ((Student) user).getStudentNumber();
            }
            return null;
        }).orElse(null);
    }
}

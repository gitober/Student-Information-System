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
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/home-profile-page-view/student-profile-page-view.css")
public class StudentDashboardView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final GradeService gradeService;
    private final AttendanceService attendanceService;
    private final AuthenticatedUser authenticatedUser;
    private List<Course> enrolledCourses;

    @Autowired
    public StudentDashboardView(CourseService courseService, GradeService gradeService, AttendanceService attendanceService, AuthenticatedUser authenticatedUser) {
        this.courseService = courseService;
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
        this.authenticatedUser = authenticatedUser;

        // Main layout setup
        getContent().addClassName("student-profile-page-view");

        // Page title
        H1 welcomeText = new H1("Student Dashboard");
        welcomeText.addClassName("student-dashboard-welcome-text");
        getContent().add(welcomeText);

        // Fetch the current student's number
        Long studentNumber = getCurrentStudentNumber();

        // Retrieve the enrolled courses for the student
        enrolledCourses = courseService.getEnrolledCourses(studentNumber);

        // Dashboard Grid Container
        FlexLayout dashboardGrid = new FlexLayout();
        dashboardGrid.addClassName("student-dashboard-grid");
        dashboardGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        dashboardGrid.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);

        // Add cards with navigation links
        dashboardGrid.add(createDashboardCard("Your Courses", courseService.getAllCourses().size() + " Enrolled", CoursesView.class));
        dashboardGrid.add(createDashboardCard("Grades", gradeService.getAllGrades().size() + " Records", GradesView.class));
        dashboardGrid.add(createDashboardCard("Attendance", "View Attendance", null)); // Opens the attendance dialog
        dashboardGrid.add(createDashboardCard("Edit Profile", "Update Your Information", EditProfileView.class));

        getContent().add(dashboardGrid);
    }

    private void openAttendanceDialog() {
        Dialog attendanceDialog = new Dialog();
        attendanceDialog.addClassName("student-attendance-dialog");

        // Title for the dialog
        H1 title = new H1("Your Attendance");
        title.addClassName("attendance-dialog-title");
        attendanceDialog.add(title);

        // Fetch the current student's number
        Long studentNumber = getCurrentStudentNumber();

        // Create a grid to display attendance records
        Grid<Attendance> attendanceGrid = new Grid<>(Attendance.class);
        attendanceGrid.addClassName("student-attendance-grid");
        attendanceGrid.removeAllColumns(); // Clear existing columns
        attendanceGrid.addColumn(attendance -> attendance.getCourse().getCourseName()).setHeader("Course Name");
        attendanceGrid.addColumn(Attendance::getAttendanceDate).setHeader("Date");
        attendanceGrid.addColumn(Attendance::getAttendanceStatus).setHeader("Status");

        // Fetch attendance records for all enrolled courses
        List<Course> enrolledCourses = courseService.getEnrolledCourses(studentNumber);
        List<Attendance> attendanceRecords = enrolledCourses.stream()
                .flatMap(course -> attendanceService.getAttendanceByCourseId(course.getCourseId()).stream()
                        .filter(attendance -> attendance.getStudent().getStudentNumber().equals(studentNumber)))
                .collect(Collectors.toList());

        // Set items in the attendance grid
        attendanceGrid.setItems(attendanceRecords);

        // Add attendance grid to the dialog
        attendanceDialog.add(attendanceGrid);

        // Close button
        Button closeButton = new Button("Close", event -> attendanceDialog.close());
        closeButton.addClassName("attendance-dialog-close-button");
        attendanceDialog.add(new HorizontalLayout(closeButton));

        // Open the attendance dialog
        attendanceDialog.open();
    }

    private Div createDashboardCard(String title, String description, Class<? extends Component> navigationTarget) {
        Div card = new Div();
        card.addClassName("student-dashboard-card");

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
            link.add(cardTitle, cardDescription);
            card.add(link);
        } else {
            // Add click listener to open the dialog when the card is clicked
            card.addClickListener(event -> {
                if (title.equals("Attendance")) {
                    openAttendanceDialog(); // No arguments needed
                }
            });
            card.add(cardTitle, cardDescription);
        }

        return card;
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

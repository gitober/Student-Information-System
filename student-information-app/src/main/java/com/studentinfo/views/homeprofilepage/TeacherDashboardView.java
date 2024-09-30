package com.studentinfo.views.homeprofilepage;

import com.studentinfo.services.CourseService;
import com.studentinfo.services.StudentService;
import com.studentinfo.services.GradeService;
import com.studentinfo.views.TeacherAttendanceTrackingView.TeacherAttendanceTrackingView;
import com.studentinfo.views.TeacherUpdateStudentProfileView.TeacherUpdateStudentProfileView;
import com.studentinfo.views.courses.CoursesView;
import com.studentinfo.views.editprofile.EditProfileView;
import com.studentinfo.views.grades.GradesView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/home-profile-page-view/teacher-profile-page-view.css")
public class TeacherDashboardView extends Composite<VerticalLayout> {

    private final CourseService courseService;
    private final StudentService studentService;
    private final GradeService gradeService;

    @Autowired
    public TeacherDashboardView(CourseService courseService, StudentService studentService, GradeService gradeService) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.gradeService = gradeService;

        // Main layout setup
        getContent().addClassName("teacher-dashboard-view");

        // Page title
        H1 welcomeText = new H1("Teacher Dashboard");
        welcomeText.addClassName("teacher-dashboard-welcome-text");
        getContent().add(welcomeText);

        // Dashboard Grid Container
        FlexLayout dashboardGrid = new FlexLayout();
        dashboardGrid.addClassName("dashboard-grid");
        dashboardGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        dashboardGrid.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);

        // Add cards with navigation links
        dashboardGrid.add(createDashboardCard("Your Courses", courseService.getAllCourses().size() + " Classes", CoursesView.class));
        dashboardGrid.add(createDashboardCard("Students", studentService.list().size() + " Students", TeacherUpdateStudentProfileView.class));
        dashboardGrid.add(createDashboardCard("Recent Grades", gradeService.getAllGrades().size() + " Grades", GradesView.class));
        dashboardGrid.add(createDashboardCard("Manage Attendance", "Click to Manage", TeacherAttendanceTrackingView.class));
        dashboardGrid.add(createDashboardCard("Quick Links", "Access Tools", EditProfileView.class));

        getContent().add(dashboardGrid);
    }

    private Div createDashboardCard(String title, String description, Class<? extends Component> navigationTarget) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        // Title
        Paragraph cardTitle = new Paragraph(title);
        cardTitle.addClassName("dashboard-card-title");

        // Description
        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassName("dashboard-card-description");

        if (navigationTarget != null) {
            RouterLink link = new RouterLink();
            link.setRoute(navigationTarget); // This should work now
            link.add(cardTitle, cardDescription);
            card.add(link);
        } else {
            card.add(cardTitle, cardDescription);
        }

        return card;
    }

}

package com.studentinfo.views.homeprofilepage;

import com.studentinfo.services.CourseService;
import com.studentinfo.services.StudentService;
import com.studentinfo.services.GradeService;
import com.studentinfo.views.TeacherAttendanceView.TeacherAttendanceView;
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
import com.vaadin.flow.component.html.Image;


@SpringComponent
@UIScope
@CssImport("./themes/studentinformationapp/views/home-profile-page-view/teacher-profile-page-view.css")
public class TeacherDashboardView extends Composite<VerticalLayout> {

    @Autowired
    public TeacherDashboardView(CourseService courseService, StudentService studentService, GradeService gradeService) {

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

        dashboardGrid.add(createDashboardCard("Students", studentService.list().size() + " Students", TeacherUpdateStudentProfileView.class, "./icons/Students.png"));
        dashboardGrid.add(createDashboardCard("Grading", gradeService.getAllGrades().size() + " Grades", GradesView.class, "./icons/Recent_Grades.png"));
        dashboardGrid.add(createDashboardCard("Manage Courses", courseService.getAllCourses().size() + " Classes", CoursesView.class, "./icons/Your_Courses.png"));
        dashboardGrid.add(createDashboardCard("Manage Attendance", "Click to Manage", TeacherAttendanceView.class, "./icons/Manage_Attendance.png"));
        dashboardGrid.add(createDashboardCard("Quick Links", "Access Tools", EditProfileView.class, "./icons/Quick_Links.png"));

        getContent().add(dashboardGrid);
    }

    private Div createDashboardCard(String title, String description, Class<? extends Component> navigationTarget, String iconUrl) {
        Div card = new Div();
        card.addClassName("dashboard-card");

        // Unique class for each card based on title
        String uniqueClassName = "dashboard-card-" + title.toLowerCase().replace(" ", "-");
        card.addClassName(uniqueClassName);

        // Add the image icon at the top of the card
        Image cardIcon = new Image(iconUrl, title + " Icon");
        cardIcon.addClassName("dashboard-card-icon"); // New class for the icon
        card.add(cardIcon); // Add icon before title

        // Title
        Paragraph cardTitle = new Paragraph(title);
        cardTitle.addClassName("dashboard-card-title");

        // Description
        Paragraph cardDescription = new Paragraph(description);
        cardDescription.addClassName("dashboard-card-description");

        if (navigationTarget != null) {
            RouterLink link = new RouterLink();
            link.setRoute(navigationTarget);
            link.add(cardTitle, cardDescription); // Include title and description in the link
            card.add(cardIcon, link); // Add icon and link to the card
        } else {
            card.add(cardIcon, cardTitle, cardDescription);
        }
        return card;
    }
}

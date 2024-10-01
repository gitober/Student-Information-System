package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.homeprofilepage.StudentDashboardView;
import com.studentinfo.views.homeprofilepage.TeacherDashboardView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.when;

class UserContentLoaderTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private StudentDashboardView studentDashboardView;

    @Mock
    private TeacherDashboardView teacherDashboardView;

    @InjectMocks
    private UserContentLoader userContentLoader;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the views to return a non-null element when added to a layout
        when(studentDashboardView.getElement()).thenReturn(new Div().getElement());
        when(teacherDashboardView.getElement()).thenReturn(new Div().getElement());
    }

    @Test
    void testLoadProfileContentForTeacher() {
        // Arrange
        when(authenticatedUser.get()).thenReturn(Optional.of(new Teacher()));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadProfileContent(layout);

        // Assert
        assert layout.getComponentCount() == 1;
    }

    @Test
    void testLoadProfileContentForStudent() {
        // Arrange
        when(authenticatedUser.get()).thenReturn(Optional.of(new Student()));
        VerticalLayout layout = new VerticalLayout();

        // Act
        userContentLoader.loadProfileContent(layout);

        // Assert
        assert layout.getComponentCount() == 1;
    }
}

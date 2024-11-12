package com.studentinfo.services;

import com.studentinfo.data.entity.Student;
import com.studentinfo.data.entity.Teacher;
import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.homeprofilepage.StudentDashboardView;
import com.studentinfo.views.homeprofilepage.TeacherDashboardView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    }

    @AfterEach
    void tearDown() {
        // Reset all mocks to ensure no interaction is carried over between tests
        reset(authenticatedUser, studentDashboardView, teacherDashboardView);
    }

    @Test
    void testLoadProfileContentForTeacher() {
        // Arrange
        Teacher teacher = new Teacher();
        when(authenticatedUser.get()).thenReturn(Optional.of(teacher));
        VerticalLayout layout = mock(VerticalLayout.class);  // Mock the layout

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
        VerticalLayout layout = mock(VerticalLayout.class);  // Mock the layout

        // Act
        userContentLoader.loadProfileContent(layout);

        // Capture the component added to layout
        ArgumentCaptor<Component> captor = ArgumentCaptor.forClass(Component.class);
        verify(layout).add(captor.capture());

        // Assert
        Component addedComponent = captor.getValue();
        Assertions.assertInstanceOf(StudentDashboardView.class, addedComponent, "Expected StudentDashboardView component for student");
    }
}

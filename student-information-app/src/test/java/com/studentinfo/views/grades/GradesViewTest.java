package com.studentinfo.views.grades;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class GradesViewTest {

    @InjectMocks
    private GradesView gradesView;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private UserContentLoader userContentLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }


    @Test
    public void testGradesViewInitialization() {
        // Verify that the GradesView is initialized properly
        assertNotNull(gradesView);
    }

    @Test
    public void testLoadUserSpecificContent() {
        // Create a mock layout to load the content into
        VerticalLayout layoutColumn = new VerticalLayout();

        // Call the userContentLoader to load grades content
        gradesView.getContent().add(layoutColumn); // Simulating adding to the view
        gradesView.getContent().add(new HeaderView("EduBird", authenticatedUser)); // Add header to the layout

        // Verify that the loadGradesContent method is called with any instance of VerticalLayout
        verify(userContentLoader).loadGradesContent(any(VerticalLayout.class));
    }
}

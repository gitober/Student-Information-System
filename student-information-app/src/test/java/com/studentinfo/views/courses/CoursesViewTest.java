package com.studentinfo.views.courses;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CoursesViewTest {

    private UserContentLoader userContentLoader;
    private CoursesView coursesView;
    private MessageSource messageSource;

    @BeforeEach
    public void setUp() {
        // Create mocks for the dependencies
        AuthenticatedUser authenticatedUser = Mockito.mock(AuthenticatedUser.class);
        userContentLoader = Mockito.mock(UserContentLoader.class);
        messageSource = Mockito.mock(MessageSource.class);

        // Instantiate the view with mocks
        coursesView = new CoursesView(authenticatedUser, userContentLoader, messageSource);
    }

    @AfterEach
    void tearDown() {
        // Reset mocks to ensure no shared state between tests
        Mockito.reset(userContentLoader, messageSource);
    }

    @Test
    public void testCoursesViewComponents() {
        // Retrieve the main layout
        VerticalLayout mainLayout = coursesView.getContent();

        // Verify that the main layout is not null
        assertNotNull(mainLayout, "The main layout should not be null.");

        // Verify that the main layout has two child components: HeaderView and layoutColumn
        assertEquals(2, mainLayout.getComponentCount(), "Main layout should contain two components.");

        // Verify that the first component is the HeaderView
        assertEquals(HeaderView.class, mainLayout.getComponentAt(0).getClass(), "First component should be HeaderView.");

        // Verify that user-specific content is loaded (mocked, so we can't verify real content here)
        Mockito.verify(userContentLoader).loadCoursesContent(Mockito.any(VerticalLayout.class));
    }
}

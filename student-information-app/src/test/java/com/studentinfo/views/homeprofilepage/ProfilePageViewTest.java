package com.studentinfo.views.homeprofilepage;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class ProfilePageViewTest {

    private ProfilePageView profilePageView;
    private AuthenticatedUser authenticatedUser;
    private UserContentLoader userContentLoader;

    @BeforeEach
    public void setUp() {
        // Create mocks for AuthenticatedUser and UserContentLoader
        authenticatedUser = Mockito.mock(AuthenticatedUser.class);
        userContentLoader = Mockito.mock(UserContentLoader.class);

        // Initialize ProfilePageView with mocked dependencies
        profilePageView = new ProfilePageView(authenticatedUser, userContentLoader);
    }

    @Test
    public void testProfilePageViewInitialization() {
        // Get the main layout from ProfilePageView
        VerticalLayout mainLayout = profilePageView.getContent();

        // Check that the main layout is not null
        assertNotNull(mainLayout, "Main layout should not be null.");

        // Check if the HeaderView is added to the layout
        assertTrue(mainLayout.getComponentAt(0) instanceof HeaderView, "First component should be an instance of HeaderView.");

        // Check the total number of components in the layout
        int expectedComponentCount = 2; // One for HeaderView and one for layoutColumn
        assertEquals(expectedComponentCount, mainLayout.getComponentCount(), "Main layout should contain the expected number of components.");

        // Verify that the padding is set correctly
        assertEquals("60px", mainLayout.getStyle().get("padding-top"), "Padding-top should be set to 60px.");
    }
}

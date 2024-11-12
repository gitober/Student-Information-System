package com.studentinfo.views.homeprofilepage;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfilePageViewTest {

    private ProfilePageView profilePageView;

    @BeforeEach
    public void setUp() {
        // Set up UI context for Vaadin components
        UI ui = new UI();
        ui.setLocale(Locale.ENGLISH); // Set a default locale
        UI.setCurrent(ui);

        // Mock the AuthenticatedUser, UserContentLoader, and MessageSource dependencies
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        UserContentLoader userContentLoader = mock(UserContentLoader.class);
        MessageSource messageSource = mock(MessageSource.class);

        // Directly use Locale.ENGLISH for the mock
        when(messageSource.getMessage("header.title", null, Locale.ENGLISH)).thenReturn("Student Information System");

        // Initialize ProfilePageView with mocked dependencies
        profilePageView = new ProfilePageView(authenticatedUser, userContentLoader, messageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clear the UI context and any references to avoid memory leaks
        UI.setCurrent(null);
        profilePageView = null;
    }

    @Test
    void testProfilePageViewInitialization() {
        // Get the main layout from ProfilePageView
        VerticalLayout mainLayout = profilePageView.getContent();

        // Check that the main layout is not null
        assertNotNull(mainLayout, "Main layout should not be null.");

        // Check if the HeaderView is added to the layout
        assertInstanceOf(HeaderView.class, mainLayout.getComponentAt(0), "First component should be an instance of HeaderView.");

        // Check the total number of components in the layout
        int expectedComponentCount = 2; // One for HeaderView and one for layoutColumn
        assertEquals(expectedComponentCount, mainLayout.getComponentCount(), "Main layout should contain the expected number of components.");

        // Verify that the padding is set correctly
        assertEquals("60px", mainLayout.getStyle().get("padding-top"), "Padding-top should be set to 60px.");
    }
}

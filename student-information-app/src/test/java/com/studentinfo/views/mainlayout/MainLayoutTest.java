package com.studentinfo.views.mainlayout;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainLayoutTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private MessageSource messageSource;

    private MainLayout mainLayout;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up the Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Manually initialize MainLayout with mocked dependencies
        mainLayout = new MainLayout(authenticatedUser, messageSource);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the UI context
        UI.setCurrent(null);
    }

    @Test
    void testHeaderViewIsAddedToMainLayout() {
        // Act
        Div content = mainLayout.getContent(); // This retrieves the root layout of MainLayout

        // Assert that MainLayout contains components and the first one is an instance of HeaderView
        assertTrue(content.getComponentCount() > 0, "MainLayout should contain components.");
        assertInstanceOf(HeaderView.class, content.getComponentAt(0), "The first component should be a HeaderView.");
    }
}

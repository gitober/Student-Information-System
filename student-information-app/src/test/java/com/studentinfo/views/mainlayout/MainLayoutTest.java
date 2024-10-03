package com.studentinfo.views.mainlayout;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.html.Div;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainLayoutTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private MainLayout mainLayout;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mainLayout = new MainLayout(authenticatedUser); // Initialize MainLayout with the mocked AuthenticatedUser
    }

    @Test
    public void testHeaderViewIsAddedToMainLayout() {
        // Act
        Div content = mainLayout.getContent(); // This retrieves the root layout of MainLayout

        // Assert that MainLayout contains components and the first one is an instance of HeaderView
        assertTrue(content.getComponentCount() > 0, "MainLayout should contain components.");
        assertInstanceOf(HeaderView.class, content.getComponentAt(0), "The first component should be a HeaderView.");
    }
}

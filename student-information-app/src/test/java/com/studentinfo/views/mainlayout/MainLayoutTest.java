package com.studentinfo.views.mainlayout;

import com.studentinfo.views.header.HeaderView;
import com.studentinfo.security.AuthenticatedUser;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class MainLayoutTest {

    @Mock
    private AuthenticatedUser authenticatedUser;

    @InjectMocks
    private MainLayout mainLayout;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHeaderViewIsAddedToMainLayout() {
        // Arrange
        String expectedHeaderTitle = "EduBird";

        // Mock the necessary behavior of AuthenticatedUser if needed
        // For example: when(authenticatedUser.getRole()).thenReturn("ROLE_USER");

        // Act
        Div content = mainLayout.getContent(); // This should initialize HeaderView in MainLayout

        // Assert
        assertTrue(content.getComponentCount() > 0, "MainLayout should contain components.");
        assertTrue(content.getComponentAt(0) instanceof HeaderView, "The first component should be a HeaderView.");
        HeaderView headerView = (HeaderView) content.getComponentAt(0);

        // Assert the title of HeaderView (modify according to your actual method)
        // You should replace getTitle() with the correct method from HeaderView that retrieves the title
        // For example:
        // assertEquals(expectedHeaderTitle, headerView.getTitle(), "HeaderView title should be 'EduBird'.");
    }
}

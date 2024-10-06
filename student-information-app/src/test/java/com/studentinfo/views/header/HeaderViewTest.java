package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HeaderViewTest {

    private HeaderView headerView;
    private MockedConstruction<RouterLink> mockedRouterLink;

    @BeforeEach
    public void setUp() {
        // Mock the AuthenticatedUser to simulate a logged-in user
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        User mockUser = mock(User.class);
        when(authenticatedUser.get()).thenReturn(Optional.of(mockUser));

        // Set up the UI context for Vaadin components
        UI.setCurrent(new UI());

        // Properly mock RouterLink construction
        mockedRouterLink = Mockito.mockConstruction(RouterLink.class, (mock, context) -> {
            when(mock.getElement()).thenReturn(new Span().getElement());  // Ensure complete stubbing
            when(mock.getText()).thenReturn("Sample Link"); // Add more stubbing if needed
        });

        // Instantiate HeaderView with the mocked user
        headerView = new HeaderView("Student Information System", authenticatedUser);
    }

    @AfterEach
    public void tearDown() {
        // Clean up the UI context
        UI.setCurrent(null);

        // Close the mocked construction to avoid state leaks
        if (mockedRouterLink != null) {
            mockedRouterLink.close();
        }

        // Clear any remaining mock objects
        mockedRouterLink = null;
    }

    @Test
    public void testHeaderViewBasicComponents() {
        // Test if the logo (Image) is present inside the HorizontalLayout
        Image logo = headerView.getChildren()
                .filter(component -> component instanceof HorizontalLayout)
                .flatMap(Component::getChildren)
                .filter(component -> component instanceof Image)
                .map(component -> (Image) component)
                .findFirst()
                .orElse(null);
        assertNotNull(logo, "Logo image should be present in the header.");

        // Test if the application title (Span) is present
        Span appName = headerView.getChildren()
                .filter(component -> component instanceof Span)
                .map(component -> (Span) component)
                .findFirst()
                .orElse(null);
        assertNotNull(appName, "Application name span should be present in the header.");

        // Test if the logout button is present
        Button logoutButton = headerView.getChildren()
                .filter(component -> component instanceof Button)
                .map(component -> (Button) component)
                .filter(button -> "Logout".equals(button.getText()))
                .findFirst()
                .orElse(null);
        assertNotNull(logoutButton, "Logout button should be present in the header.");
    }
}

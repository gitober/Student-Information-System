package com.studentinfo.views.editprofile;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.services.UserContentLoader;
import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class EditProfileViewTest {

    private EditProfileView editProfileView;

    @BeforeEach
    public void setUp() {
        // Mock the dependencies
        AuthenticatedUser authenticatedUser = Mockito.mock(AuthenticatedUser.class);
        UserContentLoader userContentLoader = Mockito.mock(UserContentLoader.class);
        MessageSource messageSource = Mockito.mock(MessageSource.class);

        // Initialize a Vaadin UI context
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the view with mocked dependencies
        editProfileView = new EditProfileView(authenticatedUser, userContentLoader, messageSource);
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null); // Clear the Vaadin UI context to avoid side effects between tests
    }

    @Test
    public void testEditProfileViewComponents() {
        // Verify that the main layout is not null
        VerticalLayout mainLayout = editProfileView.getContent();
        assertNotNull(mainLayout, "The main layout should not be null.");

        // Check if the header is present
        assertTrue(mainLayout.getChildren()
                        .anyMatch(component -> component instanceof HeaderView),
                "HeaderView should be present in the main layout.");

        // Check if the layout for user-specific content is added
        assertTrue(mainLayout.getChildren()
                        .anyMatch(component -> component instanceof VerticalLayout),
                "A VerticalLayout for user-specific content should be present in the main layout.");
    }
}

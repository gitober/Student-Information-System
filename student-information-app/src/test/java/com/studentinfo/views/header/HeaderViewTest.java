package com.studentinfo.views.header;

import com.studentinfo.security.AuthenticatedUser;
import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLink;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.VaadinService;

import java.util.Locale;
import java.util.Optional;

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

        // Mock the MessageSource for translations
        MessageSource messageSource = mock(MessageSource.class);
        when(messageSource.getMessage("header.logout", null, Locale.ENGLISH)).thenReturn("Logout");

        // Mock UI and set it as current with a specific locale
        UI mockUI = new UI();
        mockUI.setLocale(Locale.ENGLISH);
        UI.setCurrent(mockUI);

        // Mock VaadinSession and VaadinService to avoid null pointer exceptions
        VaadinSession mockSession = mock(VaadinSession.class);
        VaadinService mockService = mock(VaadinService.class);
        when(mockSession.getService()).thenReturn(mockService);
        VaadinSession.setCurrent(mockSession);

        // Properly mock RouterLink construction
        mockedRouterLink = Mockito.mockConstruction(RouterLink.class, (mock, context) -> {
            when(mock.getElement()).thenReturn(new Span().getElement());
            when(mock.getText()).thenReturn("Sample Link");
        });

        // Instantiate HeaderView with the mocked user and message source
        headerView = new HeaderView(authenticatedUser, messageSource);
    }

    @AfterEach
    public void tearDown() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);

        if (mockedRouterLink != null) {
            mockedRouterLink.close();
        }
        mockedRouterLink = null;
    }

    @Test
    public void testHeaderViewConstruction() {
        // Check if the header view is constructed correctly
        System.out.println("HeaderView constructed: " + headerView);
        Assertions.assertNotNull(headerView, "HeaderView should be constructed.");
    }
}
package com.studentinfo.views.logout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.page.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class LogoutViewTest {

    private LogoutView logoutView;
    private BeforeEnterEvent mockBeforeEnterEvent;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock the dependencies
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        mockBeforeEnterEvent = mock(BeforeEnterEvent.class);

        // Set up the SecurityContext
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        // Set up the UI context for Vaadin and attach a mocked VaadinSession
        UI ui = new UI();
        UI.setCurrent(ui);

        // Create a mock VaadinService
        VaadinService mockService = mock(VaadinService.class);

        // Create a mock VaadinSession and attach the mocked service
        VaadinSession mockSession = mock(VaadinSession.class);
        when(mockSession.getService()).thenReturn(mockService);
        VaadinSession.setCurrent(mockSession);

        // Attach the session to the UI
        ui.getInternals().setSession(mockSession);

        // Create a spy for the Page object to verify redirection
        Page mockPage = spy(ui.getPage());

        // Replace the page in the UI with the mockPage
        Field pageField = UI.class.getDeclaredField("page");
        pageField.setAccessible(true);
        pageField.set(ui, mockPage);

        // Instantiate the LogoutView
        logoutView = new LogoutView();

        // Use reflection to inject the mock request and response into the private fields of LogoutView
        Field requestField = LogoutView.class.getDeclaredField("request");
        requestField.setAccessible(true);
        requestField.set(logoutView, mockRequest);

        Field responseField = LogoutView.class.getDeclaredField("response");
        responseField.setAccessible(true);
        responseField.set(logoutView, mockResponse);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clear the UI context to avoid affecting other tests
        UI.setCurrent(null);

        // Clear the VaadinSession to prevent memory leaks
        VaadinSession.setCurrent(null);

        // Clear the SecurityContext to prevent it from interfering with other tests
        SecurityContextHolder.clearContext();

        // Nullify references to help garbage collection
        logoutView = null;
        mockBeforeEnterEvent = null;
    }

    @Test
    public void testBeforeEnter() {
        // Call the beforeEnter method to trigger logout
        logoutView.beforeEnter(mockBeforeEnterEvent);

        // Verify that setLocation was called with "login"
        verify(UI.getCurrent().getPage(), times(1)).setLocation("login");
    }
}

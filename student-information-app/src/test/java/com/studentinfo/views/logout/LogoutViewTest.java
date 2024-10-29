package com.studentinfo.views.logout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.mockito.Mockito.*;

public class LogoutViewTest {

    private LogoutView logoutView;
    private BeforeEnterEvent mockBeforeEnterEvent;
    private HttpServletRequest mockRequest;
    private HttpServletResponse mockResponse;

    @BeforeEach
    public void setUp() throws Exception {
        // Mock dependencies
        mockRequest = mock(HttpServletRequest.class);
        mockResponse = mock(HttpServletResponse.class);
        mockBeforeEnterEvent = mock(BeforeEnterEvent.class);

        // Set up the SecurityContext
        SecurityContext mockSecurityContext = mock(SecurityContext.class);
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockSecurityContext.getAuthentication()).thenReturn(mockAuthentication);
        SecurityContextHolder.setContext(mockSecurityContext);

        // Set up the UI context for Vaadin and attach a mocked VaadinSession
        UI ui = new UI();
        UI.setCurrent(ui);

        // Instantiate the LogoutView
        logoutView = new LogoutView(mockRequest, mockResponse);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Clear the UI context and VaadinSession to avoid affecting other tests
        UI.setCurrent(null);
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testBeforeEnter() throws Exception {
        // Call the beforeEnter method to trigger logout
        logoutView.beforeEnter(mockBeforeEnterEvent);

        // Verify that sendRedirect was called on the HttpServletResponse with the correct path
        verify(mockResponse, times(1)).sendRedirect("/login");
    }

}

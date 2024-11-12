package com.studentinfo.services;

import com.studentinfo.data.entity.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import static org.mockito.Mockito.*;

class LoginHandlerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private LoginHandler loginHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest, httpServletResponse));

        // Mock Vaadin session
        VaadinSession vaadinSession = mock(VaadinSession.class);
        VaadinSession.setCurrent(vaadinSession);

        // Clear the SecurityContextHolder before each test
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testSuccessfulLogin() {
        String email = "test@example.com";
        String password = "password";
        boolean rememberMe = true;

        User user = new User();
        user.setUsername(email);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password, user.getAuthorities());

        when(userService.authenticate(email, password)).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(authToken)).thenReturn(mock(Authentication.class));

        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        try (var mockedNotification = Mockito.mockStatic(Notification.class)) {
            loginHandler.login(email, password, rememberMe);

            mockUI.access(() -> {
                mockedNotification.verify(() -> Notification.show("Logged in successfully!"), times(1));
            });
        }
    }

    @Test
    void testFailedLogin() {
        String email = "invalid@example.com";
        String password = "wrongpassword";
        boolean rememberMe = false;

        when(userService.authenticate(email, password)).thenReturn(Optional.empty());

        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        try (var mockedNotification = Mockito.mockStatic(Notification.class)) {
            loginHandler.login(email, password, rememberMe);

            mockUI.access(() -> {
                mockedNotification.verify(() -> Notification.show("Authentication failed. Please check your credentials."), times(1));
            });
        }
    }
}
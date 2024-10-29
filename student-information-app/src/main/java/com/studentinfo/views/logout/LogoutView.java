package com.studentinfo.views.logout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Route("logout")
@AnonymousAllowed // Allow unauthenticated access
public class LogoutView extends VerticalLayout implements BeforeEnterObserver {

    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Autowired
    public LogoutView(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Perform logout and clean up security context
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Redirect to login page
        try {
            response.sendRedirect("/login");
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally log this error or provide feedback in a way that does not rely on Vaadin UI components
        }
    }
}

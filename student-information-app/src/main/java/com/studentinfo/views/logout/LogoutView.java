package com.studentinfo.views.logout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Route("logout")
@PageTitle("Logout")
public class LogoutView extends VerticalLayout {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    public LogoutView() {
        // Display a message that informs the user about the logout
        add(new H1("You have been logged out successfully."));

        // Button for manual redirection to the login page
        Button loginButton = new Button("Go to Login", event -> UI.getCurrent().navigate("login"));
        add(loginButton);
    }

    @PostConstruct
    private void init() {
        // Handle logout: invalidate session and clear security context
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        // Redirect to login after a delay (2 seconds)
        UI.getCurrent().getPage().executeJs("setTimeout(() => window.location.href = '/login', 2000);");
    }
}

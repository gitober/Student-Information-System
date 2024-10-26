package com.studentinfo.views.mainlayout;

import com.studentinfo.views.header.HeaderView;
import com.studentinfo.security.AuthenticatedUser;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

@SpringComponent
@RouteScope
public class MainLayout extends Composite<Div> implements RouterLayout {

    @Autowired
    public MainLayout(AuthenticatedUser authenticatedUser, MessageSource messageSource) {
        // Choose the appropriate header based on whether the user is logged in
        HeaderView headerView;
        if (authenticatedUser.get().isPresent()) {
            // User is logged in, show full header
            headerView = new HeaderView(authenticatedUser, messageSource);
        } else {
            // Public page, show minimal header
            headerView = new HeaderView(messageSource);
        }
        // Add the header to the main layout
        getContent().add(headerView);
    }
}

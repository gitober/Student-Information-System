package com.studentinfo.views.mainlayout;

import com.studentinfo.views.header.HeaderView;
import com.studentinfo.security.AuthenticatedUser;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.spring.annotation.RouteScope;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@RouteScope
public class MainLayout extends Composite<Div> implements RouterLayout {

    @Autowired
    public MainLayout(AuthenticatedUser authenticatedUser) {
        // Simply add the HeaderView, which already handles the dynamic content based on user roles
        HeaderView headerView = new HeaderView("EduBird", authenticatedUser);

        // Add the header to the main layout
        getContent().add(headerView);
    }
}

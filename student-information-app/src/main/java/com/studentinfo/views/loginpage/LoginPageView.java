package com.studentinfo.views.loginpage;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Login Page")
@Route(value = "")
@RouteAlias(value = "")
@AnonymousAllowed
public class LoginPageView extends Composite<VerticalLayout> {

    public LoginPageView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        LoginForm loginForm = new LoginForm();
        Anchor link = new Anchor();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        link.setText("Register here");
        link.setHref("#");
        link.setWidth("100%");
        getContent().add(layoutColumn2);
        layoutColumn2.add(layoutRow);
        layoutColumn2.add(loginForm);
        layoutColumn2.add(link);
    }
}

package com.studentinfo.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends Composite<VerticalLayout> implements RouterLayout {

    public MainLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.addClassName("main-container");

        // Header
        Div header = new Div();
        header.addClassName("header");
        Span appName = new Span("EduBird");
        appName.addClassName("app-name");
        header.add(appName);

        layout.add(header);
        getContent().add(layout);
    }
}

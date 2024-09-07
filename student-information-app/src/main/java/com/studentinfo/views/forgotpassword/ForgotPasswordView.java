package com.studentinfo.views.forgotpassword;

import com.studentinfo.views.header.HeaderView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Forgot Password")
@Route(value = "forgotpassword")
@AnonymousAllowed
public class ForgotPasswordView extends Composite<VerticalLayout> {

    public ForgotPasswordView() {
        // Main layout setup
        VerticalLayout mainLayout = getContent();
        mainLayout.setSizeFull();
        mainLayout.setPadding(false);
        mainLayout.setSpacing(false);
        mainLayout.setAlignItems(Alignment.CENTER);
        mainLayout.setJustifyContentMode(JustifyContentMode.START);

        // Add the reusable Header component
        mainLayout.add(new HeaderView("Forgot Password"));

        // Layout for form and content
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        layoutColumn2.setAlignItems(Alignment.CENTER);
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);

        // Title and subtitle
        H3 h3 = new H3("Forgot Password?");
        h3.setWidth("100%");
        H6 h6 = new H6("Enter your email here");
        h6.setWidth("max-content");

        // Form setup
        FormLayout formLayout2Col = new FormLayout();
        formLayout2Col.setWidth("100%");
        EmailField emailField = new EmailField("Email");

        // Buttons setup
        Button buttonPrimary = new Button("Submit");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button buttonSecondary = new Button("Cancel");
        buttonSecondary.setWidth("min-content");

        // Buttons layout
        HorizontalLayout layoutRow = new HorizontalLayout(buttonPrimary, buttonSecondary);
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        // Add components to the form layout
        formLayout2Col.add(emailField);
        layoutColumn2.add(h3, h6, formLayout2Col, layoutRow);

        // Add the form layout to the main layout
        mainLayout.add(layoutColumn2);
    }
}

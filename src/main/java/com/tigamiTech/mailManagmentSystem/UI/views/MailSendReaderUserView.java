package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.CourrierDepart;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by proserve on 24/06/2014.
 */
public class MailSendReaderUserView  extends VerticalLayout implements View {

    public MailSendReaderUserView() {
        addStyleName("dashboard-view");
        setSizeFull();
        setMargin(true);
        VerticalLayout verticalLayout = new MailSentReaderView().createVerticalLayout(CourrierDepart.findAllByCurrentUser());
        verticalLayout.setSizeFull();
        addComponent(verticalLayout);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}
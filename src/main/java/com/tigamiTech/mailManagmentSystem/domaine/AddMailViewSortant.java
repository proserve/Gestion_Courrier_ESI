package com.tigamiTech.mailManagmentSystem.domaine;

import com.tigamiTech.mailManagmentSystem.UI.views.AddMailViewEntrant;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;


public class AddMailViewSortant extends VerticalLayout implements View {

    private final AddMailViewEntrant mailViewEntrant = new AddMailViewEntrant();

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
        addStyleName("reports");
        addComponent(mailViewEntrant.buildDraftsView(false));
        try {
        }catch (CmisConnectionException e){
            Label label = new Label("<span style='font-size:24;color:red'>Votre configuration alfresco n'est pas correct", ContentMode.HTML);
            VerticalLayout verticalLayout = new VerticalLayout(label);
            verticalLayout.addStyleName("create");
            Window window = new Window("", verticalLayout);
            verticalLayout.setMargin(true);
            verticalLayout.setSpacing(true);
            window.setModal(true);
            window.setSizeUndefined();
            getUI().addWindow(window);
        }
    }
}
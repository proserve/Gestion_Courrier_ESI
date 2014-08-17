package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import java.util.List;

/**
 * Created by proserve on 24/06/2014.
 */
public class MailSentReaderView  extends VerticalLayout implements View {


    public MailSentReaderView() {
        VerticalLayout verticalLayout = createVerticalLayout(CourrierDepart.findAll());
        verticalLayout.setSizeFull();
        addComponent(verticalLayout);
    }

    public VerticalLayout createVerticalLayout(List<CourrierDepart> courrierDeparts){
        VerticalLayout verticalLayout = new VerticalLayout();
        setSizeFull();
        addStyleName("dashboard-view");

        HorizontalLayout top = new HorizontalLayout();

        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        verticalLayout.addComponent(top);
        final Label title = new Label("Liste des courriers sortants");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);
        Button createMail = new MailReaderView().createSendMailBtn();
        top.addComponent(createMail);

        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        Table mailTable = new Table();
        mailTable.setSizeFull();
        mailTable.setSelectable(true);
        mailTable.setPageLength(10);
        mailTable.setImmediate(true);
        mailTable.addContainerProperty("Courrier", Label.class, null);
        mailTable.addContainerProperty("Détail", Label.class, null);
        mailTable.addContainerProperty("Etat", Label.class, null);

        mailTable.setColumnExpandRatio("Courrier", 2);
        mailTable.setColumnExpandRatio("Détail", 6);
        mailTable.setColumnExpandRatio("Etat", 1);

        HorizontalLayout mailTablerow = createRow();
        verticalLayout.addComponent(mailTablerow);
        mailTablerow.addComponent(mailTable);

        for(CourrierDepart courrier:courrierDeparts){
            Label detail = new Label("", ContentMode.HTML);
            Label courrierLabel = new Label("", ContentMode.HTML);
            Label etat = new Label("", ContentMode.HTML);

            detail.setValue("<p style='font-size: 14px;margin: 0'><b>"+courrier.getObjet()+"</b></p>" +
                    "Envoyer par "+courrier.getContact().getFullName()+ " Le "+courrier.getDateReception());

            courrierLabel.setValue("<div style=\"background: "+courrier.getClasseCourrier().getColor()+"; width: 7%;height:56px; float: left;margin-right: 3%;\"></div>" +
                    "<div style=\"width: 90%; float: left;\"><p style='font-size: 14px;margin: 0'><b>"+courrier.getTitre()+"</b></p>" +
                    courrier.getClasseCourrier().getName()+"</div>");
            etat.setValue("<b style=''>"+courrier.getEtatCourrier().getName()+"</b>");
            mailTable.addItem(new Object[]{
                    courrierLabel, detail, etat
            }, courrier.getId());
        }
        verticalLayout.setExpandRatio(mailTablerow, 8);
        return verticalLayout;
    }
    private HorizontalLayout createRow() {
        HorizontalLayout row = new HorizontalLayout();
        row.setMargin(true);
        row.setSizeFull();
        row.setSpacing(true);
        return row;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        setSizeFull();
    }
}

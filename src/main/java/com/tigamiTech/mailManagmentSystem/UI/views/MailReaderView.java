package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.CMIS.CMISConnector;
import com.tigamiTech.mailManagmentSystem.CMIS.CMISDocumentReader;
import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;
import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.addon.tableexport.TableExport;
import com.vaadin.event.Action;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.io.File;
import java.util.List;

public class MailReaderView extends VerticalLayout implements View {

    public static final String VAADIN_THEMES_DASHBOARD_FILES = "/VAADIN/themes/dashboard/files/";
    private Integer selectedItemId;
    private final EntityManager entityManager = initialDataLoader.em;
    private final Table courrierTable = new Table("Liste des courriers arrivés");
    private final Button notify;
    private final int courrierCountForCurrentUser;
    private Label etatLabel;
    private HorizontalSplitPanel horizontalSplitPanel;

    public MailReaderView() {
        addStyleName("dashboard-view");
        setSizeFull();
        setMargin(true);
        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("1000px");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        courrierCountForCurrentUser = new Courrier().getCourrierCountForCurrentUser();
        String caption = String.valueOf(courrierCountForCurrentUser);
        notify = new Button(caption);
        notify.setImmediate(true);
        notify.setDescription("Notifications (" + courrierCountForCurrentUser + " no lut)");
        notify.addStyleName("borderless");
        notify.addStyleName("notifications");
        notify.addStyleName("unread");
        notify.addStyleName("icon-bell");

        final Label title = new Label("Liste des courriers entrants");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);

        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        HorizontalLayout buttonLayout = createBtnLayout();
        if(courrierCountForCurrentUser != 0) {

            buttonLayout.addComponent(notify);
            buttonLayout.setComponentAlignment(notify, Alignment.MIDDLE_LEFT);
        }
        courrierTable.addActionHandler(new Action.Handler() {

            private Action excel = new Action("Exporter ce tableau vers Excel");

            private Action CVS = new Action("Exporter ce tableau vers CVS");

            private Action repondre = new Action("Répondre");
            @Override
            public void handleAction(Action action, Object sender, Object target) {
                TableExport excelTableExport;
                if (action == excel) {
                    excelTableExport =  new ExcelExport(courrierTable);
                    excelTableExport.export();
                } else if (action == CVS) {
                    excelTableExport = new CsvExport(courrierTable);
                    excelTableExport.export();
                }else if(action == repondre){
                    final Window reponseWindo = new Window("Répondre au courrier: " +
                            getCourrier(selectedItemId).getTitre(), new EnvoiCourrierCompo());
                    reponseWindo.center();
                    reponseWindo.setWidth("60%");
                    UI.getCurrent().addWindow(reponseWindo);
                }else{

                }

            }

            @Override
            public Action[] getActions(Object target, Object sender) {
                return new Action[] { excel, CVS, repondre };
            }
        });

        addComponent(top);
        addComponent(buttonLayout);
        VerticalSplitPanel emailSplitePanel = getEmailEntrantSplitePanel(false);
        addComponent(emailSplitePanel);

        setExpandRatio(top, 1);
        setExpandRatio(buttonLayout, 1);
        setExpandRatio(emailSplitePanel, 9);
    }

    public VerticalSplitPanel getEmailEntrantSplitePanel(boolean isAll){
        final VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSizeFull();

        courrierTable.setSizeFull();
        courrierTable.setSelectable(true);
        courrierTable.setPageLength(10);
        courrierTable.setImmediate(true);
        courrierTable.setColumnExpandRatio("Courrier", 1);
        courrierTable.setColumnExpandRatio("Détail", 4);
        courrierTable.addContainerProperty("Courrier", Label.class, null);
        courrierTable.addContainerProperty("Détail", Label.class, null);
        EntityManager entityManager = initialDataLoader.em;
        String query = "select u from Courrier u";

        List<Courrier> listCourrier =  entityManager.createQuery(query).getResultList();
        if(isAll){
            for (Courrier courrier:listCourrier) {
                if(!(courrier instanceof CourrierDepart))
                createTablItems(courrier);
            }
        }else{
            for (Courrier courrier:listCourrier) {
                if(isForThisUser(courrier) && !(courrier instanceof CourrierDepart)){
                    createTablItems(courrier);
                }
            }
        }


        verticalSplitPanel.addComponent(courrierTable);
        horizontalSplitPanel = new HorizontalSplitPanel();
        verticalSplitPanel.addComponent(horizontalSplitPanel);

        courrierTable.addListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                horizontalSplitPanel.removeAllComponents();
                selectedItemId = (Integer) itemClickEvent.getItemId();
                horizontalSplitPanel.addComponent(createCourrierDetaiLayout(selectedItemId));
                horizontalSplitPanel.addComponent(createCourrierFile(selectedItemId));

            }
        });



        return verticalSplitPanel;
    }

    public VerticalSplitPanel getEmailSortantSplitePanel(){
        final VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        verticalSplitPanel.setSizeFull();
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


        List<CourrierDepart> courrierDeparts =  CourrierDepart.findAll();
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


        verticalSplitPanel.addComponent(mailTable);
        final HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        verticalSplitPanel.addComponent(horizontalSplitPanel);

        mailTable.addListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {

                horizontalSplitPanel.removeAllComponents();
                selectedItemId = (Integer) itemClickEvent.getItemId();
                horizontalSplitPanel.addComponent(createCourrierDetaiLayout(selectedItemId));
                horizontalSplitPanel.addComponent(createCourrierFile(selectedItemId));

            }
        });



        return verticalSplitPanel;
    }

    private void createTablItems(Courrier courrier) {
        Label detail = new Label("", ContentMode.HTML);
        Label courrierLabel = new Label("", ContentMode.HTML);
        detail.setValue("<p style='font-size: 14px;margin: 0'><b>"+courrier.getObjet()+"</b></p>" +
                "Envoyer par "+courrier.getContact().getFullName()+ " Le "+courrier.getDateReception());

        courrierLabel.setValue("<div style=\"background: "+courrier.getClasseCourrier().getColor()+"; width: 7%;height:56px; float: left;margin-right: 3%;\"></div>" +
                "<div style=\"width: 90%; float: left;\"><p style='font-size: 14px;margin: 0'><b>"+courrier.getTitre()+"</b></p>" +
                courrier.getClasseCourrier().getName()+"</div>");

        courrierTable.addItem(new Object[]{
                courrierLabel, detail
        }, courrier.getId());
    }

    private boolean isForThisUser(Courrier courrier) {
        return !courrier.getEtatCourrier().getName().equals(initialDataLoader.ARCHIVE)
                && isUserHaveThisCourrier(courrier);
    }

    public boolean isUserHaveThisCourrier(Courrier courrier){
        System.out.println("isUserHaveThisCourrier 1");
        boolean result = false;
        for(Person person:courrier.getCorrespondant()){
            if(person.getId() == MailManagementSystemUI.person.getId()){
                result = true;
            }
        }
        return result;
    }

    public HorizontalLayout createBtnLayout() {
        final HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSizeFull();
        horizontalLayout.setMargin(true);
        horizontalLayout.setWidth("50%");
        horizontalLayout.setSpacing(true);
        Button traiter = traiterButton();
        Button archiver = new Button("Marquer comme archivé");
        archiver.addStyleName("wide");

        horizontalLayout.addComponent(traiter);
        horizontalLayout.addComponent(archiver);

        horizontalLayout.setComponentAlignment(traiter, Alignment.BOTTOM_LEFT);
        horizontalLayout.setComponentAlignment(archiver, Alignment.BOTTOM_CENTER);


        archiver.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                updateCourrierEtat(initialDataLoader.ARCHIVE, selectedItemId);
                changeNotification();
                courrierTable.removeItem(selectedItemId);
                horizontalSplitPanel.removeAllComponents();
            }
        });
        return horizontalLayout;
    }

    public Button traiterButton() {
        Button traiter = new Button("Marquer comme traité");
        traiter.addStyleName("wide");
        traiter.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                updateCourrierEtat(initialDataLoader.TRAITE, selectedItemId);
                changeNotification();
                etatLabel.setValue("<div style='font-size:14px'><b>Etat</b>: "+initialDataLoader.TRAITE+"</div>");

            }
        });
        return traiter;
    }

    private void changeNotification() {
        if(notify != null) {
            if(courrierCountForCurrentUser == 1){
                notify.setVisible(false);
            }else {
                notify.setCaption(String.valueOf(courrierCountForCurrentUser-1));
                notify.setVisible(true);
            }
        }
    }

    public Button createSendMailBtn(){
        Button createMail = new Button("Créer un courrier sortant");
        createMail.addStyleName("default");
        createMail.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                Window sendMail = new Window("Création d'un courrier sortant", new EnvoiCourrierCompo());
                sendMail.center();
                sendMail.setWidth("50%");
                sendMail.setModal(true);
                UI.getCurrent().addWindow(sendMail);
            }
        });
        return createMail;
    }

    private void updateCourrierEtat(String valeur, int selectedId) {
        if(selectedItemId !=null){
            try {
                if(!entityManager.getTransaction().isActive())entityManager.getTransaction().begin();
                EtatCourrier etatCourrier = getEtatCourrier(valeur);
                Courrier courrier = getCourrier(selectedItemId);
                courrier.setEtatCourrier(etatCourrier);
                entityManager.persist(courrier);
                entityManager.getTransaction().commit();
                Log.saveLog("Mettre à jout l'etat de courrier "+ courrier.getTitre() + " vers "+ courrier.getEtatCourrier().getName());

            }catch (PersistenceException e){
                entityManager.getTransaction().rollback();
            }
        }
    }

    public EtatCourrier getEtatCourrier(String valeur) {
        String query = "select u from EtatCourrier u where u.name='"+valeur+"'";
        return ((EtatCourrier) entityManager.createQuery(query).getSingleResult());
    }

    private VerticalLayout createCourrierFile(Integer itemId) {
        Courrier courrier = getCourrier(itemId);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSpacing(true);
        verticalLayout.setMargin(true);
        verticalLayout.setSizeFull();
        File file =null;
        CMISDocumentReader cmisDocumentReader = new CMISDocumentReader();
        if(CMISConnector.getAlfrescoSessionFromXML() != null) {
            file = cmisDocumentReader.getFileFromAlfresco(courrier.getDocumentCourrier().getIdAlfresco(), courrier);
            Embedded imageLayout = new Embedded();
            imageLayout.setSizeFull();
            if(courrier.getDocumentCourrier().getType().contains("image")) {
                imageLayout.setSource(new FileResource(file));
                verticalLayout.removeAllComponents();
                verticalLayout.addComponent(imageLayout);
            }else{
                verticalLayout.removeAllComponents();
                final Label label = new Label("<span style='color:red; font-size:24'>Ce document ne correspond pas à une image (la visualisation est'indisponible)<div>");
                verticalLayout.addComponent(label);
                addPrintBtns(verticalLayout, file.getName());
            }
        }else{
             verticalLayout.removeAllComponents();
            final Label label = new Label("<span style='color:red; font-size:24'><b>Erreur: </b>La configuration alfresco est incorrect</span>", ContentMode.HTML);
            verticalLayout.addComponent(label);
        }
        return verticalLayout;
    }

    private void addPrintBtns(VerticalLayout verticalLayout, String fileName) {
        final Button print = new Button("Ouvrir le fichier");
         BrowserWindowOpener opener =
                        new BrowserWindowOpener(VAADIN_THEMES_DASHBOARD_FILES + fileName);
        opener.extend(print);
        print.addStyleName("default");
        verticalLayout.addComponents(print);
        verticalLayout.setComponentAlignment(print, Alignment.MIDDLE_CENTER);
    }

    private VerticalLayout createCourrierDetaiLayout(int id) {
        Courrier courrier = getCourrier(id);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addStyleName("dashboardBackGround");
        verticalLayout.addStyleName("login");
        verticalLayout.setMargin(true);

        Label title = new Label("Détail de Courrier");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        verticalLayout.addComponent(title);
        verticalLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        HorizontalLayout row1 = new HorizontalLayout();
        row1.setSizeFull();
        Label titre = new Label("<div style='font-size:14px'><b>Titre</b>: "+courrier.getTitre()+"</div>", ContentMode.HTML);
        Label reference = new Label("<div style='font-size:14px'><b>Réference</b>: "+courrier.getReference()+"</div>", ContentMode.HTML);
        row1.addComponent(titre);
        row1.addComponent(reference);
        row1.setComponentAlignment(titre, Alignment.MIDDLE_LEFT);
        row1.setComponentAlignment(reference, Alignment.MIDDLE_RIGHT);
        verticalLayout.addComponent(row1);

        HorizontalLayout row2 = new HorizontalLayout();
        row2.setSizeFull();
        Label expediteur = new Label("<div style='font-size:14px'><b>Expéditeur</b>: "+courrier.getContact().getFullName()+"</div>", ContentMode.HTML);
        Label dateDenvoi = new Label("<div style='font-size:14px'><b>Date d'envoie</b>: "+courrier.getDateDEnvoi()+"</div>", ContentMode.HTML);
        row2.addComponent(expediteur);
        row2.addComponent(dateDenvoi);
        row2.setComponentAlignment(expediteur, Alignment.MIDDLE_LEFT);
        row2.setComponentAlignment(dateDenvoi, Alignment.MIDDLE_RIGHT);
        verticalLayout.addComponent(row2);

        HorizontalLayout row3 = new HorizontalLayout();
        row3.setSizeFull();
        Label suivipar = new Label("<div style='font-size:14px'><b>Suivi par</b>: "+courrier.getSuiviPar().getUsername()+"</div>", ContentMode.HTML);
        Label dateDeReception = new Label("<div style='font-size:14px'><b>Date de réception</b>: "+courrier.getDateReception()+"</div>", ContentMode.HTML);
        row3.addComponent(suivipar);
        row3.addComponent(dateDeReception);
        row3.setComponentAlignment(suivipar, Alignment.MIDDLE_LEFT);
        row3.setComponentAlignment(dateDeReception, Alignment.MIDDLE_RIGHT);
        verticalLayout.addComponent(row3);

        HorizontalLayout row4 = new HorizontalLayout();
        row3.setSizeFull();
        Label objet = new Label("<div style='font-size:14px'><b>Objet</b>: "+courrier.getObjet()+"</div>", ContentMode.HTML);
        row4.addComponent(objet);
        row4.setComponentAlignment(objet, Alignment.MIDDLE_LEFT);
        verticalLayout.addComponent(row4);

        HorizontalLayout row5 = new HorizontalLayout();
        row5.setSizeFull();
        Label classe = new Label("<div style='font-size:14px'><b>Classe</b>: "+courrier.getClasseCourrier().getName()+"</div>", ContentMode.HTML);
        Label type = new Label("<div style='font-size:14px'><b>Type</b>: "+courrier.getTypeCourrier().getName()+"</div>", ContentMode.HTML);
        row5.addComponent(classe);
        row5.addComponent(type);
        row5.setComponentAlignment(classe, Alignment.MIDDLE_LEFT);
        row5.setComponentAlignment(type, Alignment.MIDDLE_RIGHT);
        verticalLayout.addComponent(row5);

        HorizontalLayout row6 = new HorizontalLayout();
        row6.setSizeFull();
        etatLabel = new Label("<div style='font-size:14px'><b>Etat</b>: "+courrier.getEtatCourrier().getName()+"</div>", ContentMode.HTML);
        row6.addComponent(etatLabel);
        row6.setComponentAlignment(etatLabel, Alignment.MIDDLE_LEFT);
        verticalLayout.addComponent(row6);
        return verticalLayout;
    }

    private Courrier getCourrier(int id) {
        String query = "select u from Courrier u where u.id='"+id+"'";
        return ((Courrier) entityManager.createQuery(query).getSingleResult());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        courrierTable.requestRepaintAll();
    }

}

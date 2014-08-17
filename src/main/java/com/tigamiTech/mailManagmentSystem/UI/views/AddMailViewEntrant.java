package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.CMIS.CMISConnector;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;
import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.FieldEvents.*;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.TabSheet.Tab;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddMailViewEntrant extends VerticalLayout implements View {

	private static final String GESTION_COURRIER = "gestionCourrier";
	private TabSheet editors;
	
	JPAContainer<ClasseCourrier> classeCourrierContainer = JPAContainerFactory.make(ClasseCourrier.class, GESTION_COURRIER);
	JPAContainer<Person> personContainer = JPAContainerFactory.make(Person.class, GESTION_COURRIER);
	JPAContainer<Contact> contactContainer = JPAContainerFactory.make(Contact.class, GESTION_COURRIER);
	JPAContainer<TypeCourrier> typeCourrierContainer = JPAContainerFactory.make(TypeCourrier.class, GESTION_COURRIER);
    JPAContainer<Groupe> groupeJPAContainer = JPAContainerFactory.make(Groupe.class, GESTION_COURRIER);
    private Button save = new Button("Sauvgrader");
    private final Courrier newCourrier = new Courrier();


    public Courrier getNewCourrier() {
        return newCourrier;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        addStyleName("reports");
        addComponent(buildDraftsView(true));
        try {
            Session alfrescoSessionFromXML = CMISConnector.getAlfrescoSessionFromXML();
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

    public Component buildDraftsView(boolean isEntrant) {
        editors = new TabSheet();
        editors.setSizeFull();
        editors.addStyleName("borderless");
        editors.addStyleName("editors");
        final VerticalLayout center = new VerticalLayout();
        center.setSizeFull();
        center.setCaption("Courriers");
        center.setMargin(true);
        center.setSpacing(true);
        editors.addComponent(center);

        Label draftsTitle = new Label("Liste des courriers");
        draftsTitle.addStyleName("h1");
        draftsTitle.setSizeUndefined();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);
        center.addComponent(horizontalLayout);

        if(isEntrant){
            MailReaderView mailReaderView = new MailReaderView();
            Button create = new Button("Créer un courrier entrant");
            create.addStyleName("default");
            horizontalLayout.addComponent(create);
            horizontalLayout.setComponentAlignment(create, Alignment.MIDDLE_RIGHT);
            create.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    autoCreate();
                }
            });
            VerticalSplitPanel emailEntrantSplitePanel =  mailReaderView.getEmailEntrantSplitePanel(true);
            center.addComponent(emailEntrantSplitePanel);
            center.setExpandRatio(emailEntrantSplitePanel, 9);
        }else{
            MailReaderView mailReaderView1 = new MailReaderView();
            Button sendMailBtn = mailReaderView1.createSendMailBtn();
            horizontalLayout.addComponent(sendMailBtn);
            horizontalLayout.setComponentAlignment(sendMailBtn, Alignment.MIDDLE_RIGHT);
            horizontalLayout.setSpacing(true);
            horizontalLayout.setMargin(true);
            horizontalLayout.addComponent(mailReaderView1.traiterButton());
            VerticalSplitPanel emailSortantSplitePanel =  mailReaderView1.getEmailSortantSplitePanel();
            center.addComponent(emailSortantSplitePanel);
            center.setExpandRatio(emailSortantSplitePanel, 9);
        }


        return editors;
    }


    private HorizontalLayout createEditorInstance() {

        HorizontalLayout editor = new HorizontalLayout();
        editor.setSizeFull();
        final SortableLayout canvas = new SortableLayout();
        canvas.setWidth("100%");
        canvas.addStyleName("canvas");
        editor.addComponent(canvas);
        editor.setExpandRatio(canvas, 7);

        editor.addStyleName("editor");
        editor.addStyleName("no-horizontal-drag-hints");

        return editor;
    }

    public TextField title = new TextField();
    private CheckBox helpCheckbox = new CheckBox("Afficher l'Aide");

    public class SortableLayout extends CustomComponent {
        private VerticalLayout layout;
        public SortableLayout() {
            helpCheckbox.setValue(false);
            setCompositionRoot(layout = new VerticalLayout());
            layout.setSizeFull();
            HorizontalLayout formLayout = new HorizontalLayout();
            formLayout.addStyleName("login-layout");
            formLayout.setMargin(true);
            formLayout.setSizeFull();
            formLayout.setSpacing(true);
            title.addStyleName("title");
           
            title.setValue("Ecrire le titre içi");
            title.addTextChangeListener(new TextChangeListener() {
                @Override
                public void textChange(TextChangeEvent event) {
                    Tab tab = editors.getTab(SortableLayout.this.getParent());
                    String t = event.getText();
                    if (t == null || t.equals("")) {
                        t = " ";
                    }
                    tab.setCaption(t);
                }
            });
            final HorizontalLayout labels = new HorizontalLayout();
            layout.addComponent(title);
            layout.addComponent(labels);
            layout.addComponent(formLayout);

            final CssLayout mailFormPanel = new CssLayout();
            final CssLayout mailUploadPanel = new CssLayout();

            mailFormPanel.addStyleName("login-panel");
            mailUploadPanel.addStyleName("login-panel");
            labels.setWidth("100%");
            labels.addStyleName("login-panel");



           mailFormPanel.setWidth("100%");
           //labels.setMargin(true);
            labels.addStyleName("labels");

            Label information = new Label("Informations d'identification");
            information.setSizeUndefined();
            information.addStyleName("h4");
            labels.addComponent(information);
            labels.setComponentAlignment(information, Alignment.MIDDLE_LEFT);


            labels.addComponent(helpCheckbox);
            labels.setComponentAlignment(helpCheckbox, Alignment.MIDDLE_LEFT);
            Label decorativeSpane = new Label("Gestion de Courrier");
            //  title.setIcon(new ThemeResource("img/logo.png"));
            decorativeSpane.setSizeUndefined();
            decorativeSpane.addStyleName("h2");
            decorativeSpane.addStyleName("light");
            labels.addComponent(decorativeSpane);
            labels.setComponentAlignment(decorativeSpane, Alignment.MIDDLE_RIGHT);

            final AlfrescoReceiver alfrescoReceiver = new AlfrescoReceiver();
            Upload uploadToAlfresco = new Upload("Le fichier électronique du courrier", null);

            uploadToAlfresco.setReceiver(alfrescoReceiver);
            uploadToAlfresco.setImmediate(true);
            uploadToAlfresco.addSucceededListener(alfrescoReceiver);
            uploadToAlfresco.addFailedListener(alfrescoReceiver);
            

            alfrescoReceiver.CourrierScanne.setVisible(false);
            alfrescoReceiver.CourrierScanne.setWidth("100%");
            final HorizontalLayout help = new HorizontalLayout();
            help.setWidth("100%");
            help.addStyleName("labels");

            mailFormPanel.addComponent(help);
            mailUploadPanel.addComponents(uploadToAlfresco, alfrescoReceiver.cheminFichier);
            try {
                mailUploadPanel.addComponent( alfrescoReceiver.CourrierScanne);
            }catch(RuntimeException e){

            }
            final VerticalLayout fields = new VerticalLayout();
            fields.setSpacing(true);
            fields.setMargin(true);
            fields.addStyleName("fields");
            fields.setSizeFull();
           
            final TextField referenceTextField = createTextField("Réference", "un identifiant pour le courrier");
			fields.addComponent(referenceTextField);
            final TextField objetTextField = createTextField("Objet du courrier", "Petit descriptif de l'objet du courrier");
			fields.addComponent(objetTextField);
			final DateField DatedEnvoiDateField = createDateField("Date d'envoi", "Correspond à la date de redaction du courrier par l'éxpediteur");
			fields.addComponent(DatedEnvoiDateField);
            final DateField dateReceptionFiels = createDateField("Date de réception", "Date d'arrivee du courrier");
			fields.addComponent(dateReceptionFiels);

			
			final ComboBox classeComboBox =  createComboBox("Nature de courrier", "Classe courrier");
            classeComboBox.setNullSelectionAllowed(false);
			classeComboBox.setContainerDataSource(classeCourrierContainer);
			classeComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
			classeComboBox.setItemCaptionPropertyId("name");
			fields.addComponent(classeComboBox);
			
			final ComboBox typeComboBox =  createComboBox("Type", "");
            typeComboBox.setNullSelectionAllowed(false);
			typeComboBox.setContainerDataSource(typeCourrierContainer);
			typeComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
			typeComboBox.setItemCaptionPropertyId("name");
			fields.addComponent(typeComboBox);



            final ComboBox groupeComboBox =  createComboBox("Groupe", "direction");
            groupeComboBox.setNullSelectionAllowed(false);
            groupeComboBox.setContainerDataSource(groupeJPAContainer);
            groupeComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
            groupeComboBox.setItemCaptionPropertyId("name");
            fields.addComponent(groupeComboBox);

            final JPAContainer<Person> correspondant = personContainer;
			correspondant.applyFilters();


			final ComboBox correspondantComboBox =  createComboBox("Utilisateur", "un utilisateur de système");
            correspondantComboBox.setNullSelectionAllowed(false);
			correspondantComboBox.setEnabled(false);
            correspondantComboBox.setContainerDataSource(correspondant);
			correspondantComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
			correspondantComboBox.setItemCaptionPropertyId("username");
			fields.addComponent(correspondantComboBox);
            groupeComboBox.addBlurListener(new BlurListener() {
                @Override
                public void blur(BlurEvent blurEvent) {
                    correspondant.removeAllContainerFilters();
                    try {
                        correspondant.addContainerFilter(new Compare.Equal("groupe.name",initialDataLoader.em.find(Groupe.class, groupeComboBox.getValue()).getName()));
                    }catch (IllegalArgumentException e){
                        System.out.println(e.getMessage());
                    }

                    correspondant.addContainerFilter(new Compare.Equal("role.name", "Utilisateur"));
                    correspondantComboBox.setEnabled(true);
                }
            });


			final ComboBox expediteurComboBox =  createComboBox("Coordonnees éxpediteur", "Coordonnées de l'organitation");
            expediteurComboBox.setNullSelectionAllowed(false);
			expediteurComboBox.setContainerDataSource(contactContainer);
			expediteurComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
			expediteurComboBox.setItemCaptionPropertyId("fullName");
			fields.addComponent(expediteurComboBox);

	            save.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent event) {
                        boolean isFormValide = referenceTextField.isValid()
                                && objetTextField.isValid()
                                && DatedEnvoiDateField.isValid()
                                && dateReceptionFiels.isValid()
                                && classeComboBox.isValid()
                                && typeComboBox.isValid()
                                && expediteurComboBox.isValid()
                                && groupeComboBox.isValid()

                        ;
                        if(!isFormValide){
                            Notification notification1 = new Notification("vous devez remplir tous les champs", Notification.Type.ERROR_MESSAGE);

                            notification1.setDelayMsec(5000);
                            notification1.show(UI.getCurrent().getPage());
                        }else  if (CMISConnector.getAlfrescoSessionFromXML() == null) {
                            Notification notification1 = new Notification("<span style='color:red'>La configuration alfresco est incorrecte</span>");
                            notification1.setHtmlContentAllowed(true);

                            notification1.setDelayMsec(5000);

                            notification1.show(UI.getCurrent().getPage());
                        }
                        else {

                            newCourrier.setReference(referenceTextField.getValue());
                            newCourrier.setObjet(objetTextField.getValue());
                            newCourrier.setDateDEnvoi(DatedEnvoiDateField.getValue());
                            newCourrier.setDateReception(dateReceptionFiels.getValue());

                            EntityManager manager = initialDataLoader.em;
                            if (!manager.getTransaction().isActive()) manager.getTransaction().begin();
                            newCourrier.setClasseCourrier(manager.find(ClasseCourrier.class, classeComboBox.getValue()));
                            newCourrier.setTypeCourrier(manager.find(TypeCourrier.class, typeComboBox.getValue()));
                            newCourrier.setContact(manager.find(Contact.class, expediteurComboBox.getValue()));
                            newCourrier.setGroupe(manager.find(Groupe.class, groupeComboBox.getValue()));

                            List<Person> persons = new ArrayList<Person>();
                            List<Person> persons1 = manager.createQuery("select u from Person u").getResultList();
                            if(correspondantComboBox.getValue() != ""){

                                for(Person person : persons1){
                                    if(person.getGroupe().getName().equals(newCourrier.getGroupe().getName())){
                                        persons.add(person);
                                    }
                                }
                            }
                            else{
                                System.out.println(correspondantComboBox.getValue());
                                persons.add(manager.find(Person.class, correspondantComboBox.getValue()));
                            }

                            newCourrier.setCorrespondant(persons);


                            newCourrier.setDateArrivee(new Date());
                            newCourrier.setTitre(title.getValue());

                            alfrescoReceiver.uploadDocumentAfterSave(newCourrier.getGroupe().getName(), true);
                            CourrierDocument documentCourrier = new CourrierDocument();
                            documentCourrier.setIdAlfresco(alfrescoReceiver.getIdAlfresco());
                            documentCourrier.setTitre(title.getValue());
                            documentCourrier.setType(alfrescoReceiver.getMimeType());


                            newCourrier.setDocumentCourrier(documentCourrier);

                            newCourrier.setEtatCourrier(manager.find(EtatCourrier.class,
                                    new MailReaderView().getEtatCourrier(initialDataLoader.RECU).getId()));

                            String query = "select u from Person u where u.username='" + getSession().getCurrent().getAttribute("username") + "'";
                            Person suiviPar = (Person) manager.createQuery(query).getResultList().get(0);
                            newCourrier.setSuiviPar(suiviPar);
                            try {
                                manager.persist(documentCourrier);
                                CourrierDocument.LogDocument(documentCourrier);
                                manager.persist(newCourrier);
                                if(!manager.getTransaction().isActive()) manager.getTransaction().begin();
                                manager.getTransaction().commit();
                                Courrier.logCourrier(new Courrier());
                            } catch (PersistenceException e) {
                                System.out.println(e.getMessage());
                                manager.getTransaction().rollback();
                            }
                        }
                    }
                });
	            save.addStyleName("default");
	            fields.addComponent(save);
	            final ShortcutListener enter = new ShortcutListener("Ajouter", KeyCode.ENTER, null) {
	                @Override
	                public void handleAction(Object sender, Object target) {
	                	save.click();
	                }
	            };
	            save.addShortcutListener(enter);

           
            mailFormPanel.addComponent(fields);

            formLayout.addComponent(mailFormPanel);
            formLayout.setComponentAlignment(mailFormPanel, Alignment.TOP_LEFT);

            formLayout.addComponent(mailUploadPanel);
            formLayout.setComponentAlignment(mailFormPanel, Alignment.TOP_RIGHT);

            mailFormPanel.addComponent(save);
        }

    }

    private CheckBox createCheckBox(String caption, final String doc){
    	return (CheckBox) createField(caption, doc, "checkBox");
    }
    private ComboBox createComboBox(String caption, final String doc){
    	return (ComboBox) createField(caption, doc, "comboBox");
    }
    private TextArea createTextArea(String caption, final String doc){
    	return (TextArea) createField(caption, doc, "textArea");
    }
    
    private DateField createDateField(String caption, final String doc){
    	return (DateField) createField(caption, doc, "date");
    }
    
    private TextField createTextField(String caption, final String doc){
    	return (TextField) createField(caption, doc, "text");
    }
    private AbstractField createField(String caption, final String doc, String type){
    	AbstractField field;
        if (type.equals("text")) {
            field = new TextField(caption);
        }
        else if (type.equals("date")) {
            field = new DateField(caption);

        } else if (type.equals("textArea")) {
            field = new TextArea(caption);

        } else if (type.equals("comboBox")) {
            field = new ComboBox(caption);

        } else if (type.equals("checkBox")) {
            field = new CheckBox(caption);

        } else {
            field = null;

        }
        assert field != null;
        field.setWidth("90%");
    	field.setValidationVisible(true);
    	field.setRequired(true);
    	field.setRequiredError(caption+" est obligatoire ");
    	if(!(field instanceof CheckBox)) ((FocusNotifier) field).addFocusListener(createFocuseListenerForFields(doc));
    	return field;
    }
    Notification notification;
    private FocusListener createFocuseListenerForFields(final String doc){
    		return new FocusListener() {
			@Override
			public void focus(FocusEvent event) {
                notification = new Notification("<b>"+doc+"</b>");
				
				notification.setHtmlContentAllowed(true);
				if(helpCheckbox.getValue()){
					notification.setDelayMsec(5000);
				    notification.show(Page.getCurrent());
				}				
				
			}
		};
    }

    public void autoCreate() {

    		//System.out.println(tab.getConnectorId());
        UI.getCurrent().access(new Runnable() {
            @Override
            public void run() {
                HorizontalLayout tab = createEditorInstance();
                try {
                    editors.addTab(tab).setClosable(true);
                }catch (RuntimeException e){
                    final SortableLayout sortableLayout = new SortableLayout();
                    final Window window = new Window("");
                    window.center();
                    window.setModal(true);
                    window.setContent(sortableLayout);
                    UI.getCurrent().addWindow(window);
                }
            }
        });
        editors.setSelectedTab(editors.getComponentCount() - 1);
    }
}
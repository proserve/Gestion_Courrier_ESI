package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.CMIS.CMISConnector;
import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;
import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.ui.*;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by proserve on 23/06/2014.
 */
public class EnvoiCourrierCompo extends VerticalLayout {
    JPAContainer<ClasseCourrier> classeCourrierContainer = JPAContainerFactory.make(ClasseCourrier.class, initialDataLoader.PERSISTENCE_UNIT_NAME);
    JPAContainer<Person> personContainer = JPAContainerFactory.make(Person.class, initialDataLoader.PERSISTENCE_UNIT_NAME);
    JPAContainer<Contact> contactContainer = JPAContainerFactory.make(Contact.class, initialDataLoader.PERSISTENCE_UNIT_NAME);
    JPAContainer<TypeCourrier> typeCourrierContainer = JPAContainerFactory.make(TypeCourrier.class, initialDataLoader.PERSISTENCE_UNIT_NAME);
    private final DateField datedEnvoiDateField;
    private final ComboBox expediteurComboBox;

    public EnvoiCourrierCompo(){
        final CourrierDepart newCourrier = new CourrierDepart();
        final CssLayout mailFormPanel = new CssLayout();
        addComponent(mailFormPanel);
        mailFormPanel.addStyleName("login-panel");
        mailFormPanel.setWidth("100%");
        final VerticalLayout fields = new VerticalLayout();
        mailFormPanel.addComponent(fields);
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");
        fields.setSizeFull();
        final TextField title = createTextField("Titre", "");
        fields.addComponent(title);
        final TextField referenceTextField = createTextField("Réference", "");
        fields.addComponent(referenceTextField);
        final TextField objetTextField = createTextField("Objet du courrier", "Petit descriptif de leobjet du courrier");
        fields.addComponent(objetTextField);
        datedEnvoiDateField = createDateField("Date d'envoi", "Correspond é la date de redaction du courrier par leexpediteur");
        fields.addComponent(datedEnvoiDateField);

        final ComboBox classeComboBox =  createComboBox("Nature de courrier", "");
        classeComboBox.setContainerDataSource(classeCourrierContainer);
        classeComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        classeComboBox.setItemCaptionPropertyId("name");
        fields.addComponent(classeComboBox);

        final ComboBox typeComboBox =  createComboBox("Type", "gggggggg");
        typeComboBox.setContainerDataSource(typeCourrierContainer);
        typeComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        typeComboBox.setItemCaptionPropertyId("name");
        fields.addComponent(typeComboBox);

        expediteurComboBox = createComboBox("Déstinataire", "ggggggggggggggg");
        expediteurComboBox.setContainerDataSource(contactContainer);
        expediteurComboBox.setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY);
        expediteurComboBox.setItemCaptionPropertyId("fullName");
        fields.addComponent(expediteurComboBox);
        HorizontalLayout lastLine = new HorizontalLayout();
        final AlfrescoReceiver alfrescoReceiver = new AlfrescoReceiver();
        Upload uploadToAlfresco = new Upload("Le fichier electronique du courrier             ", null);
        uploadToAlfresco.setReceiver(alfrescoReceiver);
        uploadToAlfresco.setImmediate(true);
        uploadToAlfresco.addSucceededListener(alfrescoReceiver);
        uploadToAlfresco.addFailedListener(alfrescoReceiver);



        Button envoyer = new Button("Envoyer");
        lastLine.addComponent(uploadToAlfresco);
        lastLine.addComponent(alfrescoReceiver.cheminFichier);
        envoyer.addStyleName("default");
        lastLine.addComponent(envoyer);
        lastLine.setSizeFull();
        lastLine.setComponentAlignment(envoyer, Alignment.BOTTOM_RIGHT);
        mailFormPanel.addComponent(lastLine);
        envoyer.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (CMISConnector.getAlfrescoSessionFromXML() == null) {
                    Notification notification1 = new Notification("La configuration alfresco est incorrecte");
                    notification1.setDelayMsec(10000);
                    notification1.show(UI.getCurrent().getPage());
                } else {
                    newCourrier.setReference(referenceTextField.getValue());
                    newCourrier.setObjet(objetTextField.getValue());
                    newCourrier.setDateDEnvoi(datedEnvoiDateField.getValue());

                    EntityManager manager = initialDataLoader.em;
                    if(!manager.getTransaction().isActive()) manager.getTransaction().begin();
                    newCourrier.setClasseCourrier((ClasseCourrier) manager.find(ClasseCourrier.class, classeComboBox.getValue()));
                    newCourrier.setOrientation((OrientationResponsable) manager.createQuery(
                            "select u from OrientationResponsable u where u.name='" + initialDataLoader.AUCUNE + "'").getSingleResult());
                    newCourrier.setTypeCourrier((TypeCourrier) manager.find(TypeCourrier.class, typeComboBox.getValue()));
                    newCourrier.setContact((Contact) manager.find(Contact.class, expediteurComboBox.getValue()));
                    List<Person> persons = new ArrayList<Person>();
                    final Person person = manager.find(Person.class, MailManagementSystemUI.person.getId());
                    persons.add(person);
                    newCourrier.setCorrespondant(persons);

                    newCourrier.setDateArrivee(new Date());
                    newCourrier.setTitre(title.getValue());

                    alfrescoReceiver.uploadDocumentAfterSave("", false);
                    CourrierDocument documentCourrier = new CourrierDocument();
                    documentCourrier.setIdAlfresco(alfrescoReceiver.getIdAlfresco());
                    documentCourrier.setTitre(title.getValue());
                    documentCourrier.setType(alfrescoReceiver.getMimeType());


                    newCourrier.setDocumentCourrier(documentCourrier);
                    EtatCourrier etatCourrier = new EtatCourrier();
                    etatCourrier.setName(initialDataLoader.ENVOYE);
                    etatCourrier.setDescription("courrier envoyé");
                    newCourrier.setEtatCourrier(manager.find(EtatCourrier.class, new MailReaderView().getEtatCourrier(initialDataLoader.ENVOYE).getId()));

                    String query = "select u from Person u where u.username='" + getSession().getCurrent().getAttribute("username") + "'";
                    Person suiviPar = (Person) manager.createQuery(query).getResultList().get(0);
                    newCourrier.setSuiviPar(suiviPar);
                    try {
                        manager.persist(documentCourrier);
                        manager.persist(newCourrier);
                        CourrierDocument.LogDocument(documentCourrier);
                        Courrier.logCourrier(newCourrier);
                        manager.getTransaction().commit();
                    } catch (Exception e) {

                        manager.getTransaction().rollback();
                    }
                }
            }
        });

    }
    private CheckBox createCheckBox(String caption, final String doc){
        return (CheckBox) createField(caption,  "checkBox");
    }
    private ComboBox createComboBox(String caption, final String doc){
        return (ComboBox) createField(caption,  "comboBox");
    }
    private TextArea createTextArea(String caption, final String doc){
        return (TextArea) createField(caption,  "textArea");
    }

    private DateField createDateField(String caption, final String doc){
        return (DateField) createField(caption,  "date");
    }

    private TextField createTextField(String caption, final String doc){
        return (TextField) createField(caption, "text");
    }
    private AbstractField createField(String caption, String type){
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
        return field;
    }
}

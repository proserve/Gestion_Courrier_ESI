package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.Groupe;
import com.tigamiTech.mailManagmentSystem.domaine.Log;
import com.tigamiTech.mailManagmentSystem.domaine.Role;
import com.tigamiTech.mailManagmentSystem.domaine.TableConfigurable;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.FieldFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.data.Item;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class EntityCRUD extends Window implements Button.ClickListener,
        FormFieldFactory {

	TableConfigurable tableConfigurable;
    private final Item item;
    private Form editorForm;
    private Button saveButton;
    private Button cancelButton;
    private final Label errorLabel = new Label("", ContentMode.HTML);

    public EntityCRUD(Item personItem, TableConfigurable configurable) {
    	this.center();
    	this.tableConfigurable = configurable;
        this.item = personItem;
        this.setResizable(true);
        this.setModal(true);

        editorForm = new Form();
        editorForm.addStyleName("login");
        editorForm.setSizeFull();

        editorForm.setFormFieldFactory(this);
        editorForm.setBuffered(true);
        editorForm.setImmediate(true);
        editorForm.setItemDataSource(item, Arrays.asList(tableConfigurable.getColumnsInEditorView()));
        editorForm.getFooter().addComponent(errorLabel);
        saveButton = new Button("Sauvgarder", this);
        cancelButton = new Button("Annuler", this);
        HorizontalLayout horizontalLayout =new HorizontalLayout();
        horizontalLayout.addComponent(saveButton);
        horizontalLayout.addComponent(cancelButton);

        horizontalLayout.setSizeFull();
        horizontalLayout.setSpacing(true);
        horizontalLayout.setMargin(true);
        editorForm.getFooter().addComponent(horizontalLayout);
        setSizeUndefined();


        VerticalLayout verticalLayout =new VerticalLayout();
        verticalLayout.setMargin(true);
        verticalLayout.setSpacing(true);
        verticalLayout.addComponent(editorForm);
        verticalLayout.setSizeUndefined();
        setContent(verticalLayout);
        setCaption(buildCaption());
    }

    private String buildCaption() {
        return tableConfigurable.getCaption();
    }

    @Override
    public void buttonClick(ClickEvent event) {
        if (event.getButton() == saveButton) {
            if(editorForm.isValid()){
                editorForm.commit();
                fireEvent(new EditorSavedEvent(this, item));
                if (EntityEditor.isEdting) Log.saveLog("modifier " + tableConfigurable.getDetail());
                else Log.saveLog("ajouter " + tableConfigurable.getDetail());
            }else{

                errorLabel.setValue("");
                errorLabel.setValue("<div style='color:red'> les champs avec * sont obligatoires</div>");
                throw new NotValideException();
            }
            System.out.println(editorForm.isValid());
        } else if (event.getButton() == cancelButton) {
            editorForm.discard();
        }
        close();
    }
    private <T> NativeSelect createNativeSelect(Object propertyId, Class<T> t){
    	NativeSelect nativeSelect = new NativeSelect();
        nativeSelect.setNullSelectionAllowed(false);
        nativeSelect.setItemCaptionMode(NativeSelect.ITEM_CAPTION_MODE_ITEM);
        JPAContainer<T> container = JPAContainerFactory.make(t, "gestionCourrier");
        nativeSelect.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
		nativeSelect.setContainerDataSource(container );
        nativeSelect.setConverter(new SingleSelectConverter(nativeSelect));
        return nativeSelect;
    }
    @Override
    public Field createField(Item item, Object propertyId, Component uiContext) {
        System.out.println(propertyId);
        Field field = new FieldFactory().createField(item, propertyId, uiContext);
        if ("role".equals(propertyId)) {
            field =  createNativeSelect(propertyId, Role.class);
        }else if("groupe".equals(propertyId)){
        	field =  createNativeSelect(propertyId, Groupe.class);
        }else if("color".equals(propertyId)){
            NativeSelect nativeSelect = new NativeSelect("choisir un couleur");
            nativeSelect.addItem("red");
            nativeSelect.addItem("blue");
            nativeSelect.addItem("black");
            nativeSelect.addItem("blueviolet");
            nativeSelect.addItem("chocolate");
            nativeSelect.addItem("green");
            nativeSelect.addItem("yellow");
            nativeSelect.addItem("orange");

            field = nativeSelect;
         }else if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
        }
        field.setRequired(true);
        field.setRequiredError("ce champs ne doit pas être vide");
       //teditorForm.se
      // field.addValidator(new BeanValidator(Person.class, propertyId.toString()));
        return field;
    }

    public void addListener(EditorSavedListener listener) {
        try {
            Method method = EditorSavedListener.class.getDeclaredMethod(
                    "editorSaved", new Class[] { EditorSavedEvent.class });
            addListener(EditorSavedEvent.class, listener, method);
        } catch (final NoSuchMethodException e) {
            // This should never happen
            throw new RuntimeException(
                    "Internal error, editor saved method not found");
        }
    }


    public static class EditorSavedEvent extends Component.Event {

        private Item savedItem;

        public EditorSavedEvent(Component source, Item savedItem) {
            super(source);
            this.savedItem = savedItem;
        }

    }

    public interface EditorSavedListener extends Serializable {
        public void editorSaved(EditorSavedEvent event);
    }

    private class NotValideException extends RuntimeException {
    }
}

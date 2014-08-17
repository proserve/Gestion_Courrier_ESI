package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;

public class UserView extends VerticalLayout implements View {

    public UserView() {
        setSizeFull();
        setMargin(true);
        addStyleName("dashboard-view");
        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("1000px");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);
        addComponent(verticalSplitPanel);
        verticalSplitPanel.setSizeFull();
        final Label title = new Label("Page d'administration");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);

        Label decorativeSpane = new Label("Gestion de Courrier");
        decorativeSpane.setSizeUndefined();
        decorativeSpane.addStyleName("h2");
        decorativeSpane.addStyleName("light");
        top.addComponent(decorativeSpane);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        top.setComponentAlignment(decorativeSpane, Alignment.MIDDLE_RIGHT);
        JPAContainer<Courrier> jpaContainer = JPAContainerFactory.make(Courrier.class, "gestionCourrier");

        jpaContainer.addContainerFilter(new Compare.Equal("Correspondant.username", "zaki"));
        EntityEditor courrierEntityEditor = new EntityEditor(new Courrier());
        final Courrier courrier = new Courrier();


        CssLayout courrierPanel = courrierEntityEditor.createEditPanel();

        verticalSplitPanel.addComponent(courrierPanel);
        final HorizontalSplitPanel editSplitPanel = new HorizontalSplitPanel();
        verticalSplitPanel.addComponent(editSplitPanel);
        EntityEditor etaEditor = new EntityEditor(new EtatCourrier());
        etaEditor.getTable().setSelectable(false);
        etaEditor.getTable().setStyleName("plain");
        courrierEntityEditor.getTable().addListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
               /* courrier.setTitre(String.valueOf(itemClickEvent.getItem().getItemProperty("titre")));
                courrier.setReference(String.valueOf(itemClickEvent.getItem().getItemProperty("reference")));
                System.out.println(itemClickEvent.getItem().getItemProperty("classeCourrier"));
                editSplitPanel.removeAllComponents();
                editSplitPanel.addComponent(getEditView(itemClickEvent.getItem(), null));*/

            }
        });
        setExpandRatio(verticalSplitPanel, 8);
        setExpandRatio(top, 1);
    }

    private CssLayout getEditView(Item item, Courrier courrier) {
        CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();
        Form form = new Form();

        form.setItemDataSource(item);/*
        FormFieldFactory fieldFactory = new FormFieldFactory() {
            private <T> NativeSelect createNativeSelect(Object propertyId, Class<T> t) {
                NativeSelect nativeSelect = new NativeSelect();
                nativeSelect.setItemCaptionMode(NativeSelect.ITEM_CAPTION_MODE_ITEM);
                JPAContainer<T> container = JPAContainerFactory.make(t, "gestionCourrier");
                nativeSelect.setCaption(DefaultFieldFactory.createCaptionByPropertyId(propertyId));
                nativeSelect.setContainerDataSource(container);
                nativeSelect.setConverter(new SingleSelectConverter(nativeSelect));
                return nativeSelect;
            }

            @Override
            public Field<?> createField(Item item, Object o, Component component) {
                Field field = new FieldFactory().createField(item, o, component);

                if ("classeCourrier".equals(o)) {
                    field = createNativeSelect(o, ClasseCourrier.class);
                }else if ("typeCourrier".equals(o)) {
                    field = createNativeSelect(o, TypeCourrier.class);
                }else if ("etatCourrier".equals(o)) {
                    field = createNativeSelect(o, EtatCourrier.class);
                }else if ("suiviPar".equals(o) || "Correspondant".equals(o)) {
                    field = createNativeSelect(o, Person.class);
                }else if ("contact".equals(o)) {
                    field = createNativeSelect(o, Contact.class);
                }else if ("documentCourrier".equals(o)) {
                    field = createNativeSelect(o, CourrierDocument.class);
                }else if ("dossier".equals(o)) {
                    field = createNativeSelect(o, Dossier.class);
                }else if (field instanceof TextField) {
                    ((TextField) field).setNullRepresentation("");
                }

                return field;
            }

            ;
        };
        */
        FormFieldFactory fieldFactory = new EntityCRUD(item, courrier);
        form.setFormFieldFactory(fieldFactory);
        panel.addComponent(form);
        return panel;

        /*VerticalLayout editPanel = new VerticalLayout();
        editPanel.addComponent(caption);
        editPanel.addComponent(reference);
        return  editPanel;*/
    }

    @Override
    public void enter(ViewChangeEvent event) {

    }
}

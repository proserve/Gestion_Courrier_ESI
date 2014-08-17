package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.*;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

/**
 * Created by proserve on 22/06/2014.
 */
public class LogView extends VerticalLayout implements View {

    private final EntityEditor logEditor = new EntityEditor(new Log());
    private final Table logTable =logEditor.getTable();
    private JPAContainer<TableConfigurable> logEditorItems =logEditor.getItems();;


    public LogView() {
        setSizeFull();
       // addStyleName("dashboard-view");

        addStyleName("login");
        addStyleName("dashboardBackGround");
        setSizeFull();
        setMargin(true);

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);

        final Label title = new Label("Traçabilité");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);

        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        addComponent(top);

        logTable.setSelectable(true);
        logTable.setStyleName("plain");
        CssLayout logPanel = logEditor.createNoEditPanel();
        addComponent(top);
        HorizontalLayout row = createRow();
        HorizontalLayout filterRow = createRow();
        TextField fUser = new TextField("Filter par utilisateur");
        TextField fRole = new TextField("Filter par role");
        TextField fAction = new TextField("Filter par action");

        fUser.addTextChangeListener(createTextChangeFilter("username"));
        fRole.addTextChangeListener(createTextChangeFilter("role"));
        fAction.addTextChangeListener(createTextChangeFilter("detail"));
        filterRow.setSizeFull();
        filterRow.setMargin(true);
        filterRow.setSpacing(true);
        filterRow.addComponent(fUser);
        filterRow.addComponent(fRole);
        filterRow.addComponent(fAction);

        addComponent(filterRow);
        row.addComponent(logPanel);
        addComponent(row);

        row.setSizeFull();
        setExpandRatio(row, 9);
        setExpandRatio(filterRow, 1);
        setExpandRatio(top, 1);
    }

    private FieldEvents.TextChangeListener createTextChangeFilter(final String property) {
        return new FieldEvents.TextChangeListener() {
            @Override
            public void textChange(FieldEvents.TextChangeEvent textChangeEvent) {
                logEditorItems.removeAllContainerFilters();
                logEditorItems.addContainerFilter(new SimpleStringFilter(property, textChangeEvent.getText(), true, false));
            }
        };
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
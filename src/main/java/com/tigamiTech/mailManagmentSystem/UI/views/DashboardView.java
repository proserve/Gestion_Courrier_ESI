package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.Groupe;
import com.tigamiTech.mailManagmentSystem.domaine.Person;
import com.tigamiTech.mailManagmentSystem.domaine.Role;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;

public class DashboardView extends VerticalLayout implements View {


    private final EntityEditor personEntityEditor = new EntityEditor(new Person());
    private final EntityEditor groupeEntityEditor = new EntityEditor(new Groupe());

    public DashboardView() {
        setSizeFull();
        addStyleName("dashboard-view");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);

        final Label title = new Label("Panneau d'administration");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);

        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);

        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();
        addComponent(verticalSplitPanel);
        verticalSplitPanel.setSizeFull();


        HorizontalLayout userRow = createRow();
        HorizontalLayout userConfigRow = createRow();
        verticalSplitPanel.addComponent(userRow);        
        verticalSplitPanel.addComponent(userConfigRow);

        CssLayout userPanel = personEntityEditor.createEditPanel();

        CssLayout groupePanel = groupeEntityEditor.createEditPanel();
        
		EntityEditor roleEditor = new EntityEditor(new Role());
		roleEditor.getTable().setSelectable(false);
		roleEditor.getTable().setStyleName("plain");
		CssLayout rolePanel = roleEditor.createNoEditPanel();
		  
		userRow.addComponent(userPanel);
        userConfigRow.addComponent(groupePanel);
        userConfigRow.addComponent(rolePanel);
        setExpandRatio(top, 1);
        setExpandRatio(verticalSplitPanel, 8);
       // verticalSplitPanel.set
    }

	private HorizontalLayout createRow() {
		HorizontalLayout row = new HorizontalLayout();
        row.setMargin(true);
        row.setSizeFull();
        row.setSpacing(true);
		return row;
	}

	@Override
	public void enter(ViewChangeEvent event) {

        setSizeFull();
        personEntityEditor.getTable().requestRepaintAll();
        groupeEntityEditor.getTable().requestRepaintAll();

    }
}


package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.domaine.ClasseCourrier;
import com.tigamiTech.mailManagmentSystem.domaine.Contact;
import com.tigamiTech.mailManagmentSystem.domaine.EtatCourrier;
import com.tigamiTech.mailManagmentSystem.domaine.TypeCourrier;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.*;

public class AdminCourrierView extends VerticalLayout implements View {

	 public AdminCourrierView() {
		 setSizeFull();
	        setMargin(true);
	        addStyleName("dashboard-view");
	        VerticalSplitPanel verticalSplitPanel = new VerticalSplitPanel();

	        HorizontalSplitPanel horizontalSplitPanelTop = new HorizontalSplitPanel();
	        horizontalSplitPanelTop.setSizeFull();

	        HorizontalSplitPanel horizontalSplitPanelButtom = new HorizontalSplitPanel();
	        horizontalSplitPanelButtom.setSizeFull();

	        HorizontalLayout top = new HorizontalLayout();
	        top.setWidth("1000px");
	        top.setSpacing(true);
	        top.addStyleName("toolbar");
	        addComponent(top);
	        addComponent(verticalSplitPanel);
	        verticalSplitPanel.setSizeFull();
	        final Label title = new Label("Administration des courriers");
	        title.setSizeUndefined();
	        title.addStyleName("h1");
	        top.addComponent(title);

	        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);



	        CssLayout classCourrierPanel = new EntityEditor(new ClasseCourrier()).createEditPanel();
	        CssLayout contactPanel =new EntityEditor(new Contact()).createEditPanel();
	        CssLayout typePanel =new EntityEditor(new TypeCourrier()).createEditPanel();

			EntityEditor etatEditor = new EntityEditor(new EtatCourrier());
			etatEditor.getTable().setSelectable(false);
			etatEditor.getTable().setStyleName("plain");

			horizontalSplitPanelTop.addComponent(classCourrierPanel);

			horizontalSplitPanelButtom.addComponent(typePanel);
			horizontalSplitPanelButtom.addComponent(contactPanel);

         verticalSplitPanel.addComponent(classCourrierPanel);
         verticalSplitPanel.addComponent(horizontalSplitPanelButtom);
	        setExpandRatio(verticalSplitPanel, 8);
	        setExpandRatio(top, 1);

	    }

		@Override
		public void enter(ViewChangeEvent event) {
			// TODO Auto-generated method stub
			
		}
}

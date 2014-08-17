package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.UI.views.EntityCRUD.EditorSavedEvent;
import com.tigamiTech.mailManagmentSystem.UI.views.EntityCRUD.EditorSavedListener;
import com.tigamiTech.mailManagmentSystem.domaine.TableConfigurable;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.addon.tableexport.TableExport;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.RowHeaderMode;

public class EntityEditor{
    public static boolean isEdting = true;
	private TableConfigurable tableConfigurable;
	private JPAContainer<TableConfigurable> items;

    public JPAContainer<TableConfigurable> getItems() {
        return items;
    }

    private Table table;
	private Button editBtn = new Button("Modifier");
    private Button deleteBtn = new Button("Supprimer");
    private Button addBtn = new Button("Ajouter");
    private Button export = new Button();
    private HorizontalLayout toolBar = new HorizontalLayout();

    public EntityEditor(final TableConfigurable tableConfigurable) {
		this.tableConfigurable = tableConfigurable;
		this.items = (JPAContainer<TableConfigurable>) JPAContainerFactory.make(tableConfigurable.getClass(), "gestionCourrier");
		
		addNestedProperties(tableConfigurable, items);
		this.table = createJPATable(tableConfigurable);
		table.setImmediate(true);
	
		addClickListenerToAddButton();
		addClickListenerToEditButton();
		addClickListenerToDeleteButton();

		 table.addActionHandler(new Handler() {

	            private Action excel = new Action("Exporter ce tableau vers Excel");

	            private Action CVS = new Action("Exporter ce tableau vers CVS");


	            @Override
	            public void handleAction(Action action, Object sender, Object target) {
	            	  TableExport excelTableExport;
	                if (action == excel) {
	                   excelTableExport =  new ExcelExport(table);
	                   excelTableExport.export();
	                } else if (action == CVS) {
	                   excelTableExport = new CsvExport(table);
	                   excelTableExport.export();
	                }else{

	                }
	                
	            }

	            @Override
	            public Action[] getActions(Object target, Object sender) {
	                return new Action[] { excel, CVS };
	            }
	        });
	}
	public CssLayout createNoEditPanel(){
   	 CssLayout panel = createPanel();
   	 panel.addComponent(table);
       return panel;
   }
	public CssLayout createEditPanel() {
        CssLayout panel = createPanel();
        
        settingExportBtn();
        settingTollbar();
        
        panel.addComponent(toolBar);
        panel.addComponent(export);
        panel.addComponent(table);
        return panel;
    }
	private void addClickListenerToAddButton(){
		addBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				
				addButtonClickHandler();
				
			}
			
			private void addButtonClickHandler() {
                isEdting = false;
				final BeanItem<TableConfigurable> newPersonItem = new BeanItem<TableConfigurable>(
						tableConfigurable);
                createEditWindow(newPersonItem);
                items.commit();
				
				}
		});
	}
	private void addClickListenerToEditButton(){
		editBtn.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				addButtonClickHandler();
			}
			
			private void addButtonClickHandler() {
                isEdting = true;
                if(table.getValue() !=null){
                    Item tableItem = items.getItem(table.getValue());
                    UI.getCurrent().addWindow(new EntityCRUD(tableItem, tableConfigurable));
                    table.requestRepaint();
                    items.commit();
                }


				}
		});
	}
	private void addClickListenerToDeleteButton(){
		deleteBtn.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                addButtonClickHandler();
            }

            private void addButtonClickHandler() {
                final Window window = new Window("Vous voullez vraiment supprimer cet enregistrement");
                window.center();
                HorizontalLayout horizontalLayout = new HorizontalLayout();
                Button oui = new Button("OUI");
                oui.addStyleName("default");

                horizontalLayout.addComponent(oui);

                final Button non = new Button("Non");
                horizontalLayout.addComponent(non);
                horizontalLayout.setSpacing(true);
                horizontalLayout.setMargin(true);
                oui.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        Item tableItem = items.getItem(table.getValue());
                        items.removeItem(table.getValue());
                        items.commit();
                        window.close();
                    }
                });

                non.addClickListener(new ClickListener() {
                    @Override
                    public void buttonClick(ClickEvent clickEvent) {
                        window.close();
                    }
                });
                window.setContent(horizontalLayout);

                UI.getCurrent().addWindow(window);

            }
        });
	}
	private void settingTollbar() {
		toolBar.addStyleName("toolbar");
        toolBar.setSpacing(true);
        toolBar.addComponent(addBtn);
        toolBar.addComponent(editBtn);
        toolBar.addComponent(deleteBtn);
	}
	private void settingExportBtn() {
		export.addStyleName("configure");
        export.addStyleName("icon-cog");
        export.addStyleName("icon-only");
        export.addStyleName("borderless");
        export.setDescription("Export");
        export.addStyleName("small");
       
        export.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
               final TableExport excelTableExport = new ExcelExport(table);
               final TableExport CSVTableExport = new CsvExport(table);
               excelTableExport.export();
            }
        });
	}
	
	private Table createJPATable(TableConfigurable configurable) {
		Table table = new Table(configurable.getCaption(), items);
		if(configurable.getVisibleColumns() != null) configureTableColumns(table, configurable);
        styleTable(table);   
		return table;
	}
	
	private void styleTable(Table table) {
        table.setWidth("100%");
        table.addStyleName("borderless");
        table.setSortEnabled(true);
        table.setSelectable(true);
        table.setRowHeaderMode(RowHeaderMode.INDEX);
        table.setImmediate(true);
	}
	private void configureTableColumns(Table table, TableConfigurable  configurable){
		table.setVisibleColumns(configurable.getVisibleColumns());
        table.setColumnHeaders(configurable.getColumnHeaders());   
	}

	private void addNestedProperties(TableConfigurable t,
			JPAContainer<? extends TableConfigurable> items) {
		for (String property : t.getNestedProperties()) {
			items.addNestedContainerProperty(property);
		}
	}
	
    private CssLayout createPanel(){
    	CssLayout panel = new CssLayout();
        panel.addStyleName("layout-panel");
        panel.setSizeFull();
        return panel;
    }
	public Table getTable() {
		return table;
	}
	private void createEditWindow(
			final Item newPersonItem) {
		EntityCRUD editor = new EntityCRUD(newPersonItem, tableConfigurable);
		editor.addListener(new EditorSavedListener() {
		    @Override
		    public void editorSaved(EditorSavedEvent event) {
                    items.addEntity(((BeanItem<TableConfigurable>) newPersonItem).getBean());
		    }
		});
		UI.getCurrent().addWindow(editor);
	}
    
	
	
}

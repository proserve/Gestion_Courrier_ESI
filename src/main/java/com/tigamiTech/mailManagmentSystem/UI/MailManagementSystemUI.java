package com.tigamiTech.mailManagmentSystem.UI;

import com.tigamiTech.mailManagmentSystem.UI.views.*;
import com.tigamiTech.mailManagmentSystem.configuration.ConfigManager;
import com.tigamiTech.mailManagmentSystem.configuration.DatabaseConfig;
import com.tigamiTech.mailManagmentSystem.domaine.AddMailViewSortant;
import com.tigamiTech.mailManagmentSystem.domaine.Log;
import com.tigamiTech.mailManagmentSystem.domaine.Person;
import com.tigamiTech.mailManagmentSystem.services.AuthenticationService;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.servlet.annotation.WebServlet;
import java.util.*;

@SuppressWarnings("serial")
@Theme("dashboard")
@PreserveOnRefresh
public class MailManagementSystemUI extends UI {
    private static final long serialVersionUID = 1L;
    public static final String IMG_PATH = "img/LOGO-ESI.png";
    public static final String COURRIERS_ENTRANTS = "/Courriers_entrants";
    public static final String ENTRANTS = "/Entrants";
    CssLayout root = new CssLayout();
    VerticalLayout DatabaseLayout;

    CssLayout menu = new CssLayout();
    CssLayout content = new CssLayout();
    public static Person person;
    HashMap<String, Class<? extends View>> routes = new HashMap<String, Class<? extends View>>();

    HashMap<String, Button> viewNameToMenuButton = new HashMap<String, Button>();
    List<String> routeNames = new ArrayList<String>();
    private Navigator nav;

    boolean isFirstTime = true;
    @WebServlet(value = "/*", asyncSupported = false)
	@VaadinServletConfiguration(productionMode = true, ui = MailManagementSystemUI.class)
	public static class Servlet extends VaadinServlet {

    }

	@Override
	protected void init(VaadinRequest request) {


       setContent(root);
        root.addStyleName("root");
        root.setSizeFull();
        Image c = new Image(null, new ThemeResource(IMG_PATH));
        c.setHeight("70px");
        c.addStyleName("image-logo");
        root.addComponent(c);
        Label bg = new Label();
        bg.setSizeUndefined();
        bg.addStyleName("login-bg");
        root.addComponent(bg);
		try{
               Persistence.createEntityManagerFactory(initialDataLoader.PERSISTENCE_UNIT_NAME,
                    DatabaseConfig.getInstance().getDatabaseConfigProperties()).createEntityManager();
            buildLoginView(false);
        }catch (PersistenceException e){
            buildConfigurationView();
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void setPerson(){
		String username =  getSession().getAttribute("username").toString();
    	EntityManager entityManager = initialDataLoader.em;
		String query = "select u from Person u where u.username='"+username+"'";
		person = ((Person) entityManager.createQuery(query).getSingleResult());
	}
    private void initRoots() {
    	setPerson();
        if (person.getRole().getName().equals(initialDataLoader.ADMINISTRATEUR)) {
            routes.put("/administration", DashboardView.class);
            routes.put("/adminCourrier", AdminCourrierView.class);
            routes.put("/configuration", ConfigurationView.class);
            routes.put("/trace", LogView.class);
            routeNames.add("administration");
            routeNames.add("adminCourrier");
            routeNames.add("configuration");
            routeNames.add("trace");

        } else if (person.getRole().getName().equals(initialDataLoader.SECRITAIRIAT)) {
            routes.put("/Entrants", AddMailViewEntrant.class);
            routes.put("/Sortants", AddMailViewSortant.class);
            routeNames.add("Entrants");
            routeNames.add("Sortants");
        } else if (person.getRole().getName().equals(initialDataLoader.UTILISATEUR)) {
            routes.put("/Courriers_entrants", MailReaderView.class);
            routes.put("/Courriers_sortrants", MailSendReaderUserView.class);
            routeNames.add("Courriers_entrants");
            routeNames.add("Courriers_sortrants");
        }
	}

	private void buildConfigurationView() {
        addStyleName("login");
        DatabaseLayout = new VerticalLayout();
        DatabaseLayout.setSizeFull();
        DatabaseLayout.addStyleName("login-layout");
        root.addComponent(DatabaseLayout);
        final CssLayout configPanel = getDatabaseConfigComponents(true);
        DatabaseLayout.addComponent(configPanel);
        DatabaseLayout.setComponentAlignment(configPanel, Alignment.MIDDLE_CENTER);
    }

    public CssLayout getDatabaseConfigComponents(final boolean isMainView) {
        final CssLayout configPanel = new CssLayout();
        configPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        configPanel.addComponent(labels);

        Label welcome = new Label("configurer la base de données Postgres");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        ConfigManager config = ConfigManager.getInstance();
        VerticalLayout fields = new VerticalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Nom d'utilisateur");
        username.setValue(config.getConfiguration("username"));
        username.focus();
        fields.addComponent(username);
        final PasswordField password = new PasswordField("Mot de passe");
        password.setValue(config.getConfiguration("password"));
        fields.addComponent(password);

        final TextField host = new TextField("Host");
        host.setValue(config.getConfiguration("host"));
        fields.addComponent(host);

        final TextField port = new TextField("Port");
        port.setValue(config.getConfiguration("port"));
        fields.addComponent(port);

        final TextField databaseName = new TextField("Nom de la base de données");
        databaseName.setValue(config.getConfiguration("databaseName"));
        fields.addComponent(databaseName);
        databaseName.setRequired(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        fields.addComponent(buttonLayout);
        buttonLayout.setSpacing(true);

        final Label testResponseMessage = new Label("");
        fields.addComponent(testResponseMessage);

        final Button tester = new Button("Tester");
        tester.addStyleName("default");
        buttonLayout.addComponent(tester);
        buttonLayout.setComponentAlignment(tester, Alignment.BOTTOM_LEFT);

        final Button valider = new Button("Valider");
        valider.addStyleName("default");
        buttonLayout.addComponent(valider);
        buttonLayout.setComponentAlignment(valider, Alignment.BOTTOM_RIGHT);


        final ShortcutListener enter = new ShortcutListener("Sign In",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                valider.click();
            }
        };
        final Properties properties = new Properties();

        tester.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                testConnection(properties, host, port, databaseName, username, password, testResponseMessage);
            }
        });

        valider.addClickListener(new ClickListener() {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();

            @Override
            public void buttonClick(ClickEvent event) {
                if (testConnection(properties, host, port, databaseName, username, password, testResponseMessage)) {
                    databaseConfig.setUsername(username.getValue());
                    databaseConfig.setDatabaseName(databaseName.getValue());
                    databaseConfig.setHost(host.getValue());
                    databaseConfig.setPassword(password.getValue());
                    databaseConfig.setPort(port.getValue());

                    if(isMainView){
                        root.removeAllComponents();
                        buildLoginView(false);
                        
                    }

                }
            }
        });

        valider.addShortcutListener(enter);

        configPanel.addComponent(fields);
        return configPanel;
    }

    private boolean testConnection(Properties properties, TextField host, TextField port, TextField databaseName,
                                   TextField username, PasswordField password, Label testResponseMessage) {
        System.out.println("com.tigamiTech.com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI.buttonClick");
        properties.put("eclipselink.jdbc.url", "jdbc:postgresql://"+host.getValue()+":"
                +port.getValue()+"/"+databaseName.getValue());
        properties.put("eclipselink.jdbc.user", username.getValue());
        properties.put("eclipselink.jdbc.password", password.getValue());
        try {
            Persistence.createEntityManagerFactory(initialDataLoader.PERSISTENCE_UNIT_NAME, properties).createEntityManager();
            testResponseMessage.setValue("la configuration fourni est correct");
            return true;
        } catch (PersistenceException e){
            System.out.println(e.getMessage());
            testResponseMessage.setValue("La configuration fourni est incorrect");
            return false;
        }
    }

    private void buildLoginView(boolean exit) {
        if (exit) {
            root.removeAllComponents();
        }

        addStyleName("login");

        DatabaseLayout = new VerticalLayout();
        DatabaseLayout.setSizeFull();
        DatabaseLayout.addStyleName("login-layout");
        root.addComponent(DatabaseLayout);

        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        loginPanel.addComponent(labels);

        Label welcome = new Label("Bienvenus");
        welcome.setSizeUndefined();
        welcome.addStyleName("h4");
        labels.addComponent(welcome);
        labels.setComponentAlignment(welcome, Alignment.MIDDLE_LEFT);

        Label title = new Label("Gestion de Courrier");
        title.setSizeUndefined();
        title.addStyleName("h2");
        title.addStyleName("light");
        labels.addComponent(title);
        labels.setComponentAlignment(title, Alignment.MIDDLE_RIGHT);

        HorizontalLayout fields = new HorizontalLayout();
        fields.setSpacing(true);
        fields.setMargin(true);
        fields.addStyleName("fields");

        final TextField username = new TextField("Nom d'utilisateur");
        username.focus();
        fields.addComponent(username);

        final PasswordField password = new PasswordField("Mot de passe");
        fields.addComponent(password);

        final Button signin = new Button("Se connecter");
        signin.addStyleName("default");
        fields.addComponent(signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        final ShortcutListener enter = new ShortcutListener("Sign In",
                KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {

                if (AuthenticationService.Authenticate(username.getValue(), password.getValue())) {

                    getSession().setAttribute("username", username.getValue());
                    getSession().setLastRequestDuration(1000000);
                    signin.removeShortcutListener(enter);
                    buildMainView();
                } else {
                    if (loginPanel.getComponentCount() > 2) {
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    Label error = new Label(
                            "Le mot de passe ou le nom d'utilisateur est incorrect",
                            ContentMode.HTML);
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    error.addStyleName("light");
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);

        loginPanel.addComponent(fields);
        DatabaseLayout.addComponent(loginPanel);
        DatabaseLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }


    private void buildMainView() {
    	 initRoots();
        nav = new Navigator(this, content);

        for (String route : routes.keySet()) {
            nav.addView(route, routes.get(route));
        }

        removeStyleName("login");
        root.removeComponent(DatabaseLayout);

        root.addComponent(new HorizontalLayout() {
            {
                setSizeFull();
                addStyleName("main-view");
                addComponent(new VerticalLayout() {
                    // Sidebar
                    {
                        addStyleName("sidebar");
                        setWidth(null);
                        setHeight("100%");

                        // Branding element
                        addComponent(new CssLayout() {
                            {
                               addStyleName("branding");
                               Label logo = new Label(
                                        "<span><b style=\"font-size:20px\">Tegami</b> Tech</span><b>Gestion de courrier</b>",
                                        ContentMode.HTML);
                                logo.setSizeUndefined();
                                addComponent(logo);
                                
                            }
                        });
                        // Main menu
                        addComponent(menu);
                        setExpandRatio(menu, 1);
                        // User menu
                        addComponent(new VerticalLayout() {
                            {
                                setSizeUndefined();
                                addStyleName("user");
                                Image profilePic = new Image(
                                        null,
                                        new ThemeResource("img/profile-pic.png"));
                                profilePic.setWidth("34px");
                                addComponent(profilePic);

                                Label userName = new Label(person.getLastName()+" "+ person.getName());
                                userName.setSizeUndefined();
                                addComponent(userName);

                                Command cmd = new Command() {
                                    @Override
                                    public void menuSelected(
                                            MenuItem selectedItem) {
                                        final Window window = new Window("Modifier le mot de passe");
                                        window.setHeight("50%");
                                        window.setWidth("30%");
                                        window.center();
                                        window.setModal(true);
                                        VerticalLayout verticalLayout = new VerticalLayout();
                                        verticalLayout.setMargin(true);
                                        verticalLayout.setSizeFull();
                                        verticalLayout.setSpacing(true);
                                        verticalLayout.addStyleName("login");
                                        window.setContent(verticalLayout);
                                        final TextField ancienmdp = new TextField("Ancien mot de passe");
                                        final TextField nouvmdp = new TextField("Nouveau mot de passe");
                                        final TextField confirmer = new TextField("Confirmer nouveau mot de passe");
                                        final Label label = new Label("", ContentMode.HTML);
                                        ancienmdp.setRequired(true);
                                        nouvmdp.setRequired(true);
                                        confirmer.setRequired(true);

                                        verticalLayout.addComponent(ancienmdp);
                                        verticalLayout.addComponent(nouvmdp);
                                        verticalLayout.addComponent(confirmer);
                                        verticalLayout.addComponent(label);

                                        Button valider = new Button("valider");
                                        valider.setStyleName("default");
                                        verticalLayout.addComponent(valider);
                                        valider.addClickListener(new ClickListener() {
                                            @Override
                                            public void buttonClick(ClickEvent clickEvent) {
                                                changermdp(label, ancienmdp, nouvmdp, confirmer, window);
                                            }
                                        });
                                        addWindow(window);
                                    }
                                };
                                MenuBar settings = new MenuBar();
                                MenuItem settingsMenu = settings.addItem("",
                                        null);
                                settingsMenu.setStyleName("icon-cog");
                             //   settingsMenu.addItem("Settings", cmd);
                                settingsMenu.addItem("Preferences", cmd);
                                settingsMenu.addSeparator();
                               // settingsMenu.addItem("", cmd);
                                addComponent(settings);

                                Button exit = new NativeButton("Exit");
                                exit.addStyleName("icon-cancel");
                                exit.setCaption("Se déconnecter");
                                exit.setDescription("Déconnecter");
                                addComponent(exit);
                                exit.addClickListener(new ClickListener() {
                                    @Override
                                    public void buttonClick(ClickEvent event) {

                                    	getUI().getPage().setLocation("");
                                        getSession().close();
                                      // buildLoginView(true);
                                       
                                    }
                                });
                            }
                        });
                    }
                });
                // Content
                addComponent(content);
                content.setSizeFull();
                content.addStyleName("view-content");
                setExpandRatio(content, 1);
            }

        });

        menu.removeAllComponents();

        for (final String view : routeNames) {
        	Button b = new NativeButton(view.substring(0, 1).toUpperCase()
                    + view.substring(1).replace('-', ' '));

        	 if (person.getRole().getName().equals(initialDataLoader.SECRITAIRIAT) ) {
        		b.addStyleName("icon-mail");
        		menu.addComponent(b);
        		System.out.println("true");
            }else if (person.getRole().getName().equals(initialDataLoader.ADMINISTRATEUR)){
                 if(view.equals("trace")) b.addStyleName("icon-monitor");
                 else if(view.equals("configuration")) b.addStyleName("icon-cog");
                 else if(view.equals("adminCourrier")) b.addStyleName("icon-mail");
        		 else b.addStyleName("icon-dashboard");
        		 menu.addComponent(b);
        	}else if (person.getRole().getName().equals(initialDataLoader.UTILISATEUR)){
        		//b.addStyleName("icon-cog");
        		String resourceId = "img/profile-pic.png";
				Resource icon = new ThemeResource(resourceId);
				b.setIcon(icon );
				menu.addComponent(b);
        	}
        	
            b.addClickListener(new ClickListener() {
                @Override
                public void buttonClick(ClickEvent event) {
                    clearMenuSelection();
                    event.getButton().addStyleName("selected");
                    if (!nav.getState().equals("/" + view))
                        nav.navigateTo("/" + view);
                }
            });
                

            viewNameToMenuButton.put("/" + view, b);
        }
        menu.addStyleName("menu");
        menu.setHeight("100%");

        String f = Page.getCurrent().getUriFragment();

        	if(person.getRole().getName().equals(initialDataLoader.SECRITAIRIAT))
        		nav.navigateTo(ENTRANTS);
        	if(person.getRole().getName().equals(initialDataLoader.ADMINISTRATEUR))
        		nav.navigateTo("/administration");
        	if(person.getRole().getName().equals(initialDataLoader.UTILISATEUR))
        		nav.navigateTo(COURRIERS_ENTRANTS);
        	
            menu.getComponent(0).addStyleName("selected");
    }

    private void changermdp(Label label, TextField ancienmdp, TextField nouvmdp, TextField confirmer, Window window) {
        label.setValue("");
        if (!ancienmdp.getValue().equals(person.getPassword()))
            label.setValue("<span style='color:red'>L'ancien mot de passe est invalide</span>");
        else if (!nouvmdp.getValue().equals(confirmer.getValue()))
            label.setValue("<span style='color:red'>Les deux mot de passes ne sont pas identique</span>");
        else if(nouvmdp.getValue().isEmpty())
            label.setValue("<span style='color:red'>Le nouveau mot de passes ne peut pas être vide</span>");
        else {
            person.setPassword(nouvmdp.getValue());

            EntityManager em = initialDataLoader.em;
            if(!em.getTransaction().isActive())
                em.getTransaction().begin();
                Person newperson = em.find(Person.class, person.getId());
                newperson.setPassword(nouvmdp.getValue());
                em.persist(newperson);
                 em.getTransaction().commit();
            label.setValue("<b>le mot de passe a été changé<b>");
            Log.saveLog("modifie le mot de passe de "+person.getUsername());
        }
    }


    private void clearMenuSelection() {
        for (Iterator<Component> it = menu.iterator(); it.hasNext();) {
            Component next = it.next();
            if (next instanceof NativeButton) {
                next.removeStyleName("selected");
            } else if (next instanceof DragAndDropWrapper) {
                ((DragAndDropWrapper) next).iterator().next()
                        .removeStyleName("selected");
            }
        }
    }


}
package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.CMIS.CMISConnector;
import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.configuration.ConfigManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

public class ConfigurationView extends VerticalLayout implements View {

    public ConfigurationView() {
        addStyleName("login");
        addStyleName("dashboardBackGround");
        setSizeFull();
        setMargin(true);

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.addStyleName("toolbar");
        addComponent(top);

        final Label title = new Label("Panneau de configuration");
        title.setSizeUndefined();
        title.addStyleName("h1");
        top.addComponent(title);

        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        addComponent(top);
        HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
        addComponent(horizontalSplitPanel);
        VerticalLayout databaseLayout = getDatabaseConfigLayout();
        horizontalSplitPanel.addComponent(databaseLayout);

        VerticalLayout alfrescoLayout = getAlfrescoConfigLayout();
        horizontalSplitPanel.addComponent(alfrescoLayout);

        setExpandRatio(top, 2);
        setExpandRatio(horizontalSplitPanel, 9);

        VerticalLayout verticalLayout = new VerticalLayout();
        addComponent(verticalLayout);
        setExpandRatio(verticalLayout, 5);
    }

    private VerticalLayout getAlfrescoConfigLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        CssLayout layout = getFieldLayout();
        verticalLayout.addComponent(layout);
        return verticalLayout;
    }

    private CssLayout getFieldLayout() {
        final CssLayout configPanel = new CssLayout();
        configPanel.addStyleName("login-panel");

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);
        labels.addStyleName("labels");
        configPanel.addComponent(labels);

        Label welcome = new Label("Configuration de la connection alfresco");
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
        username.setValue(config.getConfiguration("alfrescoUsername"));
        username.focus();
        fields.addComponent(username);
        final PasswordField password = new PasswordField("Mot de passe");
        password.setValue(config.getConfiguration("alfrescoPassword"));
        fields.addComponent(password);

        final TextField host = new TextField("Host");
        host.setValue(config.getConfiguration("alfrescoHost"));
        fields.addComponent(host);

        final TextField port = new TextField("Port");
        port.setValue(config.getConfiguration("alfrescoPort"));
        fields.addComponent(port);
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
        tester.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                testAlfrescoConnection();
            }

            private void testAlfrescoConnection() {
                CMISConnector cmisConnector = new CMISConnector();
                String alfrescoHost = host.getValue();
                String alfrescoPort = port.getValue();
                String serverUrl = "http://"+ alfrescoHost +":"+ alfrescoPort +"/alfresco/service/cmis";
                if(cmisConnector.getSession(serverUrl, username.getValue(), password.getValue()) != null){
                    testResponseMessage.setValue("la configuration fourni est correct");
                }else{
                    testResponseMessage.setValue("la configuration fourni est incorrect");
                }

            }
        });
        valider.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                ConfigManager configManager = ConfigManager.getInstance();
                configManager.setConfiguration("alfrescoUsername", username.getValue());
                configManager.setConfiguration("alfrescoPassword", password.getValue());
                configManager.setConfiguration("alfrescoPort", port.getValue());
                configManager.setConfiguration("alfrescoHost", host.getValue());
            }
        });
        configPanel.addComponent(fields);
        return configPanel;
    }

    private VerticalLayout getDatabaseConfigLayout(){
        CssLayout layout = new MailManagementSystemUI().getDatabaseConfigComponents(false);
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addComponent(layout);
        return verticalLayout;
    }
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

    public boolean testAlfrescoConnection(){
        return false;
    }
}

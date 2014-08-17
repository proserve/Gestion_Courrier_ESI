package com.tigamiTech.mailManagmentSystem.CMIS;

import com.tigamiTech.mailManagmentSystem.configuration.ConfigManager;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CMISConnector {

    public static final String FOLDER_NAME = "Courrier";
    public static final String ENTRANT = "Entrant";
    public static final String SOURTANT = "Sortant";
    private static ConfigManager configManager = ConfigManager.getInstance();;
    private static final SessionFactory sessionFactory = SessionFactoryImpl.newInstance();

    public static Session getSession(String serverUrl, String username, String password) {
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put(SessionParameter.USER, username);
            params.put(SessionParameter.PASSWORD, password);
            params.put(SessionParameter.ATOMPUB_URL, serverUrl);
            params.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
            List<Repository> repos = sessionFactory.getRepositories(params);
            if (repos.isEmpty()) {
                throw new RuntimeException("Server has no repositories!");
            }
            return repos.get(0).createSession();
        }catch (CmisConnectionException e){
            return null;
        }

    }


    public static Session getAlfrescoSessionFromXML() {
        String alfrescoUsername = configManager.getConfiguration("alfrescoUsername");
        String alfrescoPassword = configManager.getConfiguration("alfrescoPassword");
        return getSession(getServerUrl(configManager), alfrescoUsername, alfrescoPassword);
    }

    private static String getServerUrl(ConfigManager configManager) {
        String alfrescoPort = configManager.getConfiguration("alfrescoPort");
        String alfrescoHost = configManager.getConfiguration("alfrescoHost");
        return "http://"+ alfrescoHost +":"+ alfrescoPort +"/alfresco/service/cmis";
    }

    public Folder getFolderFromRoot(){
        Session alfrescoSessionFromXML = getAlfrescoSessionFromXML();
        Folder root = alfrescoSessionFromXML.getRootFolder();
        ItemIterable<CmisObject> rootFolders = root.getChildren();
        Folder folder = null;

        for (CmisObject cmisObject : rootFolders) {
            if(cmisObject instanceof Folder && cmisObject.getName().equals(FOLDER_NAME)){
                folder = (Folder) cmisObject;
            }
        }
        if(folder==null){
            folder = createFolderIn(root, alfrescoSessionFromXML, FOLDER_NAME);
        }
        return  folder;
    }

    public Folder getFolderFromCourrier(String folderName){
        Session alfrescoSessionFromXML = getAlfrescoSessionFromXML();
        Folder root = getFolderFromRoot();
        ItemIterable<CmisObject> rootFolders = root.getChildren();
        Folder folder = null;

        for (CmisObject cmisObject : rootFolders) {
            if(cmisObject instanceof Folder && cmisObject.getName().equals(folderName)){
                folder = (Folder) cmisObject;
            }
        }
        if(folder==null){
            folder = createFolderIn(root, alfrescoSessionFromXML, folderName);
        }
        return  folder;
    }

    public Folder getEntrantFolderFromCourrier(String folderName){
        Session alfrescoSessionFromXML = getAlfrescoSessionFromXML();
        Folder root = getFolderFromCourrier(folderName);
        ItemIterable<CmisObject> rootFolders = root.getChildren();
        Folder folder = null;

        for (CmisObject cmisObject : rootFolders) {
            if(cmisObject instanceof Folder && cmisObject.getName().equals(ENTRANT)){
                folder = (Folder) cmisObject;
            }
        }
        if(folder==null){
            folder = createFolderIn(root, alfrescoSessionFromXML, ENTRANT);
        }
        return  folder;
    }

    public Folder getSortantFolderFromCourrier(String folderName){
        Session alfrescoSessionFromXML = getAlfrescoSessionFromXML();
        Folder root = getFolderFromCourrier(folderName);
        ItemIterable<CmisObject> rootFolders = root.getChildren();
        Folder folder = null;

        for (CmisObject cmisObject : rootFolders) {
            if(cmisObject instanceof Folder && cmisObject.getName().equals(SOURTANT)){
                folder = (Folder) cmisObject;
            }
        }
        if(folder==null){
            folder = createFolderIn(root, alfrescoSessionFromXML, SOURTANT);
        }
        return  folder;
    }

    private Folder createFolderIn(Folder root, Session session, String folderName) {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_FOLDER.
                value());
        String name = folderName;
        properties.put(PropertyIds.NAME, name);
        List<Ace> addAces = new LinkedList<Ace>();
        List<Ace> removeAces = new LinkedList<Ace>();
        List<Policy> policies = new LinkedList<Policy>();
        return  root.createFolder(properties, policies, addAces,
                removeAces, session.getDefaultContext());
    }
}

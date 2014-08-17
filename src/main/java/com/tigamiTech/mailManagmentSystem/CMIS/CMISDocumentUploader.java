package com.tigamiTech.mailManagmentSystem.CMIS;

import com.tigamiTech.mailManagmentSystem.UI.views.AlfrescoReceiver;
import com.vaadin.ui.Notification;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.impl.MimeTypes;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by proserve on 22/05/14.
 */
public class CMISDocumentUploader {

    Session session = CMISConnector.getAlfrescoSessionFromXML();


    public Document uploadDocument(File document, String dossier, boolean isEntrant){
        String name = document.getName();
        CMISConnector connector = new CMISConnector();
        Folder entrantFolderFromCourrier = connector.getEntrantFolderFromCourrier(dossier);
        Folder sortantFolderFromCourrier = connector.getSortantFolderFromCourrier(dossier);

        Folder mailsFolder = (isEntrant) ? entrantFolderFromCourrier : sortantFolderFromCourrier;

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(PropertyIds.OBJECT_TYPE_ID, BaseTypeId.CMIS_DOCUMENT.value());

        List<Ace> addAces = new LinkedList<Ace>();
        List<Ace> removeAces = new LinkedList<Ace>();
        List<Policy> policies = new LinkedList<Policy>();

        properties.put(PropertyIds.NAME, name);


        ContentStream contentStream = null;
        try {
            contentStream = new ContentStreamImpl(name, BigInteger.valueOf(document.length()),
                    MimeTypes.getMIMEType(document), new FileInputStream(document));
            new AlfrescoReceiver().notifyUploadField(
                    "Le téléchargement est terminé avec succès, " +
                            "et le fichier est archivé dans Alfresco","Succes"
            );
            return  mailsFolder.createDocument(properties, contentStream, VersioningState.MAJOR, policies, addAces,
                    removeAces, session.getDefaultContext());

        }  catch (FileNotFoundException e) {
            notifiUploadField(e);
            return null;
        }



       /* InputStream inputStream = newDocument.getContentStream().getStream();

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(newDocument.getName()));
            int bit;
            do{
                bit =inputStream.read();
                fileOutputStream.write(bit);
            }while(bit != -1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }*/
    }

    private void notifiUploadField(Exception e) {
        Notification notification = new Notification(e.getMessage());
        notification.show("il'y a une erreur ==> " + e.getMessage());
        notification.setDelayMsec(1000000);
    }

}

package com.tigamiTech.mailManagmentSystem.CMIS;

import com.tigamiTech.mailManagmentSystem.UI.views.MailReaderView;
import com.tigamiTech.mailManagmentSystem.domaine.Courrier;
import com.tigamiTech.mailManagmentSystem.domaine.CourrierDepart;
import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;

import java.io.*;

/**
 * Created by proserve on 23/06/2014.
 */
public class CMISDocumentReader {
    File file = new File("");
    public File getFileFromAlfresco(String idAlfresco, Courrier courrier){
        Document document = getDocumentFromFolder(idAlfresco, courrier);
        try {
            file = getFileFromDocument(document);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    private File getFileFromDocument(Document document) throws FileNotFoundException {
        InputStream inputStream = document.getContentStream().getStream();

        File file1 = new File("");
        try {
            file1 = new File(MailReaderView.VAADIN_THEMES_DASHBOARD_FILES+document.getName());
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            int bit;
            do {
                bit = inputStream.read();
                fileOutputStream.write(bit);
            } while (bit != -1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

        }
        return file1;
    }

    private Document getDocumentFromFolder(String idAlfresco, Courrier courrier) {
        Folder folder = getFolderFromCourrier(courrier);
        Document document = null;
        for (CmisObject cmisObject : folder.getChildren()) {
            if(cmisObject instanceof Document && cmisObject.getId().equals(idAlfresco)){
                document = (Document) cmisObject;
                System.out.println("");
            }
        }
        return document;
    }

    private Folder getFolderFromCourrier(Courrier courrier){
        if(courrier instanceof CourrierDepart)
            return new CMISConnector().getSortantFolderFromCourrier(courrier.getGroupe().getName());
        else
            return new CMISConnector().getEntrantFolderFromCourrier(courrier.getGroupe().getName());
    }
}

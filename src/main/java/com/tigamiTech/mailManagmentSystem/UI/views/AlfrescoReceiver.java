package com.tigamiTech.mailManagmentSystem.UI.views;

import com.tigamiTech.mailManagmentSystem.CMIS.CMISDocumentUploader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;

import java.io.*;

/**
 * Created by proserve on 22/05/14.
 */
public class AlfrescoReceiver implements Upload.Receiver, Upload.SucceededListener, Upload.FailedListener{

    public Embedded CourrierScanne = new Embedded("le Courrier Scanner");

    public Label cheminFichier = new Label("");
    private File file = new File("vide");
    
    public File getFile() {
		return file;
	}
	public String getMimeType() {
		return mimeType;
	}

	private String mimeType= "";
    @Override
    public OutputStream receiveUpload(String filename,
                                      String mimeType) {
        this.mimeType = mimeType;
        FileOutputStream fos = null;
        try {
            file = new File(filename);
            fos = new FileOutputStream(file);
            FileInputStream fileInputStream = new FileInputStream(file);
            cheminFichier.setValue(filename);
            //file.delete();
            System.out.println(file.getTotalSpace());

        } catch (final FileNotFoundException e) {
          //  notifyUploadField(e);
            return null;
        }


        return fos; // Return the output stream to write to
    }
    public void notifyUploadField(String message, String type) {
       Notification notification = new Notification("<b>"+type+"</b>",
                "<b>"+message+"<b>",
                Notification.Type.ERROR_MESSAGE);
        notification.setHtmlContentAllowed(true);
        notification.setDelayMsec(10000);
        notification.show(Page.getCurrent());

    }
    Document document = null;
    @Override
    public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
    	if(mimeType.contains("image")){
           CourrierScanne.setSource(new FileResource(file));
           CourrierScanne.setVisible(true);
           // AddMailViewEntrant.title.setValue(file.getName());
        }
    	
    }
    
    public void uploadDocumentAfterSave(String dossier, boolean isEntrant){
    	 try {
             CMISDocumentUploader cmisDocumentUploader = new CMISDocumentUploader();
 			document = cmisDocumentUploader.uploadDocument(file, dossier, isEntrant);
             

          //   AddMailViewEntrant.title.setValue(file.getName());
            file.delete();
         } catch (CmisBaseException e) {
             notifyUploadField(e.getMessage(), "Erreur");
         }catch(NullPointerException e){
             notifyUploadField(e.getMessage(), "Erreur");
         }catch(RuntimeException e){
             notifyUploadField(e.getMessage(), "Erreur");
         }
    }
    @Override
    public void uploadFailed(Upload.FailedEvent failedEvent) {

    }
	public String getIdAlfresco() {
		if(document != null)
		return document.getId();
		else return "t";
	}
}

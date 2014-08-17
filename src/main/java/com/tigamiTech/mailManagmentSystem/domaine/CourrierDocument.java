package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gc_courrier_document")
public class CourrierDocument {
	@Id private String idAlfresco;
	private String type;
	private String titre;
	public String getIdAlfresco() {
		return idAlfresco;
	}
	public void setIdAlfresco(String idAlfresco) {
		this.idAlfresco = idAlfresco;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}

    @Override
    public String toString() {
        return titre;
    }

    public static void LogDocument(CourrierDocument courrierDocument){
        Log.saveLog("ajouter le document "+courrierDocument.getTitre() );
    }

}

package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gc_contact")
public class Contact implements TableConfigurable{
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;
	private String code;
	private String fullName;
	private String autreDetail;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getAutreDetail() {
		return autreDetail;
	}
	public void setAutreDetail(String autreDetail) {
		this.autreDetail = autreDetail;
	}
	
	@Override
	public List<String> getNestedProperties() {
		
		return new ArrayList<String>();
	}

	@Override
	public String[] getVisibleColumns() {
		// TODO Auto-generated method stub
		return new String []{"fullName","code","autreDetail" } ;
	}

	@Override
	public String[] getColumnHeaders() {
		// TODO Auto-generated method stub
		return new String[]{"Nom Complete", "Code", "Details"};
	}

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return "Liste des Contacts";
	}

	@Override
	public String[] getColumnsInEditorView() {
		// TODO Auto-generated method stub
		return new String[]{"fullName","code","autreDetail"};
	}

    @Override
    public String getDetail() {
        return "Le contact: "+ fullName +" (code : "+code+")";
    }

    @Override
	public String toString() {
		
		return fullName;
	}
	
}

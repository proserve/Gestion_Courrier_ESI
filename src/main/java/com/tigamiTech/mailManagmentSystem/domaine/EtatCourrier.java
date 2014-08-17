package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gc_etat_courrier")
public class EtatCourrier implements TableConfigurable{
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;

    public int getId() {
        return id;
    }

    private String reference;
	private String name;
	private String description;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public List<String> getNestedProperties() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	public String[] getVisibleColumns() {
		// TODO Auto-generated method stub
		return new String []{"name","description"} ;
	}

	@Override
	public String[] getColumnHeaders() {
		// TODO Auto-generated method stub
		return new String[]{"Nom d'etat", "Description"};
	}

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return "Liste des etats";
	}

	@Override
	public String[] getColumnsInEditorView() {
		// TODO Auto-generated method stub
		return new String[]{"name", "description"};
	}

    @Override
    public String getDetail() {
        return "L'etat de courrier: "+name;
    }

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

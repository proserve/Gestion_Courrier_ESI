package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gc_classe_courrier")
public class ClasseCourrier implements TableConfigurable{
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;
	private String name;
	private String description;
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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
		return new String []{"name","description", "color"} ;
	}

	@Override
	public String[] getColumnHeaders() {
		// TODO Auto-generated method stub
		return new String[]{"Nom de Classe", "Description", "Couleur"};
	}

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return "Liste des Classes";
	}

	@Override
	public String[] getColumnsInEditorView() {
		// TODO Auto-generated method stub
		return new String[]{"name", "description", "color"};
	}

    @Override
    public String getDetail() {
        return "La classe courrier: "+ name;
    }


    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
}

package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by proserve on 24/05/14.
 */
@Entity
@Table(name = "gc_groupe")
public class Groupe implements TableConfigurable{
    @Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;
    private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	@Override
	public List<String> getNestedProperties() {
		// TODO Auto-generated method stub
		return new ArrayList<String>();
	}

	@Override
	public String[] getVisibleColumns() {
		// TODO Auto-generated method stub
		return new String []{"name",} ;
	}

	@Override
	public String[] getColumnHeaders() {
		// TODO Auto-generated method stub
		return new String[]{"Nom de groupe"};
	}

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return "Liste des groupes";
	}

	@Override
	public String[] getColumnsInEditorView() {
		// TODO Auto-generated method stub
		return new String[]{"name"};
	}

    @Override
    public String getDetail() {
        return "Le groupe: "+name;
    }

    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}
	

}

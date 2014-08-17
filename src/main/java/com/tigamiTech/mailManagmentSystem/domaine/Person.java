package com.tigamiTech.mailManagmentSystem.domaine;

import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gc_person")
public class Person implements TableConfigurable{
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;

    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    private String name;
    private String lastName;

    public int getId() {
        return id;
    }

    @ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE} )
    private Role role;

    @ManyToMany(mappedBy = "Correspondant")
    List<Courrier> courriers;

    public List<Courrier> getCourriers() {
        return courriers;
    }

    public void setCourriers(List<Courrier> courriers) {
        this.courriers = courriers;
    }

    @ManyToOne( cascade = {CascadeType.ALL} )
    private Groupe groupe;

	
    public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	
    @CascadeOnDelete
	public Groupe getGroupe() {
		return groupe;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGroupe(Groupe groupe) {
		this.groupe = groupe;
	}


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public List<String> getNestedProperties(){
    	List<String> list = new ArrayList<String>();
    	list.add("role.name");
    	list.add("groupe.name");
    	return list;
    }
    @Override
    public String[] getVisibleColumns(){
    	return new String[]{"name", "lastName", "username","role.name","groupe.name"};
    }
    
    @Override
    public String[] getColumnHeaders(){
    	return new String[]{"Nom", "Prénom", "Utilisateur","Role","Groupe"};
    }

	@Override
	public String getCaption() {
		// TODO Auto-generated method stub
		return "Listes des utilisateurs";
	}

	@Override
	public String[] getColumnsInEditorView() {
		// TODO Auto-generated method stub
		return new String[]{"name", "lastName", "username", "password", "role" ,"groupe"};
	}

    @Override
    public String getDetail() {
        return "L'utilisateur: "+this.username;
    }

    @Override
    public String toString() {
        return username;
    }
}
package com.tigamiTech.mailManagmentSystem.domaine;

import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "gc_courrier")
public class Courrier implements TableConfigurable{
	@Id @GeneratedValue(strategy=GenerationType.AUTO) private int id;
	private String reference;
	
	@ManyToOne
	private ClasseCourrier classeCourrier;
	
	 @ManyToOne 
	private TypeCourrier typeCourrier;
	
	 @ManyToOne 
	private EtatCourrier etatCourrier;
	
	@ManyToOne
	private Person SuiviPar;
	
	@ManyToMany(cascade = CascadeType.ALL)
    private List<Person> Correspondant;
	
	 @ManyToOne 
	private Contact contact;
	
	 @ManyToOne 
	private CourrierDocument documentCourrier;

    @ManyToOne
    private Groupe groupe;

    public static void logCourrier(Courrier newCourrier) {
        String isEntrant = "Entrant";
        if(newCourrier instanceof CourrierDepart) isEntrant = "Sortant";
      /* Log.saveLog("ajouter un courrier "+isEntrant +" avec les infos suivantes " +
                "" +
                "" +
                "Réference => " + newCourrier.getReference() +
                "titre => " + newCourrier.getTitre() +
                "Objet => " + newCourrier.getObjet() +
                "Classe => " + newCourrier.getClasseCourrier().getName() +
                "Type => " + newCourrier.getTypeCourrier().getName());*/
    }

    public Groupe getGroupe() {
        return groupe;
    }

    public void setGroupe(Groupe groupe) {
        this.groupe = groupe;
    }

    @Column(unique=true)
	private String titre;
	
	
	private String Objet;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateDEnvoi;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date DateReception;

	 @Temporal(TemporalType.TIMESTAMP)
	private Date DateArrivee;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDateArrivee() {
		return DateArrivee;
	}

	public void setDateArrivee(Date dateArrivee) {
		DateArrivee = dateArrivee;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}


	public ClasseCourrier getClasseCourrier() {
		return classeCourrier;
	}

	public void setClasseCourrier(ClasseCourrier classeCourrier) {
		this.classeCourrier = classeCourrier;
	}

	public TypeCourrier getTypeCourrier() {
		return typeCourrier;
	}

	public void setTypeCourrier(TypeCourrier courrier) {
		this.typeCourrier = courrier;
	}

	public EtatCourrier getEtatCourrier() {
		return etatCourrier;
	}

	public void setEtatCourrier(EtatCourrier etatCourrier) {
		this.etatCourrier = etatCourrier;
	}

	public Person getSuiviPar() {
		return SuiviPar;
	}

	public void setSuiviPar(Person suiviPar) {
		SuiviPar = suiviPar;
	}

	public List<Person> getCorrespondant() {
		return Correspondant;
	}

	public void setCorrespondant(List<Person> correspondant) {
		Correspondant = correspondant;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String Titre) {
		this.titre = Titre;
	}

	public String getObjet() {
		return Objet;
	}

	public void setObjet(String objet) {
		Objet = objet;
	}

	public Date getDateDEnvoi() {
		return dateDEnvoi;
	}

	public void setDateDEnvoi(Date dateDEnvoi) {
		this.dateDEnvoi = dateDEnvoi;
	}

	public Date getDateReception() {
		return DateReception;
	}

	public void setDateReception(Date dateReception) {
		DateReception = dateReception;
	}

	public CourrierDocument getDocumentCourrier() {
		return documentCourrier;
	}

	public void setDocumentCourrier(CourrierDocument documentCourrier) {
		this.documentCourrier = documentCourrier;
	}
	@Override
	public List<String> getNestedProperties() {
		List<String> list = new ArrayList<String>();
    	list.add("classeCourrier.name");
    	list.add("etatCourrier.name");
    	list.add("suiviPar.username");
    	list.add("Correspondant.username");
    	list.add("contact.fullName");
    	list.add("documentCourrier.titre");
    	return list;
	}

	@Override
	public String[] getVisibleColumns() {
		return new String[]{"reference","DateReception","dateDEnvoi", "Objet","titre"
				,"classeCourrier.name", "etatCourrier.name", "suiviPar.username", "Correspondant.username",
				 "contact.fullName","documentCourrier.titre" };
	}
	
	@Override
	public String[] getColumnHeaders() {
		return new String[]{"Reference","Date de reception","date D'envoi", "Objet","Titre"
				,"Classe de courrier", "Etat de courrier", "suivi Par", "Correspondant",
				 "Contact","Document" };
	}

	@Override
	public String getCaption() {
		return "Liste des Courrier";
	}

	@Override
	public String[] getColumnsInEditorView() {
		return new String[]{"reference","DateReception","dateDEnvoi", "Objet","titre"
                ,"classeCourrier", "etatCourrier", "Correspondant",
                "contact","documentCourrier" };
	}

    @Override
    public String getDetail() {
        return "Le courrier: "+titre;
    }

    @Override
    public String toString() {
        return titre;
    }
    public int getCourrierCountForCurrentUser(){
        int result = 0;
        EntityManager entityManager = initialDataLoader.em;
        String query = "select u from Courrier u";
        List<Courrier> listCourrier =  entityManager.createQuery(query).getResultList();
        for(Courrier courrier:listCourrier){
            if(isCourrierForThisUser(courrier)){
                result++;
            }
        }

        return result;
    }
    private boolean isCourrierForThisUser(Courrier courrier) {
        final boolean isEtatequal = courrier.getEtatCourrier().getName().equals(initialDataLoader.RECU);
        final boolean isPersonEqual = isUserHaveCourrier(courrier);
        return isEtatequal && isPersonEqual;
    }

    private boolean isUserHaveCourrier(Courrier courrier) {
        boolean result = false;
        for(Person person:courrier.getCorrespondant()){
            if(person.getId() == MailManagementSystemUI.person.getId())
                result = true;
        }
        return  result;
    }
}

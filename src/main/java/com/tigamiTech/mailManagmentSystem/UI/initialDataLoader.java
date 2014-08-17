package com.tigamiTech.mailManagmentSystem.UI;

import com.tigamiTech.mailManagmentSystem.domaine.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class initialDataLoader {
    public static final String PERSISTENCE_UNIT_NAME = "gestionCourrier";
    public static final String SECRITAIRIAT = "Sécritairiat";
    public static final String ADMINISTRATEUR = "Administrateur";
    public static final String UTILISATEUR = "Utilisateur";
    public static final String RESPONSABLE = "Résponsable";
    public static final String AUCUN = "Aucun";
    public static final String AUCUNE = "Aucune";
    public static EntityManager em = getFactory().createEntityManager();
    public static final String RECU = "Reçu";
    public static final String TRAITE = "Traité";
    public static final String ARCHIVE = "Archivé";
    public static final String ENVOYE = "Envoyé";
    public static final String SIGNE = "Signé";
    public static final String A_SIGNE = "A signé";
    public static final String URGENT = "Urgent";
    public static final String CONFIDENTIEL = "Confidentiel";
    public static final String FACTURE = "Facture";
    public static final String FAX = "FAX";
    public static final String COURRIER = "Courrier";
    private static boolean isDataLoaded = false;

    public static void loadTestData() {
        if (!isDataLoaded) {
            isDataLoaded = true;

            //liste des groupes initial
            Groupe DE = new Groupe();
            DE.setName("Direction des études");

            Groupe LMCS = new Groupe();
            LMCS.setName("LMCS");

            Groupe biblio = new Groupe();
            biblio.setName("Bibliothèque");

            Groupe RE = new Groupe();
            RE.setName("Relation extèrne");

            Groupe SG = new Groupe();
            SG.setName("Sécritairia générale");

            Groupe DRE = new Groupe();
            DRE.setName("Direction des relations extèrnes");

            Groupe DPGR = new Groupe();
            DPGR.setName("Direction de la postgraduation et la recherche");

            Groupe DMI = new Groupe();
            DMI.setName("Direction MI");

            Groupe AC = new Groupe();
            AC.setName("AC");

            Groupe pers = new Groupe();
            pers.setName("Direction des personnels");

            Groupe Cgest = new Groupe();
            Cgest.setName("Gestion");

            Groupe LCSI = new Groupe();
            LCSI.setName("LCSI");

            Groupe aucunGroupe = new Groupe();
            aucunGroupe.setName(AUCUN);

            // liste des roles initial
            Role administrateur = new Role();
            administrateur.setName(ADMINISTRATEUR);

            Role secritairiat = new Role();
            secritairiat.setName(SECRITAIRIAT);

            Role utilisateur = new Role();
            utilisateur.setName(UTILISATEUR);

            Role responsable = new Role();
            responsable.setName(RESPONSABLE);


            // les utilisateur
                // de role secrétaire
                Person dEUser = new Person();
                dEUser.setLastName("BENKKOUCHE");
                dEUser.setName("Selma");
                dEUser.setUsername("bekkouche");
                dEUser.setPassword("bekkouche");
                dEUser.setGroupe(SG);
                dEUser.setRole(utilisateur);


                Person biblioUser = new Person();
                biblioUser.setLastName("MEDDAHI");
                biblioUser.setName("Imene");
                biblioUser.setUsername("meddahi");
                biblioUser.setPassword("meddahi");
                biblioUser.setGroupe(SG);
                biblioUser.setRole(secritairiat);

                Person rEUser = new Person();
                rEUser.setLastName("BENKRID");
                rEUser.setName("Abderrahmane");
                rEUser.setUsername("benkrid");
                rEUser.setPassword("benkrid");
                rEUser.setGroupe(RE);
                rEUser.setRole(utilisateur);

                // de role adminitrateur
                Person admin = new Person();
                admin.setUsername("mekdour");
                admin.setPassword("mekdour");
                admin.setName("Yassine");
                admin.setLastName("MEKDOUR");
                admin.setGroupe(aucunGroupe);
                admin.setRole(administrateur);

                // de role scriteria
                Person secriUser1 = new Person();
                secriUser1.setUsername("BENBAZIZ");
                secriUser1.setPassword("benbaziz");
                secriUser1.setName("Badro");
                secriUser1.setLastName("BENBAZIZ");
                secriUser1.setGroupe(DE);
                secriUser1.setRole(utilisateur);

                Person secriUser2 = new Person();
                secriUser2.setUsername("kadri");
                secriUser2.setPassword("kadri");
                secriUser2.setLastName("KADRI");
                secriUser2.setName("mohammed");
                secriUser2.setGroupe(DE);
                secriUser2.setRole(utilisateur);

            List<Person> persons = new ArrayList<Person>();
            persons.add(dEUser);
            persons.add(rEUser);
            persons.add(biblioUser);

            //les etats des courriers
            EtatCourrier etatRecu = new EtatCourrier();
            etatRecu.setName(RECU);

            EtatCourrier etatTraite = new EtatCourrier();
            etatTraite.setName(TRAITE);

            EtatCourrier etatArchive = new EtatCourrier();
            etatArchive.setName(ARCHIVE);

            EtatCourrier etatEvoye = new EtatCourrier();
            etatEvoye.setName(ENVOYE);

            EtatCourrier etatSigne = new EtatCourrier();
            etatSigne.setName(SIGNE);

            EtatCourrier etatASigne = new EtatCourrier();
            etatASigne.setName(A_SIGNE);

            EtatCourrier aucuneEtat = new EtatCourrier();
            aucuneEtat.setName(AUCUN);

            //les etats des courriers
            OrientationResponsable enParler = new OrientationResponsable();
            enParler.setName("M'en parler");

            OrientationResponsable pResponsable = new OrientationResponsable();
            pResponsable.setName("P.Résponsable");

            OrientationResponsable etudeAvit = new OrientationResponsable();
            etudeAvit.setName("Etude et Avis");

            OrientationResponsable pCoordination = new OrientationResponsable();
            pCoordination.setName("P.Coordination");

            OrientationResponsable pAffichage = new OrientationResponsable();
            pAffichage.setName("P.Affichage");

            OrientationResponsable aClasser = new OrientationResponsable();
            aClasser.setName("A classer");

            OrientationResponsable scanner = new OrientationResponsable();
            scanner.setName("Scanner");

            OrientationResponsable Pemail = new OrientationResponsable();
            Pemail.setName("P.EMAIL");

            OrientationResponsable aucuneOrientation = new OrientationResponsable();
            aucuneOrientation.setName(AUCUNE);
            // les classes courrier
            ClasseCourrier urgent = new ClasseCourrier();
            urgent.setName(URGENT);

            ClasseCourrier confidentiel = new ClasseCourrier();
            confidentiel.setName(CONFIDENTIEL);

            ClasseCourrier aucuneClasse = new ClasseCourrier();
            aucuneClasse.setName(AUCUNE);

           // les contact
            Contact ministereSante = new Contact();
            ministereSante.setCode("C56C");
            ministereSante.setFullName("Ministére de la santé");


            Contact gendarmeries = new Contact();
            gendarmeries.setCode("C56C");
            gendarmeries.setFullName("Gendarmerie  national");

            Contact AT = new Contact();
            AT.setCode("C56C");
            AT.setFullName("Algerie Télécom");

            Contact babese = new Contact();
            babese.setCode("C56C");
            babese.setFullName("Université de Bab el-zouare");

            // types courriers
            TypeCourrier typeCourrier = new TypeCourrier();
            typeCourrier.setName("Facture");
            typeCourrier.setDescription("les facture");

            TypeCourrier aucunType = new TypeCourrier();
            aucunType.setName(AUCUN);
            //document courrier
            CourrierDocument documentCourrier = new CourrierDocument();
            documentCourrier.setIdAlfresco("workspaceblalal");
            documentCourrier.setTitre("facture de lalaa");
            documentCourrier.setType("PDF");

            Date dateDEnvoi = new Date(2014, 5, 10);
            // les courriers
            Courrier courrier = new Courrier();
            courrier.setClasseCourrier(urgent);
            courrier.setReference("CourrierFactureA");
            courrier.setTitre("some thing");
            courrier.setContact(ministereSante);
            courrier.setTypeCourrier(typeCourrier);
            courrier.setEtatCourrier(etatRecu);
            courrier.setSuiviPar(secriUser2);
            courrier.setDocumentCourrier(documentCourrier);
            courrier.setCorrespondant(persons);
            courrier.setObjet("demande de paiment");
            courrier.setDateDEnvoi(dateDEnvoi);
            courrier.setDateReception(new Date());

            em.getTransaction().begin();
            try {
                //les groupes
                em.persist(DE);
                em.persist(LMCS);
                em.persist(biblio);
                em.persist(RE);
                em.persist(SG);
                em.persist(DRE);
                em.persist(DPGR);
                em.persist(DMI);
                em.persist(AC);
                em.persist(pers);
                em.persist(Cgest);
                em.persist(LCSI);
                em.persist(aucunGroupe);
                //les roles
                em.persist(administrateur);
                em.persist(secritairiat);
                em.persist(utilisateur);
                em.persist(responsable);
                // les utilisateur
                em.persist(dEUser);
                em.persist(biblioUser);
                em.persist(rEUser);
                em.persist(admin);
                em.persist(secriUser1);
                em.persist(secriUser2);

                //les etats des courriers
                em.persist(etatRecu);
                em.persist(etatTraite);
                em.persist(etatArchive);
                em.persist(etatASigne);
                em.persist(etatSigne);
                em.persist(etatEvoye);
                em.persist(aucuneEtat);
                //les  Orientations du DG
                em.persist(enParler);
                em.persist(pResponsable);
                em.persist(etudeAvit);
                em.persist(pCoordination);
                em.persist(pAffichage);
                em.persist(aClasser);
                em.persist(scanner);
                em.persist(Pemail);
                em.persist(aucuneOrientation);
                // les Classe courrier
                em.persist(urgent);
                em.persist(confidentiel);
                em.persist(aucuneClasse);

                //les contact
                em.persist(ministereSante);
                em.persist(gendarmeries);
                em.persist(AT);
                em.persist(babese);

                //les type Courrier
                em.persist(typeCourrier);
                em.persist(aucunType);


              //  em.persist(documentCourrier);
               //  em.persist(courrier);
                em.getTransaction().commit();

            } catch (Exception e) {
                System.out.println(e.getMessage());
                em.getTransaction().rollback();
            }

        }
    }

    private static EntityManagerFactory getFactory() {
        try {
            Properties properties = new Properties();
            String host = "localhost";
            String port = String.valueOf(5432);
            String databaseName = "alfresco";
            String username = "postgres";
            String password = "a";

            properties.put("eclipselink.jdbc.url", "jdbc:postgresql://"+ host +":"+ port +"/"+ databaseName);
            properties.put("eclipselink.jdbc.user", username);
            properties.put("eclipselink.jdbc.password", password);

            return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public static void main(String[] args) {
       loadTestData();
    }
}


package com.tigamiTech.mailManagmentSystem.domaine;

import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "gc_courrier_depart")
public class CourrierDepart extends Courrier {

    @ManyToOne
    private OrientationResponsable orientation;

    public OrientationResponsable getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationResponsable orientation) {
        this.orientation = orientation;
    }

    public static List<CourrierDepart> findAll() {
        EntityManager entityManager = initialDataLoader.em;
        return entityManager.createQuery("select u from CourrierDepart u").getResultList();
    }

    public static List<CourrierDepart> findAllByCurrentUser() {
        EntityManager entityManager = initialDataLoader.em;
        List<CourrierDepart> courrierDeparts = new ArrayList<CourrierDepart>();
        for(Courrier courrier: MailManagementSystemUI.person.getCourriers()){
            if(courrier instanceof CourrierDepart){
                courrierDeparts.add((CourrierDepart) courrier);
            }
        }
        return courrierDeparts;
    }
}

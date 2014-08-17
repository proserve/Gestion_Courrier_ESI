package com.tigamiTech.mailManagmentSystem.domaine;

import javax.persistence.*;

/**
 * Created by proserve on 24/06/2014.
 */
@Entity
@Table(name = "gc_orientation_responsable")
public class OrientationResponsable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(optional = false)
    private CourrierDepart courrierDeparts;

    public CourrierDepart getCourrierDeparts() {
        return courrierDeparts;
    }

    public void setCourrierDeparts(CourrierDepart courrierDeparts) {
        this.courrierDeparts = courrierDeparts;
    }
}

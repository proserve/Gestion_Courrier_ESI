package com.tigamiTech.mailManagmentSystem.domaine;

import com.tigamiTech.mailManagmentSystem.UI.MailManagementSystemUI;
import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by proserve on 22/06/2014.
 */
@Entity
@Table(name = "gc_log")
public class Log implements TableConfigurable{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO) private int id;
    private String username;
    private String role;
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;
    private String detail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public List<String> getNestedProperties() {
        List<String> list = new ArrayList<String>();
        return list;
    }

    @Override
    public String[] getVisibleColumns() {
         return new String[]{"username","role","actionDate", "detail"};
    }

    @Override
    public String[] getColumnHeaders() {
        return new String[]{"Nom d'utilisateur","Role","Date de l'action", "Détail sur l'action"};
    }

    @Override
    public String getCaption() {
        return "Traces";
    }

    @Override
    public String[] getColumnsInEditorView() {
        return new String[]{"username","role","actionDate", "detail"};
    }

    public static void saveLog(String detail){
        Log log = new Log();
        Person person = MailManagementSystemUI.person;
        log.setUsername(person.getUsername());
        log.setRole(person.getRole().getName());
        log.setActionDate(new Date());
        log.setDetail(detail);
       EntityManager entityManager =  initialDataLoader.em;
       if(!entityManager.getTransaction().isActive()) entityManager.getTransaction().begin();
        try{
            entityManager.persist(log);
            entityManager.getTransaction().commit();
        }catch(PersistenceException e){
            System.out.println(e.getMessage());
            entityManager.getTransaction().rollback();
        }
    }

}

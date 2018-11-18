package com.amotte.mobiuqac;

import java.util.Date;

public class Event {

    private Date dateDebut;
    private Date dateFin;
    private String titre;
    private String description;

    public Event(Date deb, Date fin, String t, String desc){
        dateDebut = deb;
        dateFin = fin;
        titre = t;
        description = desc;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

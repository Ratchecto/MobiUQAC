package com.amotte.mobiuqac;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Evenement {
    private String title;
    private String thumbnail;
    private String description;
    private Date date;
    private String localisation;
    private String dateToString;
    private  String uid;

    public Evenement(String nom, Date d, String des, String img){
        title= nom;
        thumbnail= img;
        date = d;
        description = des;
    }
    public Evenement(){

    }
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getDateToString(){
        dateToString = new SimpleDateFormat("d MMMM - HH:mm").format(date);
        return dateToString;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setUid(String title) {
        this.uid = title;
    }
    public String getUid() {
        return uid;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getLocalisation() { return localisation; }

    public void setLocalisation(String localisation) { this.localisation = localisation; }
}
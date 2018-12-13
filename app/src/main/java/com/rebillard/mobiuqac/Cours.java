package com.rebillard.mobiuqac;


public class Cours {
    private String id = "";
    private String identifiant = "";
    private String name = "";
    private String group = "";
    private String day = "";
    private String hourFinish = "";
    private String hourBegin = "";

    private String dateBeg = "";
    private String dateFinish = "";
    private String local = "";
    private String semestre = "";


    public String getId(){
        return id;
    }
    public Cours(){

    }
    public void setId(String id){
        this.id = id;
    }
    public String getIdentifiant(){
        return identifiant;
    }
    public void setIdentifiant(String identifiant){
        this.identifiant = identifiant;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDateBeg() {
        return dateBeg;
    }
    public void setDateBeg(String dateBeg) {
        this.dateBeg = dateBeg;
    }
    public String getDateFinish() {
        return dateFinish;
    }
    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }
    public String getLocal() {
        return local;
    }
    public void setLocal(String local) {
        this.local = local;
    }
    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getHourBegin() {
        return hourBegin;
    }
    public void setHourBegin(String hourBeg) {
        this.hourBegin = hourBeg;
    }
    public String getHourFinish() {
        return hourFinish;
    }
    public void setHourFinish(String hourFinish) {
        this.hourFinish = hourFinish;
    }
    public String getSemestre() {
        return semestre;
    }
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    @Override
    public String toString() {
        return "Cours [id =" + id + ", identifiant =" + identifiant + ", name =" + name + ", semeste= " + semestre
                + ", group=" + group + ", day=" + day + ", hourBeg="
                + hourBegin + ", hourFinish=" + hourFinish + ", dateBeg="
                + dateBeg + ", dateFinish=" + dateFinish + ", local=" + local
                + "]";
    }
}
package com.rebillard.mobiuqac;

public class Cours {
    private String name = "";
    private String group = "";
    private String day = "";
    private String hourBeg = "";
    private String hourFinish = "";
    private String dateBeg = "";
    private String dateFinish = "";
    private String local = "";
    private String semestre = "";

    /**
     * Obtenir le nom
     *
     * @return name (String)
     */
    public String getName(){
        return name;
    }

    /**
     * Fixer le nom
     *
     * @param name (String)
     */
    public void setName(String name){
        this.name = name;
    }


    public  Cours(String name){
        this.name = name;
    }
    public  Cours(){

    }

    /**
     * Obtenir la date de début
     *
     * @return dateBeg (String)
     */
    public String getDateBeg() {
        return dateBeg;
    }

    /**
     * Fixer la date de début
     *
     * @param dateBeg (String)
     */
    public void setDateBeg(String dateBeg) {
        this.dateBeg = dateBeg;
    }

    /**
     * Obtenir la date de fin
     *
     * @return dateFinish (String)
     */
    public String getDateFinish() {
        return dateFinish;
    }

    /**
     * Fixer la date de fin
     *
     * @param dateFinish (String)
     */
    public void setDateFinish(String dateFinish) {
        this.dateFinish = dateFinish;
    }

    /**
     * Obtenir le local
     *
     * @return local (String)
     */
    public String getLocal() {
        return local;
    }

    /**
     * Fixer le local
     *
     * @param local (String)
     */
    public void setLocal(String local) {
        this.local = local;
    }

    /**
     * Obtenir le groupe
     *
     * @return group (String)
     */
    public String getGroup() {
        return group;
    }

    /**
     * Fixer le groupe
     *
     * @param group (String)
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * Obtenir le jour
     *
     * @return day (String)
     */
    public String getDay() {
        return day;
    }

    /**
     * Fixer le jour
     *
     * @param day (String)
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * Obtenir l'heure de début
     *
     * @return hourBeg (String)
     */
    public String getHourBeg() {
        return hourBeg;
    }

    /**
     * Fixer l'heure de début
     *
     * @param hourBeg (String)
     */
    public void setHourBeg(String hourBeg) {
        this.hourBeg = hourBeg;
    }

    /**
     * Obtenir l'heure de fin
     *
     * @return hourFinish (String)
     */
    public String getHourFinish() {
        return hourFinish;
    }

    /**
     * Fixer l'heure de din
     *
     * @param hourFinish (String)
     */
    public void setHourFinish(String hourFinish) {
        this.hourFinish = hourFinish;
    }

    /**
     * Obtenir le semestre
     *
     * @return semestre (String)
     */
    public String getSemestre() {
        return semestre;
    }

    /**
     * Fixer le semestre
     *
     * @param semestre (String)
     */
    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    /**
     * Pour transformer en string
     * @return (String)
     */
    @Override
    public String toString() {
        return "Cours [name =" + name + ", semeste= " + semestre + ", group=" + group + ", day=" + day + ", hourBeg="
                + hourBeg + ", hourFinish=" + hourFinish + ", dateBeg="
                + dateBeg + ", dateFinish=" + dateFinish + ", local=" + local
                + "]";
    }
}

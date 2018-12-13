package com.rebillard.mobiuqac;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public ArrayList<String> nomCours = new ArrayList<>();
    public ArrayList<Cours> cours = new ArrayList<>();
    public String nom;

    public void addCours(Cours cours){this.cours.add(cours); }
    public void addNomCours (String cours){
        if(!nomCours.contains(cours))
            this.nomCours.add(cours);
    }

    public void removeCours(Cours cours){
        this.cours.remove(cours);
    }
    public void removeNomCours(String nom) { this.nomCours.remove(nom); }

    public ArrayList<Cours> getCours(){
        return cours;
    }
    public ArrayList<String> getNomCours(){
        return nomCours;
    }

    public void updateCours(ArrayList<Cours> cours){
        this.cours = cours;
        for(Cours c : cours){
            if(c != null)//Reglage provisoire
                addNomCours(c.getSemestre() + c.getIdentifiant() + c.getGroup());
        }
    }


    public String getName(){
        return nom;
    }
    public User(String nom){
        this.nom = nom;
    }
    public User(){

    }
}

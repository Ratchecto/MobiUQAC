package com.rebillard.mobiuqac;

import java.util.ArrayList;
import java.util.HashMap;

public class User {
    public ArrayList<String> cours = new ArrayList<>();
    public String nom;

    public void addCours(String cours){
        this.cours.add(cours);
    }

    public void removeCours(Cours cours){
        this.cours.remove(cours);
    }

    public ArrayList<String> getCours(){
        return cours;
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

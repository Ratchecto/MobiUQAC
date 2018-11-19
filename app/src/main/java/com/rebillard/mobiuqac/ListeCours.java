package com.rebillard.mobiuqac;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ListeCours {
    private HashMap<String,Cours> dbCours= new HashMap<>();

    public void addCours (Cours cour){
        dbCours.put(cour.getSemestre()+cour.getIdentifiant()+cour.getGroup(),cour);

        Log.e("cccc","---"+cour.getSemestre()+cour.getIdentifiant()+cour.getGroup()+"---");

    }
    public ArrayList<Cours> getCoursFromUser(User user){
        ArrayList<Cours> userCours= new ArrayList<>();
        for (String nom :user.getNomCours()){
            userCours.add(dbCours.get(nom));
        }
        return userCours;
    }
    public Cours gettest() {
        ArrayList<Cours> userCours = new ArrayList<>();
        return dbCours.get(dbCours.keySet().iterator().next());
    }
    public ArrayList<Cours> getCoursBySemester(String sem){
        ArrayList<Cours> userCours= new ArrayList<>();
        for( Cours cours : dbCours.values()){
            if (cours.getSemestre().equals(sem))
                userCours.add(cours);
        }
        return userCours;
    }

}

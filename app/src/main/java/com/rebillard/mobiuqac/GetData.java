package com.rebillard.mobiuqac;

import java.util.ArrayList;

public class GetData {


    static String link = "https://cours.uqac.ca";

    /**
     * Permet de récupérer tous les cours
     *
     * @return (ArrayList<String>)
     * @throws Exception
     */
    public static ArrayList<String> getAllCours() throws Exception {
        String codeSource = GetSource.getCode(link + "/"
                + "sel_cours.html?session=TRIMESTRE&lieu=Chicoutimi&Submit6=Actualiser");
        return Parser.parseToGetCours(codeSource);
    }

    /**
     * Permet de récupérer les horaires d'un cours
     *
     * @param cours (String)
     * @return (ArrayList<Cours>)
     * @throws Exception
     */
    public static ArrayList<Cours> getHoraires(String cours) throws Exception{
        String codeSource = GetSource.getCode(link + "/" + cours);
        ArrayList<Cours> allCours = Parser.parseIntoCours(codeSource);
        ArrayList<Cours> result = new ArrayList<Cours>();
        for (Cours nameCours : allCours) {
            nameCours.setIdentifiant(cours);
            nameCours.setId(nameCours.getSemestre()
                    + nameCours.getIdentifiant() + nameCours.getGroup());
            result.add(nameCours);
        }
        return result;
    }
}
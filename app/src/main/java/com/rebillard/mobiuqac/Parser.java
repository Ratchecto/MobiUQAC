package com.rebillard.mobiuqac;

import java.util.ArrayList;


public class Parser {

    public static ArrayList<String> parseToGetCours(String codeSource){
        ArrayList<String> cours = new ArrayList<String>();
        String[] list = codeSource.split("target=" + Character.toString((char)39)
                + "_blank" + Character.toString((char)39) + ">");
        for(int i = 1; i< list.length; i++){
            String[] name = list[i].split("</a>");
            cours.add(name[0]);
        }

        return cours;
    }

    public static ArrayList<Cours> parseIntoCours(String codeSource){
        ArrayList<Cours> list = new ArrayList<Cours>();
        ArrayList<String> semestres = new ArrayList<String>();

        //Partie nom
        String[] names = codeSource.split("</span>\n<h1>");
        names = names[1].split("</h1>");
        String name = names[0];

        //Partie semestre
        String[]  test = codeSource.split("<h5>");
        for (int i =1; i<test.length; i++) {
            String[] s = test[i].split("</h5>");
            semestres.add(s[0]);
        }

        int semestre = 0;

        //Partie données
        String[] groupes = codeSource.split("<tr>\n<td class");

        for(int i = 1; i < groupes.length; i++){
            Cours cours = new Cours();
            String[] split = groupes[i].split("</tr>");
            String[] datas = split[0].split("</td>");
            String sentence = "";
            for (String string : datas) {
                int start = string.indexOf(">")+1;
                int end = string.length();
                if(start != -1)
                    sentence += string.substring(start, end) + "-/";

            }
            String[] last = sentence.split("-/");

            if(last.length == 14){
                cours.setName(name);
                cours.setDay(last[1]);
                cours.setDateBeg(last[2]);
                cours.setDateFinish(last[5]);
                cours.setHourBeg(last[7]);
                cours.setHourFinish(last[9]);
                cours.setLocal(last[11]);
            }

            //Partie semestre
            if(semestre+1 < semestres.size()){
                if(groupes[i-1].indexOf(semestres.get(semestre+1)) != -1 ){
                    semestre++;
                }
            }
            cours.setSemestre(semestres.get(semestre));

            //Partie groupe
            String[] group = groupes[i-1].split("<span class=" + Character.toString((char)39)
                    + "groupe" + Character.toString((char)39) + ">");

            if(group.length != 1){
                group = group[1].split("</span>");
                cours.setGroup(group[0]);
            }


            list.add(cours);
        }
        return list;
    }
}

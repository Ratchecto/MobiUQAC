package com.amotte.mobiuqac;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.rebillard.mobiuqac.Cours;

public class ShowCours extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String s = getIntent().getExtras().getString("cours");
        Cours c = coursFromString(s);

        setContentView(R.layout.activity_show_cours);

        TextView name = (TextView) findViewById(R.id.nameDisplay);
        TextView location = (TextView) findViewById(R.id.locationDisplay);
        TextView date = (TextView) findViewById(R.id.dateDisplay);
        TextView link = (TextView) findViewById(R.id.linkDisplay);

        String info = "Pour plus d'information sur le cours, cliquez : ";

        name.setText(c.getName());
        location.setText(c.getLocal());
        date.setText("Du " + c.getDateBeg() + " au " + c.getDateFinish());
        String l = "'https://cours.uqac.ca/"+ c.getIdentifiant()+"'";
        String linkText = info + "<a href=" + l + ">ici</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private Cours coursFromString(String s){
        Cours c = new Cours();
        String[] data = s.split(",");
        String id = data[0];
        String identifiant = data[1];
        String name = data[2];
        String semestre = data[3];
        String group = data[4];
        String day = data[5];
        String hB = data[6];
        String hF = data[7];
        String dB = data[8];
        String dF = data[9];
        String local = data[10];

        c.setId(id.substring(id.indexOf("=")+1, id.length()));
        c.setIdentifiant(identifiant.substring(identifiant.indexOf("=")+1, identifiant.length()));
        c.setName(name.substring(name.indexOf("=")+1, name.length()));
        c.setSemestre(semestre.substring(semestre.indexOf("=")+1, semestre.length()));
        c.setGroup(group.substring(group.indexOf("=")+1, group.length()));
        c.setDay(day.substring(day.indexOf("=")+1, day.length()));
        c.setHourBegin(hB.substring(hB.indexOf("=")+1, hB.length()));
        c.setHourFinish(hF.substring(hF.indexOf("=")+1, hF.length()));
        c.setDateBeg(dB.substring(dB.indexOf("=")+1, dB.length()));
        c.setDateFinish(dF.substring(dF.indexOf("=")+1, dF.length()));
        c.setLocal(local.substring(local.indexOf("=")+1, local.length()-1));
        return c;
    }
}

package com.amotte.mobiuqac;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AjouterEventActivity extends AppCompatActivity {

    int annee_debut, mois_debut, jours_debut, annee_fin, mois_fin, jours_fin;
    String titre, description;
    TextView date_debut, date_fin;
    DatePickerDialog.OnDateSetListener mDateDebutSetListener, mDateFinSetListener;

    static final String TAG = "AjouterEventActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_event);

        Intent intent = getIntent();
        annee_debut = intent.getIntExtra("int_annee", 0);
        mois_debut = intent.getIntExtra("int_mois", 0);
        jours_debut = intent.getIntExtra("int_jours", 0);

        date_debut = (TextView) findViewById(R.id.editTextDateDebut);
        date_debut.setText(jours_debut + "/" + mois_debut + "/" + annee_debut);

        date_debut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjouterEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateDebutSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateDebutSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = day + "/" + month + "/" + year;
                date_debut.setText(date);
            }
        };

        date_fin = (TextView) findViewById(R.id.editTextDateFin);
        date_fin.setText(jours_debut + "/" + mois_debut + "/" + annee_debut);

        date_fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AjouterEventActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateFinSetListener,
                        year, month, day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateFinSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = day + "/" + month + "/" + year;
                date_fin.setText(date);
            }
        };

        EditText ti = (EditText) findViewById(R.id.editTextTitre);
        titre = ti.getText().toString();

        EditText desc = (EditText) findViewById(R.id.editTextDescription);
        description = desc.getText().toString();

    }

    public void ajouterEntreeCalendrier(View v){

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);

        Calendar beginTime = Calendar.getInstance();
        beginTime.set(annee_debut, mois_debut  , jours_debut, 19, 00);
        Calendar endTime = Calendar.getInstance();
        endTime.set(annee_debut, mois_debut, jours_debut, 21, 45);
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, titre);
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, description);

        //Verify There is an App to Receive the Intent
        // A verifier au demarrage et tout simplement desactiver le bouton  ;-)

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(calendarIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        // Start an activity if it's safe
        if (isIntentSafe)
            startActivity(calendarIntent);
    }
}

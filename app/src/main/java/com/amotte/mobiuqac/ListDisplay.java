package com.amotte.mobiuqac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebillard.mobiuqac.Cours;
import com.rebillard.mobiuqac.ListeCours;

import java.util.ArrayList;

public class ListDisplay extends Activity {
    // Array of strings...
    private String[] arraySem = {"Automne 2018", "Hiver 2019", "Été 2019"};
    String[] exemple = {};
    private ArrayList<String> a = new ArrayList<String>();
    private ArrayList<String> all = new ArrayList<String>();
    private ListView listView ;
    private ArrayAdapter ad;
    private Spinner spinner;

    final private ListeCours dbCours= new ListeCours();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cours);
        spinner = (Spinner) findViewById(R.id.semestre);
        listView = (ListView) findViewById(R.id.listCours);
        EditText editCours = (EditText) findViewById(R.id.editCours);


        ad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, a);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser FirebaseUser = mAuth.getCurrentUser();
        if (FirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference coursRef = rootRef.child("cours");
        coursRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.w("", "test1" );
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            dbCours.addCours(dsp.getValue(Cours.class));
                        }
                        ArrayList<Cours> allCours = dbCours.getCoursBySemester(spinner.getSelectedItem().toString());
                        a.clear();
                        ad.notifyDataSetChanged();
                        all.clear();
                        for (Cours cours : allCours) {
                            a.add(cours.getIdentifiant() + cours.getGroup());
                            ad.notifyDataSetChanged();
                            all.add(cours.getIdentifiant() + cours.getGroup());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });



        spinner.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySem));

        listView.setAdapter(ad);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ArrayList<Cours> allCours = dbCours.getCoursBySemester(spinner.getSelectedItem().toString());
                a.clear();
                ad.notifyDataSetChanged();
                all.clear();
                for (Cours cours : allCours) {
                    a.add(cours.getIdentifiant() + cours.getGroup());
                    ad.notifyDataSetChanged();
                    all.add(cours.getIdentifiant() + cours.getGroup());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                Intent data = new Intent();
                data.putExtra("result", spinner.getSelectedItem().toString()
                         + listView.getItemAtPosition(position).toString());
                setResult(Activity.RESULT_OK, data);
                finish();
            }
        });

        //Quand on change l'ecriture
        editCours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                a.clear();
                ad.notifyDataSetChanged();
                for (String name : all) {
                    if (name.indexOf(s.toString().toUpperCase()) != -1) {
                        a.add(name);
                        ad.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
            }
}

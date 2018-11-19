package com.amotte.mobiuqac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebillard.mobiuqac.Cours;
import com.rebillard.mobiuqac.ListeCours;
import com.rebillard.mobiuqac.User;

import java.util.ArrayList;

public class RemoveCours extends Activity {
    private ListView listCours;
    private DatabaseReference userRef;
    private User user;
    private ListeCours dbCours= new ListeCours();
    private FirebaseAuth mAuth;
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_cours);

        listCours = (ListView) findViewById(R.id.listCoursRemove);
        ad = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, name);
        listCours.setAdapter(ad);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser FirebaseUser = mAuth.getCurrentUser();
        if (FirebaseUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        }
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        userRef = rootRef.child("Users").child(FirebaseUser.getUid());
        user = new User();
        userRef.setValue(user);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                //mTextMessage3.setText("------"+value.getCours().get(0)+"---------");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("", "Failed to read value.", error.toException());
            }
        });

        DatabaseReference coursRef = rootRef.child("cours");
        coursRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            dbCours.addCours(dsp.getValue(Cours.class));
                        }
                        user.updateCours(dbCours.getCoursFromUser(user));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("", "Failed to read value.", error.toException());
                    }
                });


        ArrayList<String> allCours = user.getNomCours();

        for (String cours : allCours) {
            name.add(cours);
            ad.notifyDataSetChanged();
        }

        listCours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                String data = listCours.getItemAtPosition(position).toString();
                user.removeNomCours(data);
                userRef.setValue(user);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });


    }
}

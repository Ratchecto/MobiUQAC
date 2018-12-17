package com.amotte.mobiuqac;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rebillard.mobiuqac.ListeCours;
import com.rebillard.mobiuqac.User;

import java.util.ArrayList;

public class RemoveCours extends Activity {
    private ListView listCours;
    private DatabaseReference userRef;
    private User user=CalendarActivity.getUser();
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

        userRef = rootRef.child("users").child(FirebaseUser.getUid());

        ArrayList<String> allCours = user.getNomCours();

        for (String cours : allCours) {
            name.add(cours);
            ad.notifyDataSetChanged();
        }

        listCours.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {

                Intent datas = new Intent();
                datas.putExtra("result", "cc");


                String data = listCours.getItemAtPosition(position).toString();
                user.removeNomCours(data);
                userRef.setValue(user);
                setResult(Activity.RESULT_CANCELED, datas);
                finish();
            }
        });


    }
}

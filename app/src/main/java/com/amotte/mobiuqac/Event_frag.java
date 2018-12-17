package com.amotte.mobiuqac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Event_frag extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";

    private List<Evenement> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    Intent intent = new Intent(Event_frag.this, CalendarActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_about:
                    mAuth.signOut();
                    Intent intent1 = new Intent(Event_frag.this, LoginActivity.class);
                    startActivity(intent1);
                    return true;
                case R.id.navigation_event:
                    return true;
            }
            return false;
        }
    };

    private DatabaseReference rootRef;
    FirebaseUser FirebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);
        FloatingActionButton fab =  findViewById(R.id.addevent);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser = mAuth.getCurrentUser();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_event);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Event_frag.this, AjouterEventActivity.class);
                startActivity(i);
            }
        });
        //

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //parseResult();


        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listeventref = rootRef.child("events");

        listeventref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        feedsList = new ArrayList<>();
                        Log.e("ccccc","update db");
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Evenement event = dsp.getValue(Evenement.class);
                            feedsList.add(event);
                        }
                        //Collections.reverse(feedsList);
                        adapter = new MyRecyclerViewAdapter(Event_frag.this, feedsList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Evenement item) {
                                Intent i = new Intent(Event_frag.this, ShowEvent.class);
                                i.putExtra("titre", item.getTitle());
                                i.putExtra("description", item.getDescription());
                                i.putExtra("date", item.getDateToString());
                                i.putExtra("thumbnail", item.getThumbnail());
                                i.putExtra("localisation", item.getLocalisation());
                                i.putExtra("uid", item.getUid());
                                i.putExtra("id",item.getDate().getTime()+item.getUid());

                                startActivity(i);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Echec de la lecture des événements", error.toException());
                    }
                });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode ==RESULT_OK) {

            this.recreate();
        }


    }
}
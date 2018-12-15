package com.amotte.mobiuqac;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebillard.mobiuqac.Cours;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event_frag extends AppCompatActivity {
    private static final String TAG = "RecyclerViewExample";

    private List<Evenement> feedsList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
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
                    Intent intent3 = new Intent(Event_frag.this, LoginActivity.class);
                    startActivity(intent3);
                    return true;
                case R.id.navigation_event:
                    Intent intent2 = new Intent(Event_frag.this, Event_frag.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    FirebaseUser FirebaseUser;
    String testurl ="https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //parseResult();


        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listeventref = rootRef.child("events");
        DatabaseReference eventref = listeventref.child("test1");
        //Evenement e = new Evenement("nom event", new Date(), "Description",testurl);
        //eventref.setValue(e);





        listeventref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        feedsList = new ArrayList<>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Evenement event = dsp.getValue(Evenement.class);
                            feedsList.add(event);
                        }
                        adapter = new MyRecyclerViewAdapter(Event_frag.this, feedsList);
                        mRecyclerView.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(Evenement item) {
                                Toast.makeText(Event_frag.this, item.getTitle(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });

    }
}
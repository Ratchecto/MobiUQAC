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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_events);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        parseResult();
        adapter = new MyRecyclerViewAdapter(Event_frag.this, feedsList);

        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Evenement item) {
                Toast.makeText(Event_frag.this, item.getTitle(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void parseResult() {
        feedsList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Evenement item = new Evenement();
            item.setTitle("Titre de l'évenement !"+ i);
            item.setThumbnail("https://firebasestorage.googleapis.com/v0/b/mobiuqac-4caa3.appspot.com/o/CMS_Creative_164657191_Kingfisher.jpg?alt=media&token=cdf8e0fb-b635-4664-a9c7-fea2d833bf25");
            item.setDate(new Date());

            feedsList.add(item);
        }
    }
}
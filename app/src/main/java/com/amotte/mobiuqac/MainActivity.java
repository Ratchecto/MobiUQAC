package com.amotte.mobiuqac;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rebillard.mobiuqac.Cours;
import com.rebillard.mobiuqac.User;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ArrayList<Cours> dbCours= new ArrayList<>(); // ya tout les cours ici
    public int i =0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser FirebaseUser = mAuth.getCurrentUser();
        if (FirebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);

        }
        else {
            rootRef = FirebaseDatabase.getInstance().getReference();

            final TextView mTextMessage2 = (TextView) findViewById(R.id.message2);
            mTextMessage2.setText(FirebaseUser.getEmail());



            DatabaseReference userRef = rootRef.child("Users").child(FirebaseUser.getUid());
            User user = new User();
            user.addCours("cc");
            user.addCours("ouiiii");
            userRef.setValue(user);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User value = dataSnapshot.getValue(User.class);
                    //mTextMessage3.setText("------"+value.getCours().get(0)+"---------");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            DatabaseReference coursRef = rootRef.child("cours");
            coursRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Cours value = dataSnapshot.getValue(Cours.class);
                        i= i+1;
                        //dbCours.add(value);
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            dbCours.add(dsp.getValue(Cours.class)); //add result into array list
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        }
    }



}

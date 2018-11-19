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
import com.rebillard.mobiuqac.ListeCours;
import com.rebillard.mobiuqac.User;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView mTextMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private ListeCours dbCours= new ListeCours(); // ya tout les cours ici

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_about:
                    return true;
                case R.id.navigation_event:
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
            final User user = new User();
            userRef.setValue(user);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User value = dataSnapshot.getValue(User.class);
                    //mTextMessage3.setText("------"+value.getCours().get(0)+"---------");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w(TAG, "Failed to read value.", error.toException());
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
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
        }
    }


}

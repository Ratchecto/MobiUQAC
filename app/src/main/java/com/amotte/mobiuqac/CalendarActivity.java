package com.amotte.mobiuqac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
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
import com.rebillard.mobiuqac.Cours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CalendarActivity extends AppCompatActivity implements WeekView.EventClickListener, WeekView.EmptyViewClickListener, MonthLoader.MonthChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    int annee, mois, jours;
    WeekView mWeekView;
    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    Calendar clickedTime;
    FloatingActionButton fab1;
    FloatingActionButton fab2 ;
    FloatingActionButton fab3;
    boolean isFABOpen;
    Button btnAjoutCours;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private static ListeCours dbCours= new ListeCours();
    private User user;

    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        btnAjoutCours = findViewById(R.id.addCours);

        final Cours coursTest = new Cours();
        coursTest.setName("Cours de test");
        coursTest.setDateBeg("18-11-2018");
        coursTest.setDateFinish("17-12-2018");
        coursTest.setHourBeg("8:00");
        coursTest.setHourFinish("9:30");

        btnAjoutCours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCours(coursTest);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        // Get a reference for the week view in the layout.
        mWeekView = findViewById(R.id.weekView);
        mWeekView.setAccessibilityLiveRegion(MODE_APPEND);
        mWeekView.setNumberOfVisibleDays(7);

        events= new ArrayList<WeekViewEvent>();
        clickedTime=Calendar.getInstance();

        // Lets change some dimensions to best fit the view.
        mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));

        mWeekView.setMonthChangeListener(this);
        mWeekView.setEmptyViewClickListener(this);
        mWeekView.setOnEventClickListener(this);

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                return events;
    }
    public void onEmptyViewClicked(final Calendar time) {
        clickedTime=(Calendar) time.clone();
        mWeekView.notifyDatasetChanged();
        Log.i("msg","Empty box has been filled successfully.");
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    public List<? extends WeekViewEvent> AddCours(Cours c){

        String[] dateDebCours = c.getDateBeg().split("-");
        String[] dateFinCours = c.getDateFinish().split("-");
        String[] heureDeb = c.getHourBeg().split(":");
        String[] heureFin = c.getHourFinish().split(":");

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateDebCours[0]));
        startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(heureDeb[0]));
        startTime.set(Calendar.MINUTE, Integer.parseInt(heureDeb[1]));
        startTime.set(Calendar.MONTH, Integer.parseInt(dateDebCours[1])-1);
        startTime.set(Calendar.YEAR, Integer.parseInt(dateDebCours[2]));
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateDebCours[0]));
        endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(heureFin[0]));
        endTime.set(Calendar.MINUTE, Integer.parseInt(heureFin[1]));
        endTime.set(Calendar.MONTH, Integer.parseInt(dateDebCours[1])-1);
        endTime.set(Calendar.YEAR, Integer.parseInt(dateDebCours[2]));
        WeekViewEvent event = new WeekViewEvent (1, c.getName(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorPrimary));

        events.add(event);
        mWeekView.notifyDatasetChanged();

        Toast.makeText(this, "Cours ajouté : " + c.getName(), Toast.LENGTH_LONG).show();

        return events;
    }

    public void addCours2(Cours c){
        Date cur = new Date();
        Date debut;
        Date fin;
        Calendar cal = Calendar.getInstance();
        ArrayList<Date> l =new ArrayList<>();

        try {
            debut=new SimpleDateFormat("dd-MM-yyyy").parse(c.getDateBeg());
            fin=new SimpleDateFormat("dd-MM-yyyy").parse(c.getDateFinish());
            cur=debut;
            cal.setTime(debut);
            while (cur.compareTo(fin) < 0){
                //addevent here
                l.add(cur);
                cal.add(Calendar.WEEK_OF_MONTH, 1);
                cur = cal.getTime();

            }
            Log.e("AddCours",l.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser FirebaseUser = mAuth.getCurrentUser();
        if (FirebaseUser == null) {
            Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
            startActivity(intent);

        }
        else {
            rootRef = FirebaseDatabase.getInstance().getReference();

            DatabaseReference userRef = rootRef.child("users").child(FirebaseUser.getUid());
            user = new User("salut");
            user.addNomCours("Automne 20181ASP100Groupe 11 (CHICOUTIMI SOIR)");
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
                            Log.e("ccccd", dbCours.getCoursFromUser(user).get(0).toString());
                            addCours2(dbCours.getCoursBySemester("Automne 2018").get(1));


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });

        }
    }
}

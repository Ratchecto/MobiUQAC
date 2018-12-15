package com.amotte.mobiuqac;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
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

import java.text.DateFormat;
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

    WeekView mWeekView;
    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
    Calendar clickedTime;
    FloatingActionButton fab1;
    FloatingActionButton fab2 ;
    FloatingActionButton fab3;
    boolean isFABOpen;

    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;
    private static ListeCours dbCours= new ListeCours();
    FirebaseUser FirebaseUser;
    DatabaseReference userRef;
    private static User user;

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
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab1 =  findViewById(R.id.fab1);
        fab2 =  findViewById(R.id.fab2);
        fab3 =  findViewById(R.id.fab3);
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

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIntentListDiplay();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIntentRemoveCours();
            }
        });

    }

    private void newIntentListDiplay(){
        Intent i = new Intent(this, ListDisplay.class);
        startActivityForResult(i, 1);
    }

    private void newIntentRemoveCours(){
        Intent i = new Intent(this, RemoveCours.class);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String result=data.getStringExtra("result");
        user.addNomCours(result);
        userRef.setValue(user);

        update();
        this.recreate();


    }
    //onActivityResult

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int year, int month) {
        // Get the starting point and ending point of the given month. We need this to find the
        // events of the given month.
        Calendar startOfMonth = Calendar.getInstance();
        startOfMonth.set(Calendar.YEAR, year);
        startOfMonth.set(Calendar.MONTH, month - 1);
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1);
        startOfMonth.set(Calendar.HOUR_OF_DAY, 8);
        startOfMonth.set(Calendar.MINUTE, 0);
        startOfMonth.set(Calendar.SECOND, 0);
        startOfMonth.set(Calendar.MILLISECOND, 0);
        Calendar endOfMonth = (Calendar) startOfMonth.clone();
        endOfMonth.set(Calendar.DAY_OF_MONTH, endOfMonth.getMaximum(Calendar.DAY_OF_MONTH));
        endOfMonth.set(Calendar.HOUR_OF_DAY, 23);
        endOfMonth.set(Calendar.MINUTE, 59);
        endOfMonth.set(Calendar.SECOND, 59);

        // Find the events that were added by tapping on empty view and that occurs in the given
        // time frame.
        ArrayList<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        for (WeekViewEvent event : this.events) {
            if (event.getEndTime().getTimeInMillis() > startOfMonth.getTimeInMillis() &&
                    event.getStartTime().getTimeInMillis() < endOfMonth.getTimeInMillis()) {
                events.add(event);

            }
        }
        return events;
    }
    public void onEmptyViewClicked(final Calendar time) {
        clickedTime=(Calendar) time.clone();
        mWeekView.notifyDatasetChanged();
        Log.i("msg","Empty box has been filled successfully.");
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        /*String name = event.getName();
        String location  = event.getLocation();
        Calendar dateDebut = event.getStartTime();
        Calendar dateFin = event.getEndTime();

        String[] date1 = dateDebut.getTime().toString().split(" ");
        String[] date2 = dateFin.getTime().toString().split(" ");

        Context context = getApplicationContext();
        CharSequence text = name + ",  " + location + ". Du " + date1[2] + " " + date1[1] + " " + date1[5] + " au "
                + date2[2] + " " + date2[1] + " " + date2[5];
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();*/

        for (Cours cour : dbCours.getCoursFromUser(user)){
            if(cour != null){
                if(cour.getName().equals(event.getName())) {
                    Intent i = new Intent(this, ShowCours.class);
                    i.putExtra("cours", cour.toString());
                    startActivity(i);
                }
            }
        }
    }

    public List<? extends WeekViewEvent> AddCours(Cours c){

       // Log.e("cccc",debut.toString()+"----"+fin.toString());
        Log.e("cccc",c.toString());

        String[] dateDebCours = c.getDateBeg().split("-");
        String[] dateFinCours = c.getDateFinish().split("-");
        String[] heureDeb = c.getHourBegin().split(":");
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

        WeekViewEvent event = new WeekViewEvent (1,c.getName(), c.getLocal() ,startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorPrimary));

        events.add(event);
        events.get(0).toString();
        mWeekView.notifyDatasetChanged();

        Log.e("cccc","ajout de :");
        return events;
    }

    public void addCours2(Cours c){
        Date cur = new Date();
        Date debut;
        Date debut2;
        Date fin;
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        ArrayList<Date> l =new ArrayList<>();

        try {
            debut=new SimpleDateFormat("dd-MM-yyyyHH:mm").parse(c.getDateBeg()+c.getHourBegin());
            debut2=new SimpleDateFormat("dd-MM-yyyyHH:mm").parse(c.getDateBeg()+c.getHourFinish());
            fin=new SimpleDateFormat("dd-MM-yyyy").parse(c.getDateFinish());
            cur=debut;
            cal.setTime(debut);
            cal2.setTime(debut2);

            while (cur.compareTo(fin) <= 0){
                Cours cc = new Cours();
                cc.setName(c.getName());
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String d = dateFormat.format(cur);
                cc.setDateBeg(d);
                cc.setHourBegin(c.getHourBegin());
                cc.setHourFinish(c.getHourFinish());
                   AddCours(cc);
                l.add(cur);
                cal.add(Calendar.WEEK_OF_MONTH, 1);
                cal2.add(Calendar.WEEK_OF_MONTH, 1);
                cur = cal.getTime();
            }
            Log.e("cccc",l.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser = mAuth.getCurrentUser();
        if (FirebaseUser == null) {
                Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        else {
                rootRef = FirebaseDatabase.getInstance().getReference();
                userRef = rootRef.child("users").child(FirebaseUser.getUid());

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User value = dataSnapshot.getValue(User.class);
                        user = value;
                        update();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("ccc", "Failed to read value.", error.toException());
                    }
                });


            DatabaseReference coursRef = rootRef.child("cours");

            coursRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.e("cccc", "ondatachange");

                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                dbCours.addCours(dsp.getValue(Cours.class));
                            }
                            update();
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });


        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_calendar:
                    return true;
                case R.id.navigation_about:
                    mAuth.signOut();
                    Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_event:
                    Intent intent2 = new Intent(CalendarActivity.this, Event_frag.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };
    public static User getUser(){
        return user;
    }
    public void update(){
        if (user != null){
            if(dbCours.getCoursFromUser(user) != null) {
                user.updateCours(dbCours.getCoursFromUser(user));
                events = new ArrayList<WeekViewEvent>();

                for (Cours cour : dbCours.getCoursFromUser(user)) {
                    if(cour != null)
                        addCours2(cour);
                }
            }

        }else{
            userRef.setValue(new User(FirebaseUser.getEmail()));
        }
    }



}

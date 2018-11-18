package com.amotte.mobiuqac;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.RectF;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.rebillard.mobiuqac.Cours;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class CalendarActivity extends AppCompatActivity implements WeekView.EventClickListener, WeekView.EmptyViewClickListener, MonthLoader.MonthChangeListener {

    int annee, mois, jours;
    WeekView mWeekView;
    List<WeekViewEvent> events;
    Calendar clickedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

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

        //CalendarView cal = (CalendarView) findViewById(R.id.calendar); // get the reference of CalendarView

        /*cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                annee = year;
                mois = month;
                jours = dayOfMonth;
                // display the selected date by using a toast
                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month+=1) + "/" + year, Toast.LENGTH_LONG).show();
            }
        });*/

    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        //Toast.makeText(this, "newMonth: "+newMonth+"  newYear: "+newYear, Toast.LENGTH_SHORT).show();

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        Calendar startTime = Calendar.getInstance();

        // All day event until 00:00 next day
        startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, 10);
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.set(Calendar.MONTH, newMonth-1);
        startTime.set(Calendar.YEAR, newYear);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 9);
        endTime.set(Calendar.MINUTE, 30);
        WeekViewEvent event = new WeekViewEvent (8, "Titre de l'event", startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorPrimary));
        events.add(event);

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

    public void AddCours(Cours c){

        String[] dateDebCours = c.getDateBeg().split("-");
        String[] dateFinCours = c.getDateFinish().split("-");

        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateDebCours[0]));
        startTime.set(Calendar.HOUR_OF_DAY, 8);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.MONTH, Integer.parseInt(dateDebCours[1]));
        startTime.set(Calendar.YEAR, Integer.parseInt(dateDebCours[2]));
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateFinCours[0]));
        endTime.set(Calendar.HOUR_OF_DAY, 9);
        endTime.set(Calendar.MINUTE, 30);
        endTime.set(Calendar.MONTH, Integer.parseInt(dateFinCours[1]));
        endTime.set(Calendar.YEAR, Integer.parseInt(dateFinCours[2]));
        WeekViewEvent event = new WeekViewEvent (8, c.getName(), startTime, endTime);
        event.setColor(getResources().getColor(R.color.colorPrimary));
        events.add(event);

    }

    public void AddEvent(View v){
        Intent addEvent = new Intent(CalendarActivity.this, AjouterEventActivity.class);
        addEvent.putExtra("int_annee", annee);
        addEvent.putExtra("int_mois", mois);
        addEvent.putExtra("int_jours", jours);
        startActivity(addEvent);
    }
}

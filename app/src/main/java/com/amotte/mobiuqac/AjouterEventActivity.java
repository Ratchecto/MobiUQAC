package com.amotte.mobiuqac;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AjouterEventActivity extends AppCompatActivity {
    TextView date;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add__event_);
        date=(TextView) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AjouterEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Date d = new Date(year,view.getMonth(),dayOfMonth);
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, view.getMonth(),dayOfMonth);
                                d =calendar.getTime();
                                date.setText(new SimpleDateFormat("d MMMM  HH:MM").format(d));

                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }
}

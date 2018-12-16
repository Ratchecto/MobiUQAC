package com.amotte.mobiuqac;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AjouterEventActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int SELECT_IMAGE = 0;

    ImageView imageView ;

    TextView date;
    TextView heure;
    DatePickerDialog picker;
    TimePickerDialog timePicker;

    Date d = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);

        imageView = (ImageView) findViewById(R.id.imagaView);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(imageBitmap);

        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

        date=(TextView) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AjouterEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                d.setYear(year);
                                d.setMonth(monthOfYear);
                                d.setDate(dayOfMonth);
                                date.setText(new SimpleDateFormat("d MMMM").format(d));

                            }
                        }, year, month, day);
                picker.show();
            }
        });

        heure=(TextView) findViewById(R.id.heure);
        heure.setOnClickListener(new View.OnClickListener() {
            final Calendar cldr = Calendar.getInstance();
            int hour = cldr.get(Calendar.HOUR);
            int min = cldr.get(Calendar.MINUTE);
            @Override
            public void onClick(View v) {
                timePicker = new TimePickerDialog(AjouterEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hour, int minute) {
                                d.setHours(hour);
                                d.setMinutes(minute);
                                heure.setText(new SimpleDateFormat("HH:mm").format(d));
                            }
                        }, hour, min, true);
                timePicker.show();
            }
        });
    }


    public void startDialog(View view) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this, R.style.Theme_AppCompat_DayNight_Dialog);
        myAlertDialog.setTitle("Choix de l'image");
        myAlertDialog.setMessage("Où voulez-vous choisir votre image ?");

        myAlertDialog.setPositiveButton("Gallerie",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    }
                });

        myAlertDialog.setNegativeButton("Caméra",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                });
        myAlertDialog.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

                Uri uri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

    }


}

package com.amotte.mobiuqac;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AjouterEventActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int SELECT_IMAGE = 0;

    ImageView imageView ;
    private Uri filePath= null;
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    com.google.firebase.auth.FirebaseUser FirebaseUser;
    private boolean dateset= false;
    TextView date;
    EditText title;
    EditText description;
    TextView heure;
    String UID;
    DatePickerDialog picker;
    private DatabaseReference rootRef;
    TimePickerDialog timePicker;

    Date d = new Date();
    boolean timeSet = false;
    Uri uri = null;
    File destination;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser = mAuth.getCurrentUser();
        imageView = (ImageView) findViewById(R.id.imagaView);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(imageBitmap);

        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        destination = new File(Environment.getExternalStorageDirectory(), "image.jpg");

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
                                dateset = true;
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
                                timeSet = true;
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
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destination));
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }
                });
        myAlertDialog.show();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
                filePath = uri;


            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            uri = Uri.fromFile(destination);
            filePath = uri;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;


    }
    public void addevent (View v){
        if (dateset){
            if (title.getText().length() > 8){
                if (description.getText().length() > 8){
                    if (filePath != null){
                        uploadImage();


                    }else{
                        Toast.makeText(AjouterEventActivity.this, "Merci d'ajouter une image", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AjouterEventActivity.this, "Indiquer une description correcte", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(AjouterEventActivity.this, "Indiquer un nom correct", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AjouterEventActivity.this, "Indiquer une date et une heure correcte", Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadImage() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Ajout de l'événement");
            progressDialog.show();
            UID= UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/"+ UID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            uploadEvent();
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AjouterEventActivity.this, "Echec lors de l'envoie de l'image "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Envoi de l'image en cours...");
                        }
                    });
        }
    }
    private void uploadEvent(){
        rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference listeventref = rootRef.child("events");
        DatabaseReference eventref = listeventref.child(d.getTime()+FirebaseUser.getUid());
        Evenement e = new Evenement(title.getText().toString(), d, description.getText().toString(),UID);
        EditText emplacement = (EditText) findViewById(R.id.emplacement);
        e.setLocalisation(emplacement.getText().toString());
        e.setUid(FirebaseUser.getUid());
        eventref.setValue(e);

        setResult(Activity.RESULT_OK);
        finish();
    }

}

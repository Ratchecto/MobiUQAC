package com.amotte.mobiuqac;


import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AjouterEventActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int SELECT_IMAGE = 0;

    ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__event_);

        imageView = (ImageView) findViewById(R.id.imagatest);
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder);
        imageView.setImageBitmap(imageBitmap);

        imageView.getLayoutParams().height = 300;
        imageView.getLayoutParams().width = 300;

    }

    public void onClick(View view) {
        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    public void takePhoto(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void startDialog(View view) {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this, R.style.Theme_AppCompat_DayNight_Dialog);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");



        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
// Show only images, no videos or anything else
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    }
                });

        myAlertDialog.setNegativeButton("Camera",
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

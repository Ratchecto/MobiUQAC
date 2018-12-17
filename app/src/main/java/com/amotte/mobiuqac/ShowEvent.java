package com.amotte.mobiuqac;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ShowEvent extends Activity {

    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    com.google.firebase.auth.FirebaseUser FirebaseUser;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser = mAuth.getCurrentUser();
        String title = getIntent().getExtras().getString("titre");
        String description = getIntent().getExtras().getString("description");
        String date = getIntent().getExtras().getString("date");
        String thumbnail = getIntent().getExtras().getString("thumbnail");
        String localisation = getIntent().getExtras().getString("localisation");
        String uid = getIntent().getExtras().getString("uid");
        id=getIntent().getExtras().getString("id");


        TextView tvTitle = (TextView) findViewById(R.id.eventTitle);
        tvTitle.setText(title);
        ImageView imgEvent = (ImageView) findViewById(R.id.thumbnail);
        Button btnsupp =  findViewById(R.id.supp);
        if(uid.equals(FirebaseUser.getUid())){
            btnsupp.setVisibility(View.VISIBLE);
        }
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference ref = storageReference.child("images/"+ thumbnail);

        GlideApp.with(this)
                .load(ref)
                .into(imgEvent);

        TextView evtDate = (TextView) findViewById(R.id.eventDate);
        evtDate.setText(date);
        TextView evtLocal = (TextView) findViewById(R.id.eventLocalisation);
        if(localisation == null)
            evtLocal.setText("Pas de localisation pour le moment");
        else
            evtLocal.setText(localisation);
        TextView evtDescription = (TextView) findViewById(R.id.eventDescription);
        evtDescription.setText(description);


    }
    public void removeEvent(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Supprimer l'événement ?")
                .setMessage("Voulez-vous vraiment supprimer l'événement ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().child("events").child(id);
                        Log.e("ccccc",id);
                        dbNode.removeValue();
                        finish();
                    }

                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}

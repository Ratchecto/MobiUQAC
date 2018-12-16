package com.amotte.mobiuqac;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class ShowEvent extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);

        String title = getIntent().getExtras().getString("titre");
        String description = getIntent().getExtras().getString("description");
        String date = getIntent().getExtras().getString("date");
        String thumbnail = getIntent().getExtras().getString("thumbnail");
        String localisation = getIntent().getExtras().getString("localisation");

        TextView tvTitle = (TextView) findViewById(R.id.eventTitle);
        tvTitle.setText(title);
        ImageView imgEvent = (ImageView) findViewById(R.id.thumbnail);
        Picasso.with(this).load(thumbnail)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
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
}

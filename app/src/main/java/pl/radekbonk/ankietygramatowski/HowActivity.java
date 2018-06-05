package pl.radekbonk.ankietygramatowski;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HowActivity extends AppCompatActivity implements View.OnClickListener {
    public SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String localization;
    private String how;

    private DateFormat dfYear;
    private DateFormat dfMonth;
    private DateFormat dfTime;

    private String year;
    private String month;
    private String time;

    private Answer answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_layout);

        Locale locale = new Locale("polish", "poland");

        dfYear = new SimpleDateFormat("yyyy", locale);
        year = dfYear.format(Calendar.getInstance().getTime());
        dfMonth = new SimpleDateFormat("MM", locale);
        month = dfMonth.format(Calendar.getInstance().getTime());
        dfTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", locale);
        time = dfTime.format(Calendar.getInstance().getTime());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        Bundle bundle = getIntent().getExtras();
        localization = bundle.getString("localization");

        Button googleButton = (Button)findViewById(R.id.googleButton);
        googleButton.setOnClickListener(this);
        Button facebookButton = (Button)findViewById(R.id.facebookButton);
        facebookButton.setOnClickListener(this);
        Button webButton = (Button)findViewById(R.id.webButton);
        webButton.setOnClickListener(this);
        Button bannerButton = (Button)findViewById(R.id.bannerButton);
        bannerButton.setOnClickListener(this);
        Button standButton = (Button)findViewById(R.id.standButton);
        standButton.setOnClickListener(this);
        Button radioButton = (Button)findViewById(R.id.radioButton);
        radioButton.setOnClickListener(this);
        Button clientButton = (Button)findViewById(R.id.clientButton);
        clientButton.setOnClickListener(this);
        Button driveByButton = (Button)findViewById(R.id.driveByButton);
        driveByButton.setOnClickListener(this);
        Button fromFriendButton = (Button)findViewById(R.id.fromFriendButton);
        fromFriendButton.setOnClickListener(this);
        Button otherButton = (Button)findViewById(R.id.otherButton);
        otherButton.setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.googleButton:
                how = "google";
                break;
            case R.id.facebookButton:
                how = "facebook";
                break;
            case R.id.webButton:
                how = "web";
                break;
            case R.id.bannerButton:
                how = "banner";
                break;
            case R.id.standButton:
                how = "stand";
                break;
            case R.id.radioButton:
                how ="radio";
                break;
            case R.id.clientButton:
                how = "client";
                break;
            case R.id.driveByButton:
                how = "driveBy";
                break;
            case R.id.fromFriendButton:
                how = "fromFriend";
                break;
            case R.id.otherButton:
                how = "other";
                break;
        }
        Intent intent = new Intent(HowActivity.this, WhatActivity.class);
        intent.putExtra("localization",localization);
        intent.putExtra("how",how);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
    }
}

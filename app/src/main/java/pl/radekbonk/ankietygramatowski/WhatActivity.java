package pl.radekbonk.ankietygramatowski;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WhatActivity extends AppCompatActivity implements View.OnClickListener{
    public SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String localization;
    private String how;
    private String what;

    private DateFormat dfYear;
    private DateFormat dfMonth;
    private DateFormat dfTime;

    private String year;
    private String month;
    private String time;

    private Answer answer;

    private CountDownTimer countDownTimer;

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
        setContentView(R.layout.what_layout);

        Locale locale = new Locale("polish", "poland");

        dfYear = new SimpleDateFormat("yyyy", locale);
        year = dfYear.format(Calendar.getInstance().getTime());
        dfMonth = new SimpleDateFormat("MM", locale);
        month = dfMonth.format(Calendar.getInstance().getTime());
        dfTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", locale);
        time = dfTime.format(Calendar.getInstance().getTime());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle bundle = getIntent().getExtras();
        localization = bundle.getString("localization");
        how = bundle.getString("how");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(localization +"/"+year+"/"+month);

        Button serviceButton = (Button)findViewById(R.id.serviceButton);
        serviceButton.setOnClickListener(this);
        Button sellerButton = (Button)findViewById(R.id.sellerButton);
        sellerButton.setOnClickListener(this);
        Button sellerServiceButton = (Button)findViewById(R.id.sellerServiceButton);
        sellerServiceButton.setOnClickListener(this);

        answer = new Answer(localization,how,"0",0,time);

        countDownTimer = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("Time","Seconds remain: " +millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                databaseReference.push().setValue(answer);
                Intent intent = new Intent(WhatActivity.this,HowActivity.class);
                intent.putExtra("localization",localization);
                startActivity(intent);
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.serviceButton:
                what = "service";
                break;
            case R.id.sellerButton:
                what = "seller";
                break;
            case R.id.sellerServiceButton:
                what = "sellerService";
                break;
        }
        countDownTimer.cancel();
        if(localization.equals("citroenElblag") || localization.equals("kiaStarogard")) {
            Intent intent = new Intent(WhatActivity.this, RatingCitroenActivity.class);
            intent.putExtra("localization", localization);
            intent.putExtra("how", how);
            intent.putExtra("what", what);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WhatActivity.this, RatingActivity.class);
            intent.putExtra("localization", localization);
            intent.putExtra("how", how);
            intent.putExtra("what", what);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }
}

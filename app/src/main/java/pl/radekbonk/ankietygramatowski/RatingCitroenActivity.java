package pl.radekbonk.ankietygramatowski;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RatingCitroenActivity extends AppCompatActivity implements View.OnClickListener {
    public SharedPreferences sharedPreferences;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference email1Reference;
    private DatabaseReference email2Reference;
    private DatabaseReference messageReference;
    private DatabaseReference message2Reference;

    private String localization;
    private String how;
    private String what;
    private long rating;
    private String email;
    private String email2;

    private DateFormat dfYear;
    private DateFormat dfMonth;
    private DateFormat dfTime;

    private String year;
    private String month;
    private String time;

    private Answer answer;

    private CountDownTimer countDownTimer;

    private String miejsce, kto;

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
        setContentView(R.layout.rating_layout_citroen);

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
        what = bundle.getString("what");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(localization + "/" + year + "/" + month);

        Button bad = (Button) findViewById(R.id.bad);
        bad.setOnClickListener(this);
        Button neutral = (Button) findViewById(R.id.neutral);
        neutral.setOnClickListener(this);
        Button good = (Button) findViewById(R.id.good);
        good.setOnClickListener(this);

        answer = new Answer(localization, how, "0", 0, time);

        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.i("Time", "Second remain: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                databaseReference.push().setValue(answer);
                Intent intent = new Intent(RatingCitroenActivity.this, HowActivity.class);
                intent.putExtra("localization", localization);
                startActivity(intent);
            }
        };

        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bad:
                rating = 1;
                break;
            case R.id.neutral:
                rating = 3;
                break;
            case R.id.good:
                rating = 5;
                break;
        }
        countDownTimer.cancel();
        messageReference = firebaseDatabase.getReference(localization + "/" + "message1");
        message2Reference = firebaseDatabase.getReference(localization + "/" + "message2");

        if (rating > 1) {
            messageReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String msg = dataSnapshot.getValue(String.class);
                    Log.i("MSG", msg);
                    showDialog(msg);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (rating == 1) {
            message2Reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String msg = dataSnapshot.getValue(String.class);
                    Log.i("MSG", msg);
                    showDialog(msg);
                    sendMail();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        answer = new Answer(localization, how, what, rating, time);
        databaseReference.push().setValue(answer);

    }

    public void showDialog(String message) {
        final AlertDialog alertDialog = new AlertDialog.Builder(RatingCitroenActivity.this)
                .setTitle("Dziękujemy")
                .setMessage(message)
                .setCancelable(false)
                .create();

        alertDialog.show();

        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                alertDialog.dismiss();
                Intent intent = new Intent(RatingCitroenActivity.this, HowActivity.class);
                intent.putExtra("localization", localization);
                startActivity(intent);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onBackPressed() {
    }

    public void sendMail() {

        switch (localization) {
            case "skodaElblag":
                miejsce = "Skoda Elbląg";
                email1Reference = firebaseDatabase.getReference(localization + "/" + "email1");
                email2Reference = firebaseDatabase.getReference(localization + "/" + "email2");
                break;
            case "citroenElblag":
                miejsce = "Citroen Elbląg";
                email1Reference = firebaseDatabase.getReference(localization + "/" + "email1");
                email2Reference = firebaseDatabase.getReference(localization + "/" + "email2");
                break;
            case "skodaStarogard":
                miejsce = "Skoda Starogard";
                email1Reference = firebaseDatabase.getReference(localization + "/" + "email1");
                email2Reference = firebaseDatabase.getReference(localization + "/" + "email2");
                break;
            case "kiaStarogard":
                miejsce = "Kia Starogard";
                email1Reference = firebaseDatabase.getReference(localization + "/" + "email1");
                email2Reference = firebaseDatabase.getReference(localization + "/" + "email2");
                break;
            case "test":
                miejsce = "test";
                email1Reference = firebaseDatabase.getReference(localization + "/" + "email1");
                email2Reference = firebaseDatabase.getReference(localization + "/" + "email2");
                break;
        }

        switch (what) {
            case "service":
                kto = "klienta serwisu";
                break;
            case "seller":
                kto = "klienta salonu";
                break;
            case "sellerService":
                kto = "klienta serwisu i salonu";
                break;
        }
        email1Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.getValue(String.class);
                Log.i("email", email);
                try {
                    MailSender sender1 = new MailSender(RatingCitroenActivity.this, email, "Negatywna Opinia: " + miejsce + "!", "Właśnie nadeszła negatywna opinia \nOd: " + kto + "\nCzas: " + time + "\nSatysfakcja: " + rating);
                    sender1.execute();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        email2Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email2 = dataSnapshot.getValue(String.class);
                Log.i("email", email2);
                try {
                    MailSender sender2 = new MailSender(RatingCitroenActivity.this, email2, "Negatywna Opinia: " + miejsce + "!", "Właśnie nadeszła negatywna opinia \nOd: " + kto + "\nCzas: " + time + "\nSatysfakcja: " + rating);
                    sender2.execute();
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

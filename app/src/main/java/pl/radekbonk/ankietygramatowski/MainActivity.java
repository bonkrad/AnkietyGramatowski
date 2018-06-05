package pl.radekbonk.ankietygramatowski;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public SharedPreferences sharedPreferences;
    public static int RC_SIGN_IN = 1;
    private String localization;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        Button skodaElblagButton = (Button) findViewById(R.id.skodaElblagButton);
        skodaElblagButton.setOnClickListener(this);
        Button citroenElblagButton = (Button) findViewById(R.id.citroenElblagButton);
        citroenElblagButton.setOnClickListener(this);
        Button skodaStarogardButton = (Button) findViewById(R.id.skodaStarogardButton);
        skodaStarogardButton.setOnClickListener(this);
        Button kiaStarogardButton = (Button) findViewById(R.id.kiaStarogardButton);
        kiaStarogardButton.setOnClickListener(this);
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setOnClickListener(this);
        /*Button adminButton = (Button) findViewById(R.id.adminButton);
        adminButton.setOnClickListener(this);*/

        mAuthStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "Zalogowano", Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(new IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Zalogowano", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Anulowano", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (v.getId()) {
            case R.id.skodaElblagButton:
                //editor.putString("menu", "skodaElblag");
                //editor.apply();
                localization = "skodaElblag";
                break;
            case R.id.citroenElblagButton:
                //editor.putString("menu", "skodaElblag");
                //editor.apply();
                localization = "citroenElblag";
                break;
            case R.id.skodaStarogardButton:
                //editor.putString("menu", "skodaStarogard");
                //editor.apply();
                localization = "skodaStarogard";
                break;
            case R.id.kiaStarogardButton:
                //editor.putString("menu", "kiaStarogard");
                //editor.apply();
                localization = "kiaStarogard";
                break;
            case R.id.testButton:
                //editor.putString("menu", "test");
                //editor.apply();
                localization = "test";
                break;
            /*case R.id.adminButton:
                //editor.putString("menu", "admin");
                //editor.apply();
                break;*/
        }
        //if (v.getId() != R.id.adminButton) {
            Intent intent = new Intent(MainActivity.this, HowActivity.class);
            intent.putExtra("localization",localization);
            startActivity(intent);
        /*} else if (v.getId() == R.id.adminButton) {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        }*/
    }
}

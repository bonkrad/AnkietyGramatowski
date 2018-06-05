package pl.radekbonk.ankietygramatowski;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReferenceMain;

    private List<String> salons;
    private List<String> years;
    private List<String> months;

    private Spinner salonSpinner;
    private Spinner yearSpinner;
    private Spinner monthSpinner;

    private Answer answersData;

    private int[] sellerResult;
    private int[] serviceResult;
    private int[] sellerServiceResult;
    private int[] howResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReferenceMain = firebaseDatabase.getReference();

        salonSpinner = (Spinner) findViewById(R.id.salonSpinner);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        monthSpinner = (Spinner) findViewById(R.id.monthSpinner);

        Button generateButton = (Button) findViewById(R.id.generateButton);

        salons = new ArrayList<>();
        years = new ArrayList<>();
        months = new ArrayList<>();

        sellerResult=new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        serviceResult =new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        sellerServiceResult =new int[] {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        howResult =new int[] {0,0,0,0,0,0,0,0,0,0};

        databaseReferenceMain.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                salons.add(dataSnapshot.getKey());
                ArrayAdapter<String> adapterSalons = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_spinner_dropdown_item, salons);
                salonSpinner.setAdapter(adapterSalons);
                salonSpinner.setSelection(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        salonSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                years.clear();
                DatabaseReference databaseReferenceSalon = firebaseDatabase.getReference("/" + parent.getSelectedItem().toString());
                databaseReferenceSalon.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        years.add(dataSnapshot.getKey());
                        ArrayAdapter<String> adapterYears = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_spinner_dropdown_item, years);
                        yearSpinner.setAdapter(adapterYears);
                        yearSpinner.setSelection(0);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        yearSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                months.clear();
                DatabaseReference databaseReferenceMonth = firebaseDatabase.getReference("/" + salonSpinner.getSelectedItem().toString() + "/" + parent.getSelectedItem().toString());
                databaseReferenceMonth.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        months.add(dataSnapshot.getKey());
                        ArrayAdapter<String> adapterMonths = new ArrayAdapter<String>(AdminActivity.this, android.R.layout.simple_spinner_dropdown_item, months);
                        monthSpinner.setAdapter(adapterMonths);
                        monthSpinner.setSelection(0);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        generateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedSalon;
                String selectedYear;
                String selectedMonth;

                selectedSalon = salonSpinner.getSelectedItem().toString();
                selectedYear = yearSpinner.getSelectedItem().toString();
                selectedMonth = monthSpinner.getSelectedItem().toString();

                Log.i("Salon",selectedSalon);
                Log.i("Rok",selectedYear);
                Log.i("Miesiac",selectedMonth);

                DatabaseReference databaseReferenceData = firebaseDatabase.getReference("/"+selectedSalon+"/"+selectedYear+"/"+selectedMonth);
                databaseReferenceData.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        answersData = dataSnapshot.getValue(Answer.class);
                        System.out.println(answersData.getHow());
                        System.out.println(answersData.getWhat());
                        System.out.println(answersData.getRating());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void addHow() {

    }
    public void addWhat() {

    }
    public void addRating() {

    }
}

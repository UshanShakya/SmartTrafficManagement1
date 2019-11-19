package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttrafficmanagement1.Class.Reports;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReportActivity extends AppCompatActivity {
    private CardView cvAccident, cvConstruction, cvTrafficJam, cvOthers;
    private TextView placeName;
    Reports reports;
    DatabaseReference databaseReference;
    static String userName;
    long maxId =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        cvAccident= findViewById(R.id.cvAccident);
        cvConstruction= findViewById(R.id.cvConstruction);
        cvTrafficJam = findViewById(R.id.cvTraffic);
        cvOthers = findViewById(R.id.cvOthers);
        placeName = findViewById(R.id.tvplaceName);


        databaseReference= FirebaseDatabase.getInstance().getReference().child("Reports");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    maxId= (dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userName = user.getEmail();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String address = bundle.getString("address");
            placeName.setText(address);
            Toast.makeText(this, "Address : "+address, Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText(this, "No message.", Toast.LENGTH_SHORT).show();
        }

        cvAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Accident");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Accident Reported Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cvConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Construction");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Construction Reported Successfully", Toast.LENGTH_SHORT).show();


            }
        });

        cvTrafficJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Traffic Jam");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Traffic Jam Reported Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        cvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reports.setLocation(placeName.getText().toString().trim());
                reports.setReport("Others");
                reports.setUserName(userName);
                databaseReference.push().setValue(reports);
                databaseReference.child(String.valueOf(maxId+1)).setValue("Reports");
                Toast.makeText(ReportActivity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

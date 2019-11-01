package com.example.smarttrafficmanagement1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {
    private CardView cvAccident, cvConstruction, cvTrafficJam, cvOthers;
    private TextView placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        cvAccident= findViewById(R.id.cvAccident);
        cvConstruction= findViewById(R.id.cvConstruction);
        cvTrafficJam = findViewById(R.id.cvTraffic);
        cvOthers = findViewById(R.id.cvOthers);
        placeName = findViewById(R.id.tvplaceName);
        cvAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                reports.setLocation(placeName.getText().toString().trim());
//                reports.setReport("Accident");
//                reports.setUserName(userName);
//                databaseReference.push().setValue(reports);
                Toast.makeText(ReportActivity.this, "Accident Reported Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        cvConstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                reports.setLocation(placeName.getText().toString().trim());
//                reports.setReport("Construction");
//                reports.setUserName(userName);
//                databaseReference.push().setValue(reports);
                Toast.makeText(ReportActivity.this, "Construction Reported Successfully", Toast.LENGTH_SHORT).show();


            }
        });

        cvTrafficJam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                reports.setLocation(placeName.getText().toString().trim());
//                reports.setReport("Traffic Jam");
//                reports.setUserName(userName);
//                databaseReference.push().setValue(reports);
                Toast.makeText(ReportActivity.this, "Traffic Jam Reported Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        cvOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                reports.setLocation(placeName.getText().toString().trim());
//                reports.setReport("Others");
//                reports.setUserName(userName);
//                databaseReference.push().setValue(reports);
                Toast.makeText(ReportActivity.this, "Reported Successfully", Toast.LENGTH_SHORT).show();

            }
        });



    }
}

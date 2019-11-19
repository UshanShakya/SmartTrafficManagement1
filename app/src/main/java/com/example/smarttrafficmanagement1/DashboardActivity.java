package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smarttrafficmanagement1.Adapter.ReportsAdapter;
import com.example.smarttrafficmanagement1.Class.Reports;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ListView reportsListView;
    DatabaseReference databaseReference;
    List<Reports> reportsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        reportsListView = findViewById(R.id.reportsListView);
        reportsList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportsList.clear();
                for (DataSnapshot reportsSnapshot: dataSnapshot.getChildren()){
                    Reports reports = reportsSnapshot.getValue(Reports.class);
                    reportsList.add(reports);
                }

                ReportsAdapter adapter = new ReportsAdapter(DashboardActivity.this, reportsList);
                reportsListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

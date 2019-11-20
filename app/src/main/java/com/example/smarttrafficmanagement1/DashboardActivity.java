package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.smarttrafficmanagement1.Adapter.ReportsAdapter;
import com.example.smarttrafficmanagement1.Class.RecyclerViewConfig;
import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.DatabaseReference.FirebaseDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mRecyclerView = findViewById(R.id.reportsRecyclerView);
        new FirebaseDatabaseHelper().readReports(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Reports> reportsList1, List<String> keys) {
                new RecyclerViewConfig().setConfig(mRecyclerView,DashboardActivity.this,reportsList1,keys);
            }

            @Override
            public void DataIsInserted() {

            }

            @Override
            public void DataIsUpdated() {

            }

            @Override
            public void DataIsDeleted() {

            }
        });

    }

}

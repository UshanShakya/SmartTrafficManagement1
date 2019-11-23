package com.example.smarttrafficmanagement1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smarttrafficmanagement1.Class.RecyclerViewConfig;
import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.DatabaseReference.FirebaseDatabaseHelper;

import java.util.List;

public class TrafficActivity extends AppCompatActivity {

    public RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mRecyclerView = findViewById(R.id.reportsRecyclerView);


    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(TrafficActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}

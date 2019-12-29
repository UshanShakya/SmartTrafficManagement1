package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttrafficmanagement1.Class.Density;
import com.example.smarttrafficmanagement1.Class.RecyclerViewConfig;
import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.DatabaseReference.FirebaseDatabaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TrafficActivity extends AppCompatActivity {

    public TextView tvDensity;
    public DatabaseReference mDatabaseReference;
    public Density density;
    public String ddensity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvDensity = findViewById(R.id.density);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("density");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ddensity = dataSnapshot.getValue().toString();
//                ddensity = density.getDensity();

                tvDensity.setText(ddensity);


                Toast.makeText(getApplicationContext(), "The density is " + ddensity, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(TrafficActivity.this, "Error "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent mainIntent = new Intent(TrafficActivity.this, MainActivity.class);
        startActivity(mainIntent);
    }
}

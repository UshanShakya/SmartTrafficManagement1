package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.smarttrafficmanagement1.Adapter.ReportsAdapter;
import com.example.smarttrafficmanagement1.Class.RecyclerViewConfig;
import com.example.smarttrafficmanagement1.Class.Reports;
import com.example.smarttrafficmanagement1.DatabaseReference.FirebaseDatabaseHelper;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    List<AuthUI.IdpConfig> providers;
    private CardView btnReport, btnTraffic, btnLogout;
    private final static int MY_REQUEST_CODE =0404;
    private long backPressedTime;
    private Toast backtoast;

    private RecyclerView mRecyclerView;
    private ReportsAdapter reportsAdapter;

    private DatabaseReference mDatabaseRef;
    private List<Reports> mReports;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = findViewById(R.id.btn_sign_out);
        btnReport = findViewById(R.id.btn_report);
        btnTraffic = findViewById(R.id.btn_check_traffic);
        mRecyclerView = findViewById(R.id.reportsRecyclerView1);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mReports = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Report");


        sharedPreferences = getSharedPreferences("User",MODE_PRIVATE);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    Reports reports = postSnapshot.getValue(Reports.class);
                    mReports.add(reports);
                }
                reportsAdapter = new ReportsAdapter(MainActivity.this,mReports);
                mRecyclerView.setAdapter(reportsAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        showSignInOptions();
        checkForDatabase();

    btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(MainActivity.this, PlaceActivity.class);
                startActivity(reportIntent);
            }
        });
        btnTraffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trafficIntent = new Intent(MainActivity.this, TrafficActivity.class);
                startActivity(trafficIntent);

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance()
                        .signOut(MainActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btnLogout.setEnabled(false);
                                showSignInOptions();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkForDatabase() {

        Query dayQuery = mDatabaseRef.orderByChild("date").equalTo(String.valueOf(new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis()-(1000 * 60 * 60 * 24))));
        dayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error","Cancelled",databaseError.toException());
            }
        });
    }


    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.MyTheme)
                        .build(),MY_REQUEST_CODE
        );
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime+2000> System.currentTimeMillis()){
            backtoast.cancel();
            super.onBackPressed();

            return;
        }
        else {
            backtoast=Toast.makeText(getBaseContext(), "Press Back Again to Exit", Toast.LENGTH_SHORT);
            backtoast.show();
        }
        backPressedTime = System.currentTimeMillis();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode==RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
                btnLogout.setEnabled(true);
                btnReport.setEnabled(true);
                mRecyclerView.setEnabled(true);
            }
            else{
                Toast.makeText(this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }


    }
}

package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<AuthUI.IdpConfig> providers;
    private CardView btnReport, btnDashboard, btnLogout;
    private final static int MY_REQUEST_CODE =0404;
    public RecyclerView mRecyclerView;
    private long backPressedTime;
    private Toast backtoast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnLogout = findViewById(R.id.btn_sign_out);
        btnReport = findViewById(R.id.btn_report);
        mRecyclerView = findViewById(R.id.reportsRecyclerView1);


        providers= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        new FirebaseDatabaseHelper().readReports(new FirebaseDatabaseHelper.DataStatus() {
                                                     @Override
                                                     public void DataIsLoaded(List<Reports> reportsList1, List<String> keys) {
                                                         new RecyclerViewConfig().setConfig(mRecyclerView, MainActivity.this, reportsList1, keys);
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
        showSignInOptions();


//        btnDashboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent dashboardIntent = new Intent(MainActivity.this, DashboardActivity.class);
//                startActivity(dashboardIntent);
//            }
//        });
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reportIntent = new Intent(MainActivity.this, PlaceActivity.class);
                startActivity(reportIntent);
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
                Toast.makeText(this, ""+user.getEmail(), Toast.LENGTH_SHORT).show();
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

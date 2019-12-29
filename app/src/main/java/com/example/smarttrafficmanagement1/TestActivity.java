package com.example.smarttrafficmanagement1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarttrafficmanagement1.Class.Density;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestActivity extends AppCompatActivity {
    TextView time;
    private DatabaseReference mDatabaseReference;
    private Density density;
//    HashMap<String, Object> times;
//Object asdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Density");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                density = dataSnapshot.getValue(Density.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(this, "" + density, Toast.LENGTH_SHORT).show();

//        time = findViewById(R.id.tvTime);

//        times = ServerValue.TIMESTAMP;
//
//        asdad = ServerValue.TIMESTAMP;
//        long date = getLong();
//        time.setText(String.valueOf(date));
//
//        Toast.makeText(this,
//                times.get(".sv"), Toast.LENGTH_LONG).show();
    }

//    private long getLong() {
//        return (long)asdad;
//    }
}

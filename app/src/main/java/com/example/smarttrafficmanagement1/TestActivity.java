package com.example.smarttrafficmanagement1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {
    TextView time;
//    HashMap<String, Object> times;
Object asdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        time = findViewById(R.id.tvTime);

//        times = ServerValue.TIMESTAMP;
//
        asdad = ServerValue.TIMESTAMP;
        long date = getLong();
        time.setText(String.valueOf(date));

//        Toast.makeText(this,
//                times.get(".sv"), Toast.LENGTH_LONG).show();
    }

    private long getLong() {
        return (long)asdad;
    }
}

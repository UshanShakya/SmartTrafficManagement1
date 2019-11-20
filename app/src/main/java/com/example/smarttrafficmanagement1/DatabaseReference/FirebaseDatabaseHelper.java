package com.example.smarttrafficmanagement1.DatabaseReference;

import androidx.annotation.NonNull;

import com.example.smarttrafficmanagement1.Class.Reports;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper  {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private List<Reports> reportsList= new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Reports> reportsList1,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper(){
        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Report");
    }

    public void readReports(final DataStatus dataStatus){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reportsList.clear();
                List<String> keys =  new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Reports reports= keyNode.getValue(Reports.class);
                    reportsList.add(reports);
                }
                dataStatus.DataIsLoaded(reportsList,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

package com.example.parkease_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.parkease_admin.object.Parking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ParkingHistoryActivity extends AppCompatActivity {
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> lvAdapter;
    DatabaseReference databaseUsers, databaseParking;
    Spinner spinnerParking;
    ListView lvParkingHistory;
    List<String> parkingHistorys;
    List<Parking> parkings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_history);
        //init ui
        spinnerParking = findViewById(R.id.spinner_parkingHistory_parkingID);
        lvParkingHistory = findViewById(R.id.lv_parkingHistory_history);

        databaseParking = FirebaseDatabase.getInstance().getReference("parking");
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        parkings = new ArrayList<>();
        parkingHistorys = new ArrayList<>();

        lvAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        lvParkingHistory.setAdapter(lvAdapter);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        spinnerParking.setAdapter(spinnerAdapter);

        databaseParking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkings.clear();
                spinnerAdapter.clear();
                for(DataSnapshot parkingDataSnapshot : snapshot.getChildren()){
                    Parking parking = parkingDataSnapshot.getValue(Parking.class);
                    parkings.add(parking);
                    spinnerAdapter.add(parking.getParkingSpaceID());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinnerParking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parkingHistorys.clear();
                lvAdapter.clear();
                for (String parkingHistory : parkings.get(position).getParkingHistory()){
                    parkingHistorys.add(parkingHistory);
                    lvAdapter.add(parkingHistory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
package com.example.parkease_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.parkease_admin.object.User;
import com.example.parkease_admin.object.parking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParkingDetailActivity extends AppCompatActivity {

    DatabaseReference databaseParkings = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("parking");
    DatabaseReference databaseUsers = FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("users");;
    String parkingID;
    TextView tvLocation, tvPrice, tvUser, tvParkingID;
    ToggleButton tbStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_detail);

        Intent intent = getIntent();
        parkingID = intent.getStringExtra("parkingID");

        //init ui
        tvLocation = findViewById(R.id.tv_parkingDetail_location);
        tvParkingID = findViewById(R.id.tv_parkingDetail_id);
        tvUser = findViewById(R.id.tv_parkingDetail_user);
        tvPrice = findViewById(R.id.tv_parkingDetail_price);
        tbStatus = findViewById(R.id.tb_parkingDetail_status);




        databaseParkings.child(parkingID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                parking currentParking = task.getResult().getValue(parking.class);

                tvParkingID.setText(currentParking.getParkingSpaceID());
                tvPrice.setText(String.format("%.2f", currentParking.getPrice()));

                if(currentParking.getCurrentUser().equals("none")){
                    tvUser.setText("none");
                }else{
                    databaseUsers.child(currentParking.getCurrentUser()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            User currentUser = task.getResult().getValue(User.class);
                            tvUser.setText(currentUser.getUserId() + " " + currentUser.getUserName());
                        }
                    });
                }

                if(currentParking.getStatus()){
                    tbStatus.setChecked(false);
                    tbStatus.setBackgroundColor(Color.RED);
                    tbStatus.setText("Unavailable");
                }else{
                    tbStatus.setChecked(true);
                    tbStatus.setBackgroundColor(Color.GREEN);
                    tbStatus.setText("Available");
                }

                tvLocation.setText(getAddress(currentParking.getLatitude(), currentParking.getLongitude()));


            }
        });

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(ParkingDetailActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getLocality() + ", " + obj.getAdminArea();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ParkingDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }
}
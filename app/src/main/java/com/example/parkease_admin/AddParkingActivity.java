package com.example.parkease_admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.parkease_admin.object.parking;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddParkingActivity extends AppCompatActivity {
    DatabaseReference databaseUsers;

    TextView tvParkingID, tvLongitude, tvLatitude, tvPrice;
    Button btnConfirm;
    String ParkingID;
    double longitude = 0.00;
    double latitude = 0.00;
    double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);

        databaseUsers= FirebaseDatabase.getInstance("https://parkease-1a60f-default-rtdb.firebaseio.com/").getReference("parking");

        //init ui
        tvParkingID = findViewById(R.id.et_addParking_parkingID);
        tvLatitude = findViewById(R.id.et_addParking_latitude);
        tvLongitude = findViewById(R.id.et_addParking_longitude);
        tvPrice = findViewById(R.id.et_addParking_price);

        btnConfirm = findViewById(R.id.btn_addParking_confirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParkingID = tvParkingID.getText().toString().trim();
                latitude = Double.parseDouble(tvLatitude.getText().toString());
                longitude = Double.parseDouble(tvLongitude.getText().toString());
                price = Double.parseDouble(tvPrice.getText().toString());
                if(ParkingID.isEmpty() || latitude == 0.00 || longitude == 0.00 || price == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddParkingActivity.this);
                    builder.setMessage("Please enter all field").setTitle("Warning").setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    parking newParking = new parking(
                        ParkingID,
                        "none",
                        false,
                        longitude,
                        latitude,
                        price
                    );

                    databaseUsers.child(ParkingID).setValue(newParking);

                    goToMainActivity();
                }
            }
        });



    }

    private void goToMainActivity(){
        Intent intent = new Intent(AddParkingActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
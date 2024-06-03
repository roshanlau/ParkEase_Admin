package com.example.parkease_admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    Button btnAddParking;
    Button btnScanQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddParking = findViewById(R.id.btn_main_addParking);

        btnAddParking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddParkingActivity.class);
                startActivity(intent);
            }
        });

        btnScanQR = findViewById(R.id.btn_main_scanQr);

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a QR code");
                intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.QR_CODE);
                intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setTorchEnabled(true);
                intentIntegrator.initiateScan();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(intentResult != null){
            String contents = intentResult.getContents();
            if(contents != null){
                //start pay parking activity
                //find parking status in firebase, if available then proceed to payment
                //add extra the parking id from the qrscanner
                Intent startIntent = new Intent(MainActivity.this, ParkingDetailActivity.class);
                startIntent.putExtra("parkingID", intentResult.getContents());
                startActivity(startIntent);
            }
        }else{
            Toast.makeText(MainActivity.this, "Qr Code not valid", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}
package com.example.parkease_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parkease_admin.object.Parking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GenerateQrCodeActivity extends AppCompatActivity {
    private ArrayAdapter<String> spinnerAdapter;
    List<Parking> parkings;
    DatabaseReference databaseParkings;
    Button btnGenerate, btnDownload;
    ImageView imgQrCode;
    Spinner spinnerParking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);
        //init ui
        btnGenerate = findViewById(R.id.btn_generateQr_generate);
        imgQrCode = findViewById(R.id.img_generateQr_qrCode);
        spinnerParking = findViewById(R.id.spinner_generateQr_parkingID);
        btnDownload = findViewById(R.id.btn_generateQr_download);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        spinnerParking.setAdapter(spinnerAdapter);

        databaseParkings = FirebaseDatabase.getInstance().getReference("parking"    );

        parkings =new ArrayList<>();

        databaseParkings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkings.clear();
                spinnerAdapter.clear();
                for(DataSnapshot parkingDataSnapshot : snapshot.getChildren()){
                    Parking parking = parkingDataSnapshot.getValue(Parking.class);

                    parkings.add(parking);
                    spinnerAdapter.add(parking.getParkingSpaceID() + "\n" + getAddress(parking.getLatitude(), parking.getLongitude()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //does nothing
            }
        });

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String parkingID = parkings.get(spinnerParking.getSelectedItemPosition()).getParkingSpaceID();

                MultiFormatWriter mWriter = new MultiFormatWriter();

                try {
                    //BitMatrix class to encode entered text and set Width & Height
                    BitMatrix mMatrix = mWriter.encode(parkingID, BarcodeFormat.QR_CODE, 400,400);
                    BarcodeEncoder mEncoder = new BarcodeEncoder();
                    Bitmap mBitmap = mEncoder.createBitmap(mMatrix);//creating bitmap of code
                    imgQrCode.setImageBitmap(mBitmap);//Setting generated QR code to imageView

                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });



        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable draw = (BitmapDrawable) imgQrCode.getDrawable();
                Bitmap bitmap = draw.getBitmap();

                FileOutputStream outStream = null;
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ParkEase");
                dir.mkdirs();
                String fileName = parkings.get(spinnerParking.getSelectedItemPosition()).getParkingSpaceID() + ".jpg" ;
                File outFile = new File(dir, fileName);
                try {
                    outStream = new FileOutputStream(outFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    outStream.flush();
                    outStream.close();
                }catch (FileNotFoundException e){
                    Toast.makeText(GenerateQrCodeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }catch (IOException e){
                    Toast.makeText(GenerateQrCodeActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(GenerateQrCodeActivity.this, "QR Code saved to gallery.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(GenerateQrCodeActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getLocality() + ", " + obj.getAdminArea();

            Log.v("IGA", "Address" + add);
            return add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(GenerateQrCodeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }
}
package com.example.parkease_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parkease_admin.object.Parking;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GenerateReportActivity extends AppCompatActivity {
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    DatabaseReference databaseParking;
    List<Parking> parkings;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
        databaseParking = FirebaseDatabase.getInstance().getReference("parking");

        parkings = new ArrayList<>();
        barEntries = new ArrayList<>();
        barChart = findViewById(R.id.barChart_generateReport);

        databaseParking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                parkings.clear();
                i = 0;
                barEntries.clear();
                for(DataSnapshot parkingDataSnapshot: snapshot.getChildren()){
                    Parking parking = parkingDataSnapshot.getValue(Parking.class);
                    parkings.add(parking);
                    barEntries.add(new BarEntry((float) i, parking.getParkingHistory().size()));
                    i++;
                }

                //for label
                String[] label = new String[parkings.size()];
                for(int j = 0; j < parkings.size(); j++){
                    label[j] = parkings.get(j).getParkingSpaceID();
                }

                barDataSet = new BarDataSet(barEntries, "Parking Report");
                barData = new BarData(barDataSet);



                barChart.setData(barData);
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);
                barChart.setFitBars(true);
                Description description =  barChart.getDescription();
                description.setEnabled(true);
                description.setText("frequency of parking");

                ValueFormatter formatter = new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        return label[(int) value];
                    }
                };

                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(formatter);

                barChart.invalidate(); //refresh


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
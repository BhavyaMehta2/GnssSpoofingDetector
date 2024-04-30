package com.app.navicspoofdetector;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurementsEvent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LocationManager locationManager;
    private GnssAdapter adapter = null;
    static Map<Integer, GnssSat> satellites;
    static List<GnssSat> listSatellites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        RecyclerView recyclerView = findViewById(R.id.gnssView);
        Button analyzeBtn = findViewById(R.id.analyzeBtn);

        if (satellites == null) {
            satellites = new HashMap<>();
        }
        if (listSatellites == null) {
            listSatellites = new ArrayList<>();
        }

        adapter = new GnssAdapter(new ArrayList<>(satellites.values()));
        adapter.setHasStableIds(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            startGNSSMeasurement();
        }

        analyzeBtn.setOnClickListener(view -> {
            double[][] res = analyzeSignals();
            Intent intent = new Intent(MainActivity.this, AnalyzeActivity.class);
            intent.putExtra("coeffs", res);
            moveTaskToBack(true);
            startActivity(intent);
        });
    }

    private void startGNSSMeasurement() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            }
            locationManager.registerGnssMeasurementsCallback(new android.location.GnssMeasurementsEvent.Callback() {
                @Override
                public void onGnssMeasurementsReceived(@NonNull GnssMeasurementsEvent event) {
                    for (GnssSat satellite : listSatellites) {
                        satellite.setCn0(0.00);
                    }

                    for (android.location.GnssMeasurement measurement : event.getMeasurements()) {
                        String carrierFrequency = (Math.round(measurement.getCarrierFrequencyHz() / Math.pow(10, 7)) == 118) ? "L5" : "L1";

                        GnssSat obj = satellites.getOrDefault(measurement.getConstellationType() * 10000 + measurement.getSvid(), null);

                        if (obj == null) {
                            obj = new GnssSat();
                            satellites.put(measurement.getConstellationType() * 10000 + measurement.getSvid(), obj);
                            listSatellites.add(obj);
                        }

                        obj.setId(measurement.getSvid());
                        obj.setConstellation(measurement.getConstellationType());
                        obj.setCF(carrierFrequency);
                        obj.setCn0(measurement.getCn0DbHz());
                        obj.setHash();
                    }

                    for (GnssSat satellite : listSatellites) {
                        satellite.setSignal();
                    }

                    updateUI();
                }
            });
        }
    }

    private double[][] analyzeSignals() {
        int K = listSatellites.size();
        double[][] coefficients = new double[K][K];

        GnssSat satelliteA, satelliteB;
        double[] a, b;
        double aSqrt, bSqrt;
        double sum;

        for (int i = 0; i < K; i++) {
            satelliteA = listSatellites.get(i);
            a = satelliteA.getNumeratorConstant();
            aSqrt = satelliteA.getDenominatorConstant();

            for (int j = 0; j < i+1; j++) {
                sum = 0;
                satelliteB = listSatellites.get(j);

                if (satelliteA.getMean() == 0 || satelliteB.getMean() == 0) {
                    coefficients[i][j] = -1;
                    coefficients[j][i] = -1;
                    continue;
                }

                b = satelliteB.getNumeratorConstant();
                bSqrt = satelliteB.getDenominatorConstant();

                for (int k = 0; k < b.length; k++) {
                    sum = sum + a[k] * b[k];
                }
                coefficients[i][j] = (double) Math.round(sum / (aSqrt * bSqrt) * 100.0) / 100.0;
                coefficients[j][i] = coefficients[i][j];
            }
        }

        return coefficients;
    }

    private void updateUI() {
        runOnUiThread(() -> adapter.updateList(listSatellites));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGNSSMeasurement();
            }
        }
    }
}

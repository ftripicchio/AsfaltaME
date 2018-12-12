package com.dam.asfaltame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.Modelo.Report;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ReportListActivity extends AppCompatActivity {

    AppRepository appRepository;
    List<Report> listReport;
    List<Report> closeByListReport;
    ReportAdapter rAdapter;
    ListView lv;
    SwitchCompat switchCompat;
    FusedLocationProviderClient mFusedLocationClient;
    double currentLat;
    double currentLong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        appRepository = AppRepository.getInstance(this);
        lv = findViewById(R.id.list_reports);
        switchCompat = findViewById(R.id.list_only_near);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        closeByListReport = new ArrayList<Report>();


        configureToolbar();
        //TODO: Traer los reportes de la base de datos y con ese arreglo armar un ReportAdapter, y seteralo como adapter del ListView
        Runnable r = new Runnable() {
            @Override
            public void run() {
                listReport = appRepository.reportDao.getAll();
                Log.d("Test",listReport.toString());
                rAdapter = new ReportAdapter(ReportListActivity.this,listReport);
                Log.d("Adapter", rAdapter.toString());
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      lv.setAdapter(rAdapter);
                                  }
                              }

                );
            }
        };

        Thread t = new Thread(r);
        t.start();

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                closeByListReport.clear();
                if (isChecked) {
                    if (ActivityCompat.checkSelfPermission(ReportListActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(ReportListActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ReportListActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    } else {
                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(ReportListActivity.this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if (location != null) {
                                            currentLat = location.getLatitude();
                                            currentLong = location.getLongitude();
                                            for (Report r : listReport) {
                                                Double distancia = distanceBetweenPoints(r.getLatitud(), r.getLongitud(), currentLat, currentLong);
                                                Log.d("Lat", ((Double)currentLat).toString());
                                                Log.d("Long", ((Double)currentLong).toString());
                                                Log.d("Lejos", ((Double)distancia).toString());
                                                if(distancia <= 1.0){
                                                    Log.d("Cerca", ((Double)distancia).toString());
                                                    closeByListReport.add(r);
                                                }
                                            }
                                            lv.setAdapter(new ReportAdapter(ReportListActivity.this,closeByListReport));
                                        } else {
                                            currentLat = 0;
                                            currentLong = 0;
                                        }
                                    }
                                });
                    }
                } else {
                    lv.setAdapter(new ReportAdapter(ReportListActivity.this,listReport));
                }
            }
        });


    }



    public double distanceBetweenPoints(double lat1, double long1, double lat2, double long2){
        double distance = 0;

        double dLat = Math.toRadians(lat2-lat1);
        double dLong = Math.toRadians(long2-long1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLong / 2) * Math.sin(dLong / 2);


        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        distance = 6371.0 * c; // Distance in km

        return distance;
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightGray));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.menuOptReportList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

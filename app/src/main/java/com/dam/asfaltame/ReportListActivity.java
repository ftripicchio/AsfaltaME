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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.Modelo.Report;
import com.dam.asfaltame.Modelo.ReportType;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ReportListActivity extends AppCompatActivity {

    AppRepository appRepository;
    List<Report> listReport;
    List<Report> closeByListReport;
    List<Report> filteredListReport;
    ReportAdapter rAdapter;
    ListView lv;
    SwitchCompat switchCompat;
    FusedLocationProviderClient mFusedLocationClient;
    double currentLat;
    double currentLong;
    Spinner filterType;
    ArrayAdapter<ReportType> rp;
    AppCompatButton seeMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        appRepository = AppRepository.getInstance(this);
        lv = findViewById(R.id.list_reports);
        switchCompat = findViewById(R.id.list_only_near);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        closeByListReport = new ArrayList<Report>();
        filterType = findViewById(R.id.list_type_filter);
        rp = new ArrayAdapter<ReportType>(this,android.R.layout.simple_spinner_dropdown_item, ReportType.values());
        filterType.setAdapter(rp);
        seeMap = findViewById(R.id.list_map_button);

        configureToolbar();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                listReport = appRepository.reportDao.getAll();
                filteredListReport = new ArrayList<>(listReport);
                rAdapter = new ReportAdapter(ReportListActivity.this,filteredListReport);
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

        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchCompat.isChecked()){
                    Intent i = new Intent(ReportListActivity.this,MapsActivity.class);
                    i.putExtra("mapType",4);
                    i.putParcelableArrayListExtra("reports",(ArrayList<Report>)filteredListReport);
                    startActivity(i);
                }else{
                    Intent i = new Intent(ReportListActivity.this,MapsActivity.class);
                    i.putExtra("mapType",3);
                    i.putParcelableArrayListExtra("reports",(ArrayList<Report>)filteredListReport);
                    startActivity(i);
                }
            }
        });

        filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(switchCompat.isChecked()){
                    filteredListReport.clear();
                    filteredListReport = filterByType(closeByListReport,rp.getItem(position));
                    lv.setAdapter(new ReportAdapter(ReportListActivity.this, filteredListReport));
                }else {
                    filteredListReport.clear();
                    filteredListReport = filterByType(listReport,rp.getItem(position));
                    lv.setAdapter(new ReportAdapter(ReportListActivity.this, filteredListReport));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                closeByListReport.clear();
                if (isChecked) {
                    onlyNear(listReport);
                } else {
                    filteredListReport.clear();
                    filteredListReport = filterByType(listReport,ReportType.valueOf(filterType.getSelectedItem().toString()));
                    lv.setAdapter(new ReportAdapter(ReportListActivity.this,filteredListReport));
                }
            }
        });


    }

    public ArrayList<Report> filterByType(List<Report> original, ReportType type){
            ArrayList<Report> filtered = new ArrayList<>();
        if (type == ReportType.TODOS) {
            filtered = new ArrayList<>(original);
        } else {

            for (Report r : original) {
                if (r.getReportType() == type) {
                    filtered.add(r);
                }
            }
        }
        return filtered;
    }

    public void onlyNear(final List<Report> original){
        final ArrayList<Report> near = new ArrayList<>();

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
                                for (Report r : original) {
                                    Double distancia = distanceBetweenPoints(r.getLatitud(), r.getLongitud(), currentLat, currentLong);
                                    if(distancia <= 1.0){
                                        near.add(r);
                                    }
                                }
                                closeByListReport = near;
                                filteredListReport.clear();
                                filteredListReport = filterByType(closeByListReport,ReportType.valueOf(filterType.getSelectedItem().toString()));
                                lv.setAdapter(new ReportAdapter(ReportListActivity.this,filteredListReport));
                            } else {
                                currentLat = 0;
                                currentLong = 0;
                            }
                        }
                    });
        }
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

package com.dam.asfaltame;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.Modelo.Report;
import com.dam.asfaltame.Modelo.ReportType;
import com.dam.asfaltame.Modelo.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class NewReportActivity extends AppCompatActivity {

    Button otherLocation, actualLocation, createReport;
    Spinner reportType;
    EditText description;
    FusedLocationProviderClient mFusedLocationClient;
    Report newReport;
    AppRepository appRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        newReport = new Report();
        appRepository = AppRepository.getInstance(this);

        configureToolbar();
        getViews();
        setListeners();
    }

    private void getViews(){
        otherLocation = findViewById(R.id.other_location_button);
        actualLocation = findViewById(R.id.actual_location_button);
        reportType = findViewById(R.id.new_report_type_input);
        description = findViewById(R.id.new_description_input);
        createReport = findViewById(R.id.new_create_report);
    }

    private void setListeners(){
        otherLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i  = new Intent(NewReportActivity.this, MapsActivity.class);
                i.putExtra("mapType", 1);
                startActivityForResult(i, 1);
            }
        });

        actualLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(NewReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(NewReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewReportActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},9999);
                    return;
                } else {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(NewReportActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                newReport.setLatitud(location.getLatitude());
                                newReport.setLongitud(location.getLongitude());
                            }
                        }
                    });
                }
            }
        });

        ArrayAdapter<ReportType> reportTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ReportType.values());
        reportType.setAdapter(reportTypeAdapter);

        createReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newReport.setStatus(Status.ACTIVO);
                newReport.setReportType((ReportType) reportType.getSelectedItem());
                newReport.setDescription(description.getText().toString());

                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        appRepository.reportDao.insert(newReport);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                description.setText("");
                                reportType.setSelection(0);
                                newReport = new Report();
                                Toast.makeText(NewReportActivity.this, "Reclamo creado con éxito",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                };
                Thread t1 = new Thread(r1);
                t1.start();
            }
        });
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightGray));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.menuOptNewReport);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == RESULT_OK){
            Double latitude = data.getExtras().getDouble("latitude");
            Double longitude = data.getExtras().getDouble("longitude");
            newReport.setLatitud(latitude);
            newReport.setLongitud(longitude);
        }
    }
}

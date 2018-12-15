package com.dam.asfaltame.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.asfaltame.ChangeListenerVariable;
import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.ImageSourceDialog;
import com.dam.asfaltame.Maps.FetchAddressIntentService;
import com.dam.asfaltame.Model.Report;
import com.dam.asfaltame.Model.ReportType;
import com.dam.asfaltame.Model.Status;
import com.dam.asfaltame.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class NewReportActivity extends AppCompatActivity {

    Button otherLocation, actualLocation, createReport, addPicture;
    Spinner reportType;
    EditText description;
    TextView location;
    RecyclerView photoGallery;
    PhotoGalleryAdapter photoGalleryAdapter;

    FusedLocationProviderClient mFusedLocationClient;
    AddressResultReceiver mResultReceiver = new AddressResultReceiver(new Handler());
    ChangeListenerVariable createEnabled;

    Report newReport;
    AppRepository appRepository;
    String address;
    List<String> imagePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_report);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        newReport = new Report();
        appRepository = AppRepository.getInstance(this);
        createEnabled = new ChangeListenerVariable();

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
        location = findViewById(R.id.new_location);
        addPicture = findViewById(R.id.new_add_picture);
        photoGallery = findViewById(R.id.new_photo_gallery);
    }

    private void setListeners(){
        createEnabled.setListener(new ChangeListenerVariable.ChangeListener() {
            @Override
            public void onChange() {
                createReport.setEnabled(createEnabled.isVariable());
            }
        });
        createEnabled.setVariable(false);

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
                    ActivityCompat.requestPermissions(NewReportActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    return;
                } else {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(NewReportActivity.this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                startFetchAddressIntentService(location);
                                newReport.setLatitud(location.getLatitude());
                                newReport.setLongitud(location.getLongitude());
                            }
                        }
                    });
                }
            }
        });

        List<ReportType> types = new LinkedList<>(Arrays.asList(ReportType.values()));
        types.remove(ReportType.TODOS);
        ArrayAdapter<ReportType> reportTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, types);
        reportType.setAdapter(reportTypeAdapter);

        photoGalleryAdapter = new PhotoGalleryAdapter(NewReportActivity.this, imagePaths);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photoGallery.setLayoutManager(horizontalLayoutManagaer);
        photoGallery.setAdapter(photoGalleryAdapter);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(NewReportActivity.this, ImageSourceDialog.class);
                startActivityForResult(dialogIntent,2);
            }
        });

        createReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReport();
            }
        });
    }

    protected void startFetchAddressIntentService(Location location) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra("Receiver", mResultReceiver);
        intent.putExtra("Location", location);
        startService(intent);
    }

    private void createReport(){
        newReport.setStatus(Status.ACTIVO);
        newReport.setReportType((ReportType) reportType.getSelectedItem());
        newReport.setDescription(description.getText().toString());
        newReport.setAddress(address);
        newReport.setPictures(imagePaths.stream().collect(Collectors.joining(",")));

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                appRepository.reportDao.insert(newReport);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        description.setText("");
                        reportType.setSelection(0);
                        address = "";
                        location.setText("Ubicación:");
                        imagePaths.clear();
                        photoGalleryAdapter.notifyDataSetChanged();
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

            Location location = new Location("");
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            startFetchAddressIntentService(location);
        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            imagePaths.add(data.getStringExtra("result"));
            photoGalleryAdapter.notifyItemInserted(imagePaths.size() - 1);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultData == null) {
                return;
            }
            address = resultData.getString("Result").split(",")[0];
            createEnabled.setVariable(true);
            if (address == null) {
                address = "";
                createEnabled.setVariable(false);
                Toast.makeText(NewReportActivity.this, "Dirección no encontrada",
                        Toast.LENGTH_LONG).show();
            }
            location.setText("Ubicación: " + address);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation()
                            .addOnSuccessListener(NewReportActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        startFetchAddressIntentService(location);
                                        newReport.setLatitud(location.getLatitude());
                                        newReport.setLongitud(location.getLongitude());
                                    }
                                }
                            });
                }
                return;
            }
        }
    }
}

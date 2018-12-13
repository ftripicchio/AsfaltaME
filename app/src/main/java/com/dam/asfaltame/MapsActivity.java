package com.dam.asfaltame;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dam.asfaltame.Maps.MapClusterRenderer;
import com.dam.asfaltame.Maps.MapItem;
import com.dam.asfaltame.Modelo.Report;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int mapType;
    private Intent intent;
    private Bundle extras;
    private FusedLocationProviderClient mFusedLocationClient;
    private ClusterManager<MapItem> mClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        intent = getIntent();
        extras = intent.getExtras();
        mapType = extras.getInt("mapType");

        configureToolbar();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},9999);
            return;
        } else {
            manageMap();
        }

    }

    @SuppressLint("MissingPermission")
    private void manageMap(){
        mMap.setMyLocationEnabled(true);
        List<Report> reports;
        switch (mapType){
            case 1: //nuevo reclamo
                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MapsActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    mMap.moveCamera(CameraUpdateFactory.
                                            newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
                                }
                            }
                        });
                selectLocation();
                break;
            case 2: //detalle reclamo
                Report report = intent.getParcelableExtra("report");
                putMarker(report);
                break;
            case 3: //todos los reclamos
                reports = intent.getParcelableArrayListExtra("reports");
                allReports(reports);
                break;
            case 4: //reclamos cercanos
                reports = intent.getParcelableArrayListExtra("reports");
                putMarkers(reports);
                break;
        }
    }

    public void selectLocation(){
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                intent.putExtra("latitude", latLng.latitude);
                intent.putExtra("longitude", latLng.longitude);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void putMarker(Report report){
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(report.getLatitud(), report.getLongitud()))
                .icon(BitmapDescriptorFactory.fromResource(markerIcon(report))));
        mMap.moveCamera(CameraUpdateFactory.
                newLatLngZoom(marker.getPosition(), 16));
    }

    private void putMarkers(List<Report> reports){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Report r : reports){
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(r.getLatitud(), r.getLongitud()))
                    .icon(BitmapDescriptorFactory.fromResource(markerIcon(r))));
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,10);
        mMap.moveCamera(cu);
    }

    private void allReports(List<Report> reports){
        mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        final MapClusterRenderer renderer = new MapClusterRenderer(this, mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

        for (Report r : reports){
            mClusterManager.addItem(new MapItem(r.getLatitud(), r.getLongitud(), markerIcon(r)));
        }
        mClusterManager.cluster();

    }

    private int markerIcon(Report report){
        int markerIconId = 0;
        switch (report.getStatus()){
            case ACTIVO:
                switch (report.getReportType()){
                    case BACHE:
                        markerIconId = R.drawable.ic_bache_rojo;
                        break;
                    case MULTIPLE:
                        markerIconId = R.drawable.ic_multiple_rojo;
                        break;
                    case HUNDIMIENTO:
                        markerIconId = R.drawable.ic_hundimiento_rojo;
                        break;
                    case TAPA_HUNDIDA:
                        markerIconId = R.drawable.ic_tapa_hundida_rojo;
                }
                break;
            case EN_REPARACION:
                markerIconId = R.drawable.ic_reparacion_rojo;
                break;
        }
        return  markerIconId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    manageMap();
                }
                break;
            }
        }
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightGray));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionbar.setDisplayHomeAsUpEnabled(true);

        switch (mapType){
            case 1:
                actionbar.setTitle(R.string.mapSelectLocation);
                break;
            case 2:
                actionbar.setTitle(R.string.mapReportDetail);
                break;
            case 3:
                actionbar.setTitle(R.string.menuOptReportList);
                break;
            case 4:
                actionbar.setTitle(R.string.mapNearReports);
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

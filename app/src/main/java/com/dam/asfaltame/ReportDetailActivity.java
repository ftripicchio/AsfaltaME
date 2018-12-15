package com.dam.asfaltame;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.asfaltame.Modelo.Report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView icon;
    private TextView address, type, status, description;
    private Button addPicture, supportReport, alreadyFixed;
    private RecyclerView photoGallery;
    private PhotoGalleryAdapter photoGalleryAdapter;

    List<String> imagePaths = new ArrayList<>();
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        report = getIntent().getParcelableExtra("report");
        imagePaths = Arrays.asList(report.getPictures().split(","));

        configureToolbar();
        getViews();
        completeViews();
        setListeners();
    }

    private void getViews(){
        icon = findViewById(R.id.detail_icon);
        address = findViewById(R.id.detail_address);
        type = findViewById(R.id.detail_type);
        status = findViewById(R.id.detail_status);
        description = findViewById(R.id.detail_description);
        addPicture = findViewById(R.id.detail_add_picture);
        supportReport = findViewById(R.id.support_report_button);
        alreadyFixed = findViewById(R.id.already_fixed_button);
        photoGallery = findViewById(R.id.detail_photo_gallery);
    }

    private void completeViews(){
        icon.setImageResource(reportIcon(report));
        address.setText(report.getAddress());
        type.setText(report.getReportType().toString());
        status.setText(report.getStatus().toString());
        description.setText(report.getDescription());
    }

    public void setListeners(){
        photoGalleryAdapter = new PhotoGalleryAdapter(ReportDetailActivity.this, imagePaths);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photoGallery.setLayoutManager(horizontalLayoutManagaer);
        photoGallery.setAdapter(photoGalleryAdapter);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dialogIntent = new Intent(ReportDetailActivity.this, ImageSourceDialog.class);
                startActivityForResult(dialogIntent,1);
            }
        });
    }

    private int reportIcon(Report report){
        int markerIconId = 0;
        switch (report.getStatus()){
            case ACTIVO:
                switch (report.getReportType()){
                    case BACHE:
                        markerIconId = R.drawable.ic_bache;
                        break;
                    case MULTIPLE:
                        markerIconId = R.drawable.ic_multiple;
                        break;
                    case HUNDIMIENTO:
                        markerIconId = R.drawable.ic_hundimiento;
                        break;
                    case TAPA_HUNDIDA:
                        markerIconId = R.drawable.ic_tapa_hundida;
                }
                break;
            case EN_REPARACION:
                markerIconId = R.drawable.ic_reparacion;
                break;
        }
        return  markerIconId;
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightGray));
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.screenDetail);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 && resultCode == RESULT_OK){
            report.setPictures(report.getPictures() + "," + data.getStringExtra("result"));
            imagePaths = Arrays.asList(report.getPictures().split(","));
            photoGalleryAdapter = new PhotoGalleryAdapter(ReportDetailActivity.this, imagePaths);
            photoGallery.setAdapter(photoGalleryAdapter);
        }
    }
}

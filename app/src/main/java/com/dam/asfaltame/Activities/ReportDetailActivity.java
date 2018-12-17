package com.dam.asfaltame.Activities;

import android.content.Intent;
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

import com.dam.asfaltame.Database.AppRepository;
import com.dam.asfaltame.ImageSourceDialog;
import com.dam.asfaltame.Model.Report;
import com.dam.asfaltame.Model.ReportUser;
import com.dam.asfaltame.Model.Status;
import com.dam.asfaltame.Model.User;
import com.dam.asfaltame.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView icon;
    private TextView address, type, status, description;
    private Button seeMap, addPicture, supportReport, alreadyFixed;
    private RecyclerView photoGallery;
    private PhotoGalleryAdapter photoGalleryAdapter;
    private User activeUser;
    private AppRepository appRepository;
    private ReportUser reportUser;

    List<String> imagePaths = new ArrayList<>();
    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        report = getIntent().getParcelableExtra("report");
        imagePaths = Arrays.asList(report.getPictures().split(","));
        activeUser = MainActivity.getActiveUser();
        appRepository = AppRepository.getInstance(this);


        getReportUser(report,activeUser);
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
        seeMap = findViewById(R.id.detail_map_button);
    }

    private void completeViews(){
        icon.setImageResource(reportIcon(report));
        address.setText(report.getAddress());
        type.setText(report.getReportType().toString());
        status.setText(" - " + report.getStatus().toString());
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

        seeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportDetailActivity.this, MapsActivity.class);
                i.putExtra("mapType", 2);
                i.putExtra("report", report);
                startActivity(i);
            }
        });

        supportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportUser != null) {
                    reportUser.setAction(0);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.reportUserDao.update(reportUser);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                } else {
                    reportUser = new ReportUser(report, activeUser, 1);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.reportUserDao.insert(reportUser);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }

                supportReport.setEnabled(false);

            }
        });

        alreadyFixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportUser != null) {
                    reportUser.setAction(0);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.reportUserDao.update(reportUser);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                } else {
                    reportUser = new ReportUser(report, activeUser, -1);
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            appRepository.reportUserDao.insert(reportUser);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }

                alreadyFixed.setEnabled(false);

                report.setStatus(Status.EN_REPARACION);

                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        appRepository.reportDao.update(report);
                        Intent i = new Intent();
                        i.putExtra("reportId",report.getId());
                        i.setAction("com.dam.asfaltame.EN_REPARACION");
                        sendBroadcast(i);
                    }
                };
                Thread t = new Thread(r);
                t.start();



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

    private void getReportUser(final Report r, final User u){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<ReportUser> ruList = appRepository.reportUserDao.getReportUser(r.getId(),u.getId());

                if(ruList.size() != 0){
                    reportUser = ruList.get(0);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            switch (reportUser.getAction()){
                                case 1:
                                    supportReport.setEnabled(false);
                                    break;
                                case 0:
                                    supportReport.setEnabled(false);
                                    alreadyFixed.setEnabled(false);
                                    break;
                                case -1:
                                    alreadyFixed.setEnabled(false);
                                    break;
                            }
                        }
                    });

                } else {
                    reportUser = null;
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
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
            updateReport(report);
        }
    }

    private void updateReport(final Report report){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                appRepository.reportDao.update(report);
            }
        };
        Thread t = new Thread(runnable);
        t.start();
    }
}

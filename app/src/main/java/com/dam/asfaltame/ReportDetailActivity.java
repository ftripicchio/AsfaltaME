package com.dam.asfaltame;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.dam.asfaltame.Modelo.Report;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView icon;
    private TextView address, type, status, description;

    private Report report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        report = getIntent().getParcelableExtra("report");


        configureToolbar();
        getViews();
        //lo que sigue se haría luego de buscar el reporte
        completeViews();
    }

    private void getViews(){
        icon = findViewById(R.id.detail_icon);
        address = findViewById(R.id.detail_address);
        type = findViewById(R.id.detail_type);
        status = findViewById(R.id.detail_status);
        description = findViewById(R.id.detail_description);
    }

    private void completeViews(){
        icon.setImageResource(reportIcon(report));
        address.setText(report.getAddress());
        type.setText(report.getReportType().toString());
        status.setText(report.getStatus().toString());
        description.setText(report.getDescription());
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
}

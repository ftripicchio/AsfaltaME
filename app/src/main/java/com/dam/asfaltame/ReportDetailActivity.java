package com.dam.asfaltame;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView icon;
    private TextView address;
    private TextView type;
    private TextView status;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
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
        icon.setImageResource(R.drawable.ic_bache);
        address.setText("Pasaje 13 de Diciembre 2925");
        type.setText("Bache");
        status.setText("ACTIVO");
        description.setText("kjabsjfabslfa aslans akjfbalkjsnla alnflkansflka alfnalksnlka alnalknf alnflkansflkans alsnflkasnf");
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

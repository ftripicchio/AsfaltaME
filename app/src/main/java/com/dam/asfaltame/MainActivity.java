package com.dam.asfaltame;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNavigationDrawer();
        configureToolbar();
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
    }
    private void configureNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.navview);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Intent i;
                switch (menuItem.getItemId()){
                    case R.id.optReportList:
                        i = new Intent(MainActivity.this, ReportListActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.optNewReport:
                        i = new Intent(MainActivity.this, NewReportActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.optPreferences:
                        //test
                        i = new Intent(MainActivity.this, ReportDetailActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.optHelp:
                        return true;
                    case R.id.optAbout:
                        //test
                        i = new Intent(MainActivity.this, LoginRegisterActivity.class);
                        i.putExtra("LoginRegisterOption",1);
                        startActivity(i);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        drawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
}

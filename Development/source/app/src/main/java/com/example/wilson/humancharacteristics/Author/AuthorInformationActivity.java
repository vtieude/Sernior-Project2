package com.example.wilson.humancharacteristics.Author;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Setting.SettingActivity;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;

public class AuthorInformationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_information);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_start, R.string.navigation_start);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuNav = navigationView.getMenu();
        MenuItem navitem= menuNav.findItem(R.id.author_homepage);
        navitem.setEnabled(false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            super.onBackPressed();
            finish();
            return true; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent myIntent = null;
        switch (item.getItemId()) {
            case R.id.camera_start:
                super.onBackPressed();
                break;
            case R.id.storage_homepage:
                myIntent = new Intent(this, StorageActivity.class);
                this.startActivity(myIntent);
                finish();
                break;
            case R.id.setting_homepage:
                myIntent = new Intent(this, SettingActivity.class);
                this.startActivity(myIntent);
                finish();
                break;
            case R.id.exit_homepage:
                this.finishAffinity(); System.exit(0);
                break;
            case R.id.author_homepage:
                myIntent = new Intent(this, AuthorInformationActivity.class);
                this.startActivity(myIntent);
                finish();
                break;
            default: break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}

package com.example.wilson.humancharacteristics.Setting;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.Author.AuthorInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;
import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SettingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public SharedPreferences setting;
    public SharedPreferences.Editor prefEditor ;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.setting_name);
        setContentView(R.layout.activity_setting);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        Fragment fragment = new SettingScreen();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.setting_layout, fragment, "setting");
            fragmentTransaction.commit();
        }
        else {
            fragment = getFragmentManager().findFragmentByTag("setting");
        }
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
        MenuItem navitem= menuNav.findItem(R.id.setting_homepage);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.default_setting:
                defaultSetting();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void defaultSetting() {
        SettingScreen settingScreen = new SettingScreen();
        prefEditor = setting.edit();
        prefEditor.putBoolean("switch_age",true);
        prefEditor.putBoolean("switch_gender",true);
        prefEditor.putBoolean("switch_character",false);
        prefEditor.putBoolean("switch_emotion",false);
        prefEditor.putInt("amount_face", 5);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            prefEditor.apply();
        } else {
            prefEditor.commit();
        }
        settingScreen.defaultValue();
        Toast.makeText(this, R.string.toask_default_setting,
                Toast.LENGTH_LONG).show();
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

    public static class SettingScreen extends PreferenceFragment {
        static SwitchPreference switchGender, switchEmotion ,switchCharacteristic, switchAge;
        static SeekBarPreference seekBarAmount;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_screen);
            switchAge = (SwitchPreference) findPreference("switch_age");
            switchGender = (SwitchPreference) findPreference("switch_gender");
            switchEmotion = (SwitchPreference) findPreference("switch_emotion");
            switchCharacteristic = (SwitchPreference) findPreference("switch_character");
            seekBarAmount = (SeekBarPreference) findPreference("amount_face");
        }

        public void defaultValue() {
            switchAge.setChecked(false);
            switchAge.setChecked(true);
            switchCharacteristic.setChecked(false);
            switchEmotion.setChecked(false);
            switchGender.setChecked(true);
            seekBarAmount.setDefaultValue(5);
            seekBarAmount.setCurrentValue(5);
        }
    }
}

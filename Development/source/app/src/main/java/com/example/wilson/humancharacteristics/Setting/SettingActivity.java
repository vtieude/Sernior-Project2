package com.example.wilson.humancharacteristics.Setting;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class SettingActivity extends AppCompatActivity {

    public SharedPreferences setting;
    public SharedPreferences.Editor prefEditor ;
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
            switchAge.setChecked(true);
            switchCharacteristic.setChecked(false);
            switchEmotion.setChecked(false);
            switchGender.setChecked(true);
            seekBarAmount.setCurrentValue(5);
            seekBarAmount.setDefaultValue(5);
        }
    }
}

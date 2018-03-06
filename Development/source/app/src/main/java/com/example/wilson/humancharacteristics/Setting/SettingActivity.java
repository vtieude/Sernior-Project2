package com.example.wilson.humancharacteristics.Setting;

import android.app.FragmentTransaction;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;

import com.example.wilson.humancharacteristics.R;
import com.pavelsikun.seekbarpreference.SeekBarPreference;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
    public static class SettingScreen extends PreferenceFragment {
        List<Preference> listSwitch = null;
        Preference switchGender, switchEmotion ,switchCharacteristic, switchAge;
        SeekBarPreference seekBarAmount;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.setting_screen);
            listSwitch = new ArrayList<Preference>();
            switchAge = (Preference) findPreference("switch_age");
            switchGender = (Preference) findPreference("switch_gender");
            switchEmotion = (Preference) findPreference("switch_emotion");
            switchCharacteristic = (Preference) findPreference("switch_character");
            seekBarAmount = (SeekBarPreference) findPreference("amount_face");
            listSwitch.add(switchAge);
            listSwitch.add(switchGender);
            listSwitch.add(switchEmotion);
            listSwitch.add(switchCharacteristic);
            setOnChangeSwitch();
        }
        public int getAmount() {
            return seekBarAmount.getCurrentValue();
        }
        public void setOnChangeSwitch() {
            for (int i =0; i < listSwitch.size(); i++) {
                final int finalI = i;
                listSwitch.get(i).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        boolean isOn = (boolean) o;
                        if (isOn) {
                            listSwitch.get(finalI).setSummary("On");
                        }
                        else listSwitch.get(finalI).setSummary("Off");
                        return true;
                    }
                });
            }
        }
    }
}

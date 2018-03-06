package com.example.wilson.humancharacteristics;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wilson.humancharacteristics.CameraDetect.CameraDetectActivity;
import com.example.wilson.humancharacteristics.Setting.SettingActivity;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    private List<Button> listButton;
    Button btStart, btStorage, btSetting, btCopyright, btExit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listButton = new ArrayList<Button>();
        // Example of a call to a native method
        btStart = (Button)this.findViewById(R.id.buttonStart);
        btStorage = (Button)this.findViewById(R.id.buttonStorage);
        btSetting = (Button)this.findViewById(R.id.buttonSetting);
        btCopyright = (Button)this.findViewById(R.id.buttonCopyright);
        btExit = (Button) this.findViewById(R.id.buttonExit);
        listButton.add(btStart);
        listButton.add(btStorage);
        listButton.add(btSetting);
        listButton.add(btCopyright);
        listButton.add(btExit);
        onClickButton();
    }
    public void onClickButton() {
        for (int i = 0 ; i < (int) listButton.size() ; i++) {
            final int finalI = i;
            listButton.get(i).setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = null;
                    switch (finalI) {
                        case 0:  myIntent = new Intent(MainActivity.this, CameraDetectActivity.class);
                            break;
                        case 1: myIntent = new Intent(MainActivity.this, StorageActivity.class);
                            break;
                        case 2:  myIntent = new Intent(MainActivity.this, SettingActivity.class);
                            break;
                        case 4: finish(); System.exit(0);
                        default: break;
                    }
                    MainActivity.this.startActivity(myIntent);
                }
            });
        }
    }
    public native String stringFromJNI();
}

package com.example.wilson.humancharacteristics.HumanInformation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanInformationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);
        Intent intent = getIntent();
        HumanModel humanModel = (HumanModel)intent.getSerializableExtra("human");
        TextView textName = (TextView)findViewById(R.id.text_name);
        TextView textAge = (TextView)findViewById(R.id.text_Age);
        ImageView imageView = (ImageView)findViewById(R.id.humanFaceAvatar);
        int imageId = humanModel.getMipmapResIdByName(this,humanModel.getFlagImage());
        imageView.setImageResource(imageId);
        textName.setText(humanModel.getName().toString());
        textAge.setText("Age: "+String.valueOf(humanModel.getAge()));
    }
}

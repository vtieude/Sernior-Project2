package com.example.wilson.humancharacteristics.HumanInformation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanInformationActivity extends AppCompatActivity {
    private int REQUEST_CODE = 100;
    private ImageView imageView;
    private TextView textName,textAge, textComment, textEmail, textPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);
        checkItent();
        getView();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");
        final EditText input = new EditText(this);
        
    }

    public void getView() {
        textName = (TextView)findViewById(R.id.text_name);
        textAge = (TextView)findViewById(R.id.text_Age);
        imageView = (ImageView)findViewById(R.id.humanFaceAvatar);
        textComment = (TextView) findViewById(R.id.textViewComment);
        textEmail = (TextView) findViewById(R.id.textViewEmail);
        textPhone = (TextView) findViewById(R.id.textViewPhone);
    }
    public void checkItent(){
        Intent intent = getIntent();
        if (intent.hasExtra("human") ) {
            HumanModel humanModel = (HumanModel)intent.getSerializableExtra("human");
            int imageId = humanModel.getMipmapResIdByName(this,humanModel.getFlagImage());
            imageView.setImageResource(imageId);
            textName.setText(humanModel.getName().toString());
            textAge.setText("Age: "+String.valueOf(humanModel.getAge()));
        }
        else {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent1.putExtra("return-data", true);
            startActivityForResult(intent1, REQUEST_CODE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            Toast.makeText(this, R.string.toask_default_setting,
                    Toast.LENGTH_LONG).show();
        }
    }
}

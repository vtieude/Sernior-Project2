package com.example.wilson.humancharacteristics.HumanInformation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanInformationActivity extends AppCompatActivity {
    private int REQUEST_CODE = 100;
    private int MAX_LENGTH = 15;
    private ImageView imageView, imagePencil;
    private TextView textName,textAge, textComment, textEmail, textPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);
        checkItent();
        getView();
        viewEvent();
    }
    public void imagePencilEvent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update");
        Context context = getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editName = new EditText(context);
        editName.setText(textName.getText().toString());
        editName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        layout.addView(editName);

        final EditText editAge = new EditText(context);
        editAge.setText(textAge.getText().toString());
        editAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        layout.addView(editAge);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                textName.setText(editName.getText().toString());
                textAge.setText(editAge.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void editInformationEvent(String tittle, final TextView text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(tittle);
        final EditText input = new EditText(this);
        input.setText(text.getText().toString());
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text.setText(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
    public void getView() {
        textName = (TextView)findViewById(R.id.text_name);
        textAge = (TextView)findViewById(R.id.text_Age);
        imageView = (ImageView)findViewById(R.id.humanFaceAvatar);
        textComment = (TextView) findViewById(R.id.textViewComment);
        textEmail = (TextView) findViewById(R.id.textViewEmail);
        textPhone = (TextView) findViewById(R.id.textViewPhone);
        imagePencil = (ImageView)findViewById(R.id.imageViewPencil);

    }
    public void viewEvent() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accessCamera();
            }
        });
        imagePencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePencilEvent();
            }
        });
        textComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInformationEvent("Comment", textComment);
            }
        });
        textPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInformationEvent("Comment", textPhone);
            }
        });
        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInformationEvent("Comment", textEmail);
            }
        });
    }
    public void accessCamera() {
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra("return-data", true);
        startActivityForResult(intent1, REQUEST_CODE);
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
            accessCamera();
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

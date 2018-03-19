package com.example.wilson.humancharacteristics.HumanInformation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.io.ByteArrayOutputStream;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanInformationActivity extends AppCompatActivity {
    private int REQUEST_CODE = 100;
    private int MAX_LENGTH = 20;
    private Boolean checkItent = false;
    private ImageView imageView, imagePencil;
    private TextView textName,textAge, textComment, textEmail, textPhone;
    private Button buttonSave;
    private HumanModel humanInfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);
        getView();
        checkItent();
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
        editAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
        editAge.setInputType(InputType.TYPE_CLASS_NUMBER);
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
        buttonSave = (Button)findViewById(R.id.btSave);

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
                editInformationEvent("Phone", textPhone);
            }
        });
        textEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInformationEvent("Email", textEmail);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HumanDatabaseHelper database = new HumanDatabaseHelper(getApplicationContext());
                if (!checkItent) {

                    database.addHuman(getHumanInfor());
                    Toast.makeText(getApplicationContext(), R.string.alert_succsess, Toast.LENGTH_LONG).show();
                }
                else {
                    database.updateHuman(getHumanInfor());
                }
                Intent intent = new Intent(HumanInformationActivity.this, StorageActivity.class);
                startActivity(intent);
            }
        });
    }
    public HumanModel getHumanInfor() {
        HumanModel humanModel = new HumanModel();
        if ( checkItent) {
            humanModel.setId(humanInfor.getId());
        }
        humanModel.setName(textName.getText().toString());
        humanModel.setPhone(textPhone.getText().toString());
        humanModel.setEmail(textEmail.getText().toString());
        humanModel.setComment(textComment.getText().toString());
        humanModel.setAge(Integer.parseInt(textAge.getText().toString()));
        humanModel.setImage(ConverttoArrayByte(imageView));
        return humanModel;
    }
    public void accessCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE);
        }
        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent1.putExtra("return-data", true);
        startActivityForResult(intent1, REQUEST_CODE);

    }
    public void checkItent(){
        Intent intent = getIntent();
        if (intent.hasExtra("human") ) {
            checkItent = true;
            humanInfor = (HumanModel)intent.getSerializableExtra("human");
            if ( humanInfor.getImage() != null) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(humanInfor.getImage(), 0, humanInfor.getImage().length);
                imageView.setImageBitmap(bitmap);
            }
            textName.setText(humanInfor.getName());
            textAge.setText(String.valueOf(humanInfor.getAge()));
            textEmail.setText(humanInfor.getEmail());
            textPhone.setText(humanInfor.getPhone());
            textComment.setText(humanInfor.getComment());
            buttonSave.setText(R.string.update_Button);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
        return;
    }

    public byte[] ConverttoArrayByte(ImageView img)
    {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) img.getDrawable();
        Bitmap bitmap=bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}

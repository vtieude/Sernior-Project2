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
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.Tensorflow.Classifier;
import com.example.wilson.Tensorflow.TensorFlowImageClassifier;
import com.example.wilson.humancharacteristics.Author.AuthorInformationActivity;
import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Setting.SettingActivity;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.HumanCharacteristicAttractiveness;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanInformationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private int REQUEST_CODE = 100;
    private int MAX_LENGTH = 20;
    private Boolean checkItent = false;
    private ImageView imageAvartaView, imagePencil;
    private TextView textName, textComment, textPhone, textEmail, textAtractiveness, textCompetent, textDominant;
    private TextView textExtroverted, textLikeability, textThread, textTrustworthy;
    private Button buttonSave, buttonBack;
    private HumanModel humanInfor;
    private String titlePhone, titleEmail, titleComment;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_information);
        getView();
        checkItent();
        if (savedInstanceState != null ) {
            updateTextChangeOrientation(savedInstanceState);
        }
        viewEvent();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_start, R.string.navigation_start);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
////        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Information");
//        initTensorFlowAndLoadModel();
    }

//    private void initTensorFlowAndLoadModel() {
//        executor.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                     hahai = new HumanCharacteristicAttractiveness(getAssets());
//                } catch (final Exception e) {
//                    throw new RuntimeException("Error initializing TensorFlow!", e);
//                }
//            }
//        });
//    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("TextName", textName.getText().toString());
        outState.putString("TextComment", textComment.getText().toString());
        outState.putString("TextPhone", textPhone.getText().toString());
        outState.putString("TextEmail", textEmail.getText().toString());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(HumanInformationActivity.this, StorageActivity.class);
            startActivity(intent);
            finish();
            return true; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }
    public void updateTextChangeOrientation(Bundle save) {
        textName.setText(save.getString("TextName").toString());
        textComment.setText(save.getString("TextComment").toString());
        textEmail.setText(save.getString("TextEmail").toString());
        textPhone.setText(save.getString("TextPhone").toString());
    }
//    public void imagePencilEvent() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Update");
//        Context context = getApplicationContext();
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final EditText editName = new EditText(context);
//        editName.setText(textName.getText().toString());
//        editName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
//        layout.addView(editName);
//
//        final EditText editAge = new EditText(context);
//        editAge.setText(textAge.getText().toString());
//        editAge.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
//        editAge.setInputType(InputType.TYPE_CLASS_NUMBER);
//        layout.addView(editAge);
//        builder.setView(layout);
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                textName.setText(editName.getText().toString());
//                textAge.setText(editAge.getText().toString());
//            }
//        });
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.show();
//    }
    public void editInformationEvent(String tittle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(tittle);
        Context context = getApplicationContext();
        LayoutInflater inflater = getLayoutInflater();
        View layout =(View) inflater.inflate(R.layout.alert_update_information,null) ;
        final EditText editName = layout.findViewById(R.id.input_name_alert);
        final EditText editCommend = layout.findViewById(R.id.input_commend_alert);
        final EditText editPhone = layout.findViewById(R.id.input_phone_alert);
        final EditText editEmail = layout.findViewById(R.id.input_email_alert);
        editName.setText(humanInfor.getName());
        editCommend.setText(humanInfor.getComment());
        editPhone.setText(humanInfor.getPhone());
        editEmail.setText(humanInfor.getEmail());
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//
//        LinearLayout layoutName = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout layoutAge = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout layoutCommend = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout layoutPhone = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        LinearLayout layoutEmail = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.HORIZONTAL);
//
//        TextView textName = new TextView(this);
//        textName.setText("Name: ");
//        final EditText inputName = new EditText(this);
//        inputName.setText(humanInfor.getName());
//        layoutName.addView(textName);
//        layoutName.addView(inputName);
//        layout.addView(layoutName);
//
//        TextView textAge = new TextView(this);
//        textAge.setText("Age: ");
//        final EditText inputAge = new EditText(this);
//        inputAge.setText(String.valueOf(humanInfor.getAge()));
//        TextView textCommend = new TextView(this);
//        textCommend.setText(getString(R.string.tittle_comment) + ": ");
//        final EditText inputCommend = new EditText(this);
//        inputCommend.setText(humanInfor.getComment());
//        TextView textPhone = new TextView(this);
//        textPhone.setText(getString(R.string.tittle_phone) + ": ");
//        final EditText inputPhone = new EditText(this);
//        inputPhone.setText(humanInfor.getPhone());
//        TextView textEmail = new TextView(this);
//        textEmail.setText(getString(R.string.tittle_email) + ": ");
//        final EditText inputEmail = new EditText(this);
//        inputEmail.setText(humanInfor.getEmail());
//
//        layoutAge.addView(textAge);
//        layoutAge.addView(inputAge);
//        layout.addView(layoutAge);
//        layoutCommend.addView(textCommend);
//        layoutCommend.addView(inputCommend);
//        layout.addView(layoutCommend);
//        layoutPhone.addView(textPhone);
//        layoutPhone.addView(inputPhone);
//        layout.addView(layoutPhone);
//        layoutEmail.addView(textEmail);
//        layoutEmail.addView(inputEmail);
//        layout.addView(layoutEmail);

        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HumanDatabaseHelper database = new HumanDatabaseHelper(getApplicationContext());
                HumanModel humanModel = new HumanModel();
                if ( checkItent) {
                    humanModel.setId(humanInfor.getId());
                }
                humanModel.setName(editName.getText().toString());
                humanModel.setPhone(editPhone.getText().toString());
                humanModel.setEmail(editEmail.getText().toString());
                humanModel.setComment(editCommend.getText().toString());
                database.updateHuman(humanModel);
                textName.setText(editName.getText().toString());
                textComment.setText(editCommend.getText().toString());
                textPhone.setText(titlePhone+ ": "+ editPhone.getText().toString());
                textEmail.setText(titleEmail+ ": " +editEmail.getText().toString());
                database.close();
//                Intent intent = new Intent(HumanInformationActivity.this, StorageActivity.class);
//                startActivity(intent);
//                finish();
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
        imageAvartaView = (ImageView)findViewById(R.id.humanFaceAvatar);
        textComment = (TextView) findViewById(R.id.textViewComment);
        textEmail = (TextView) findViewById(R.id.tittleEmail);
        textPhone = (TextView) findViewById(R.id.titlePhone);
        buttonSave = (Button)findViewById(R.id.btSave);
        buttonBack = (Button)findViewById(R.id.back_screen);
        textAtractiveness = findViewById(R.id.tittleAttactiveness);
        textCompetent = findViewById(R.id.tittleCompetent);
        textDominant = findViewById(R.id.tittleDominant);
        textExtroverted = findViewById(R.id.tittleExtroverted);
        textLikeability = findViewById(R.id.tittleLikeability);
        textThread = findViewById(R.id.tittleThread);
        textTrustworthy = findViewById(R.id.tittleTrustworthy);
        titleComment = textComment.getText().toString();
        titleEmail = textEmail.getText().toString();
        titlePhone = textPhone.getText().toString();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void viewEvent() {
//        imagePencil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imagePencilEvent();
//            }
//        });
//        textComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editInformationEvent("Comment", textComment);
//            }
//        });
//        textPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editInformationEvent("Phone", textPhone);
//            }
//        });
//        textEmail.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editInformationEvent("Email", textEmail);
//            }
//        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editInformationEvent(getString(R.string.updated_inforhuman).toString());

            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HumanInformationActivity.this, StorageActivity.class);
                startActivity(intent);
                finish();
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
        humanModel.setImage(ConverttoArrayByte(imageAvartaView));
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
                imageAvartaView.setImageBitmap(bitmap);
            }
            textName.setText(humanInfor.getName());
            textEmail.setText(titleEmail+": "+humanInfor.getEmail());
            textPhone.setText(titlePhone+": "+humanInfor.getPhone());
            if (humanInfor.getComment() == "") {
                textComment.setText(getString(R.string.tittle_comment));
            }
            else {
                textComment.setText(humanInfor.getComment());
            }
            textAtractiveness.setText(textAtractiveness.getText() + humanInfor.getAttracttive());
            textCompetent.setText(textCompetent.getText() + humanInfor.getCompetent());
            textDominant.setText(textDominant.getText() + humanInfor.getDominant());
            textExtroverted.setText(textExtroverted.getText()+ humanInfor.getExtroverted());
            textLikeability.setText(textLikeability.getText()+ humanInfor.getLikeability());
            textThread.setText(textThread.getText()+ humanInfor.getThreadCharacteristic());
            textTrustworthy.setText(textTrustworthy.getText()+ humanInfor.getTrustworthy());
            buttonSave.setText(R.string.update_Button);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

//            bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);
//            textEmail.setText(hahai.recognizeImage(bitmap));
            imageAvartaView.setImageBitmap(bitmap);
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
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
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

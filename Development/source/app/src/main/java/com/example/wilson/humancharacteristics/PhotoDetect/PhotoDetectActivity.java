package com.example.wilson.humancharacteristics.PhotoDetect;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;
import com.example.wilson.humancharacteristics.utils.ImageUtils;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PhotoDetectActivity extends AppCompatActivity {

    private static final String TAG = PhotoDetectActivity.class.getSimpleName();
    private HumanModel humanModel;

    private static final int RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM = 3;
    private static int PICK_IMAGE_REQUEST = 5;
    private FaceView faceView;
    private ArrayList<Bitmap> facesBitmap;
    private FaceResult faces[];

    private static final int MAX_FACE = 1;
    private Executor executor = Executors.newSingleThreadExecutor();
    private static final int INPUT_SIZE = 224;

    private boolean checkCreateModel = false;
    private boolean checkCropImage = false;

    private TextView textView;
    private Button btnRecognize;
    private Button btnSave;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Face Detect Image");
        progressBar = findViewById(R.id.progressBarDetectFaceGalary);
        faceView = (FaceView) findViewById(R.id.faceView);
        textView = (TextView) findViewById(R.id.text_recognize);
        btnRecognize = (Button) findViewById(R.id.btn_recognize);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnSave.setEnabled(false);
        checkCropImage = false;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkCreateModel && checkCropImage && faces[0] != null){
                    HumanDatabaseHelper database = new HumanDatabaseHelper(getApplicationContext());
                    HumanModel humanModel = new HumanModel();
                    humanModel.setAttracttive(String.valueOf(Integer.parseInt(faces[0].getAttractive().substring(1,2))+ 1));
                    humanModel.setDominant(String.valueOf(Integer.parseInt(faces[0].getDominant().substring(1,2))+ 1));
                    humanModel.setCompetent(String.valueOf(Integer.parseInt(faces[0].getCompetent().substring(1,2))+ 1));
                    humanModel.setExtroverted(String.valueOf(Integer.parseInt(faces[0].getExtroverted().substring(1,2))+ 1));
                    humanModel.setLikeability(String.valueOf(Integer.parseInt(faces[0].getLikeability().substring(1,2))+ 1));
                    humanModel.setThreadCharacteristic(String.valueOf(Integer.parseInt(faces[0].getThread().substring(1,2))+ 1));
                    humanModel.setTrustworthy(String.valueOf(Integer.parseInt(faces[0].getTrustworthy().substring(1,2))+ 1));
                    Bitmap bmp32 = Bitmap.createScaledBitmap(faces[0].getBitmapFaceCrop(), 100, 100, false);
                    humanModel.setImage(ConverttoArrayByte(bmp32));
                    humanModel.setName("");
                    humanModel.setAge(18);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    humanModel.setDateCreatAt(df.format(c));
                    humanModel.setComment("Type");
                    humanModel.setEmail("Type");
                    humanModel.setPhone("Type");
                    database.addHuman(humanModel);
                    database.close();
                    btnSave.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Saved",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCreateModel && checkCropImage && faces[0] != null){
                    Bitmap bmp32 = faces[0].getBitmapFaceCrop();
                    faces[0].setAttractive(humanModel.getAttracttiveHuman().recognizeImage(bmp32));
                    faces[0].setTrustworthy(humanModel.getTrustworthyHuman().recognizeImage(bmp32));
                    faces[0].setDominant(humanModel.getDominantHuman().recognizeImage(bmp32));
                    faces[0].setThread(humanModel.getThreadHuman().recognizeImage(bmp32));
                    faces[0].setLikeability(humanModel.getLikeabilityHuman().recognizeImage(bmp32));
                    faces[0].setCompetnent(humanModel.getCompetentHuman().recognizeImage(bmp32));
                    faces[0].setExtroverted(humanModel.getExtrovertedHuman().recognizeImage(bmp32));
                    setStringRecognize(faces[0].getAttractive(),faces[0].getTrustworthy(),faces[0].getDominant(),
                            faces[0].getThread(),faces[0].getLikeability(),faces[0].getCompetent(),faces[0].getExtroverted());
                    btnSave.setEnabled(true);
                }
                else {
                    textView.setText("Cannot detect face");
                }
            }
        });


        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            getImage();
        } else {
            requestWriteExternalPermission();
        }

        faces = new FaceResult[MAX_FACE];

        for (int i = 0; i < MAX_FACE; i++) {
            faces[i] = new FaceResult();
        }

        if(checkCreateModel == false){
            initTensorFlowAndLoadModel();
        }
    }
    public byte[] ConverttoArrayByte(Bitmap bitmapConvert)
    {
        if (bitmapConvert == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bitmapConvert.getWidth() * bitmapConvert.getHeight());
        bitmapConvert.compress(Bitmap.CompressFormat.PNG, 100, bos);
        return bos.toByteArray();
    }
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    humanModel = new HumanModel(getAssets());
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
                checkCreateModel = true;
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_photo, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;

            case R.id.gallery:

                int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    getImage();
                } else {
                    requestWriteExternalPermission();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetData();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(checkCreateModel == true){
                    humanModel.getAttracttiveHuman().onDestroy();
                    humanModel.getExtrovertedHuman().onDestroy();
                    humanModel.getCompetentHuman().onDestroy();
                    humanModel.getDominantHuman().onDestroy();
                    humanModel.getLikeabilityHuman().onDestroy();
                    humanModel.getThreadHuman().onDestroy();
                    humanModel.getTrustworthyHuman().onDestroy();
                    checkCreateModel = false;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            Bitmap bitmap = ImageUtils.getBitmap(ImageUtils.getRealPathFromURI(this, uri), 2048, 1232);
            if (bitmap != null)
                detectFace(bitmap);
            else
                Toast.makeText(this, "Cann't open this image.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Write External permission granted");
            // we have permission
            getImage();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));
    }

    public void getImage() {
        // Create intent to Open Image applications like Gallery, Google Photos
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
        } catch (ActivityNotFoundException i) {
            Toast.makeText(PhotoDetectActivity.this, "Your Device can not select image from gallery.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setStringRecognize(String atracttive, String trustworthy,String dominant, String thread,
                                    String likeability, String competent, String extroverted) {
        textView.setText("Attractive: "+ String.valueOf(Integer.parseInt(atracttive.substring(1,2))+ 1)+ ";   "+"Trusworthy: "+String.valueOf(Integer.parseInt(trustworthy.substring(1,2))+ 1)
                +"; " +"Dominant: "+String.valueOf(Integer.parseInt(dominant.substring(1,2))+ 1)+";\n"+"Thread: "
                +String.valueOf(Integer.parseInt(thread.substring(1,2))+ 1)+"; "
                +"Likeability: "+String.valueOf(Integer.parseInt(likeability.substring(1,2))+ 1)+";  "+
                "Competent: "+String.valueOf(Integer.parseInt(competent.substring(1,2))+ 1)+"; "+"Extroverted"+
                String.valueOf(Integer.parseInt(extroverted.substring(1,2))+ 1));
    }
    private void detectFace(Bitmap bitmap) {
        resetData();

        android.media.FaceDetector fdet_ = new android.media.FaceDetector(bitmap.getWidth(), bitmap.getHeight(), MAX_FACE);

        android.media.FaceDetector.Face[] fullResults = new android.media.FaceDetector.Face[MAX_FACE];
        fdet_.findFaces(bitmap, fullResults);

        ArrayList<FaceResult> faces_ = new ArrayList<>();


        for (int i = 0; i < MAX_FACE; i++) {
            if (fullResults[i] != null) {
                PointF mid = new PointF();
                fullResults[i].getMidPoint(mid);

                float eyesDis = fullResults[i].eyesDistance();
                float confidence = fullResults[i].confidence();
                float pose = fullResults[i].pose(android.media.FaceDetector.Face.EULER_Y);

                Rect rect = new Rect(
                        (int) (mid.x - eyesDis * 1.20f),
                        (int) (mid.y - eyesDis * 0.55f),
                        (int) (mid.x + eyesDis * 1.20f),
                        (int) (mid.y + eyesDis * 1.85f));

                /**
                 * Only detect face size > 100x100
                 */
                if (rect.height() * rect.width() > 100 * 100) {
                    FaceResult faceResult = new FaceResult();
                    faceResult.setFace(0, mid, eyesDis, confidence, pose, System.currentTimeMillis());
                    faces_.add(faceResult);

                    //
                    // Crop Face to display in RecylerView
                    //
                    Bitmap cropedFace = ImageUtils.cropFace(faceResult, bitmap, 0);
                    if (cropedFace != null) {
                        Bitmap bmp32 = Bitmap.createScaledBitmap(cropedFace, INPUT_SIZE, INPUT_SIZE, false);
                        faces[i].setBitmapFaceCrop(bmp32);
                        checkCropImage = true;
                    }
                }
            }
        }

        FaceView overlay = (FaceView) findViewById(R.id.faceView);
        overlay.setContent(bitmap, faces_);
    }


    private void requestWriteExternalPermission() {
        Log.w(TAG, "Write External permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_WRITE_EXTERNAL_STORAGE_PERM);
    }

    private void resetData() {
//
//        if (imagePreviewAdapter == null) {
//            facesBitmap = new ArrayList<>();
//            imagePreviewAdapter = new ImagePreviewAdapter(PhotoDetectActivity.this, facesBitmap, new ImagePreviewAdapter.ViewHolder.OnItemClickListener() {
//                @Override
//                public void onClick(View v, int position) {
//                    imagePreviewAdapter.setCheck(position);
//                    imagePreviewAdapter.notifyDataSetChanged();
//                }
//            });
//            recyclerView.setAdapter(imagePreviewAdapter);
//        } else {
//            imagePreviewAdapter.clearAll();
//        }
        textView.setText("");
        faceView.reset();
    }
}
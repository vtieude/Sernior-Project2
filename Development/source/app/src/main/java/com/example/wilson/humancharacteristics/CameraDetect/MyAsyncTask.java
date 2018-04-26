package com.example.wilson.humancharacteristics.CameraDetect;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.text.BoringLayout;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;

import org.opencv.face.Face;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Wilson on 4/24/2018.
 */

public class MyAsyncTask extends AsyncTask<FaceResult, FaceResult, FaceResult[]> {
    public Activity activityScren;
    public ProgressBar progress;
    ImageButton imageButton;
    private int MAX_LENGTH = 20;
    Boolean check = false;
    private int max_face = 0;
    public String nameHuman = "";
    public MyAsyncTask(Activity activity) {
        activityScren = activity;
        imageButton = activityScren.findViewById(R.id.takePhoto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = (ProgressBar)activityScren.findViewById(R.id.progressBarCamera);
        Toast.makeText(activityScren, "Wait!" ,
                Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        SystemClock.sleep(1000);

    }
    public void getNameHuman() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityScren);
        builder.setTitle("Type name");
        Context context = activityScren.getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText editName = new EditText(context);
        editName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        layout.addView(editName);
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                nameHuman = editName.getText().toString();
//                textName.setText(editName.getText().toString());
//                textAge.setText(editAge.getText().toString());
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
    @Override
    protected FaceResult[] doInBackground(FaceResult... faceResults) {
            for (int i = 0; i < faceResults.length; i++) {
                if (faceResults[i].getBitmapFaceCrop()!= null) {
                    check = true;
                    max_face++;
                    HumanDatabaseHelper database = new HumanDatabaseHelper(activityScren);
                    HumanModel humanModel = new HumanModel();
                    humanModel.setAttracttive(faceResults[i].getAttractive());
                    humanModel.setDominant(faceResults[i].getDominant());
                    humanModel.setCompetent(faceResults[i].getCompetent());
                    humanModel.setExtroverted(faceResults[i].getExtroverted());
                    humanModel.setLikeability(faceResults[i].getLikeability());
                    humanModel.setThreadCharacteristic(faceResults[i].getThread());
                    humanModel.setTrustworthy(faceResults[i].getTrustworthy());
                    humanModel.setImage(ConverttoArrayByte(faceResults[i].getBitmapFaceCrop()));
                    humanModel.setName(nameHuman);
                    humanModel.setAge(18);
                    humanModel.setEmail("");
                    humanModel.setPhone("");
                    publishProgress(faceResults[0]);
                    database.addHuman(humanModel);
                    SystemClock.sleep(1500);
                }
//            publishProgress(check);

            }

        return faceResults;
    }

    @Override
    protected void onProgressUpdate(FaceResult... values) {
        super.onProgressUpdate(values);
        if (check) {
            Toast.makeText(activityScren, "Save " + max_face,
                    Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onPostExecute(FaceResult[] faceResults) {
        super.onPostExecute(faceResults);
        SystemClock.sleep(500);

        if (check) {
            Toast.makeText(activityScren, "Save Success with " + max_face  +" face",
                    Toast.LENGTH_SHORT).show();
            check = false;
        }
        else {
            Toast.makeText(activityScren, "Cannot save!",
                    Toast.LENGTH_SHORT).show();
        }
        progress.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        max_face = 0;
    }
}

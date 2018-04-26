package com.example.wilson.humancharacteristics.CameraDetect;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.BoringLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    Boolean check;
    public MyAsyncTask(Activity activity) {
        activityScren = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = (ProgressBar)activityScren.findViewById(R.id.progressBarCamera);
        imageButton = activityScren.findViewById(R.id.takePhoto);
        imageButton.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected FaceResult[] doInBackground(FaceResult... faceResults) {
        for (int i = 0; i < faceResults.length; i++) {
            SystemClock.sleep(100);
            check = false;
            if (faceResults[i].getDominant().equals("")) {
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
                humanModel.setName("");
                humanModel.setAge(18);
                humanModel.setEmail("");
                humanModel.setPhone("");
                database.addHuman(humanModel);
                publishProgress(faceResults[0]);
                check = true;
            }
//            publishProgress(check);
        }
        return faceResults;
    }

    @Override
    protected void onProgressUpdate(FaceResult... values) {
        super.onProgressUpdate(values);
            Toast.makeText(activityScren, "Saving:" + values[0].getLikeability() ,
                    Toast.LENGTH_SHORT).show();

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
        if (check)
        Toast.makeText(activityScren, "Save Success!" + faceResults.length,
                Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.INVISIBLE);
        imageButton.setEnabled(true);
    }
}

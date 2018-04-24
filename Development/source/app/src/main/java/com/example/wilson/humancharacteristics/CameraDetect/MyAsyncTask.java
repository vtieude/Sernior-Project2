package com.example.wilson.humancharacteristics.CameraDetect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.model.FaceResult;

/**
 * Created by Wilson on 4/24/2018.
 */

public class MyAsyncTask extends AsyncTask<FaceResult, FaceResult, FaceResult[]> {
    public Activity activityScren;
    public ProgressBar progress;
    public MyAsyncTask(Activity activity) {
        activityScren = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = (ProgressBar)activityScren.findViewById(R.id.progressBarCamera);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected FaceResult[] doInBackground(FaceResult... faceResults) {
        for (int i = 0; i < faceResults.length; i++) {
            SystemClock.sleep(100);
            if (faceResults[i].getId() != 0) {

            }
        }

        return faceResults;
    }

    @Override
    protected void onProgressUpdate(FaceResult... values) {
        super.onProgressUpdate(values);
        if (values[0].getId() != 0) {
            Toast.makeText(activityScren, "Savingrer!" + values[0].getAttractive(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPostExecute(FaceResult[] faceResults) {
        super.onPostExecute(faceResults);
        Toast.makeText(activityScren, "Savingrer!" + faceResults.length,
                Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.INVISIBLE);
    }
}

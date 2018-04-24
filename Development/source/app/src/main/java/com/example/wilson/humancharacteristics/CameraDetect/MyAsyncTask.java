package com.example.wilson.humancharacteristics.CameraDetect;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;

/**
 * Created by Wilson on 4/24/2018.
 */

public class MyAsyncTask extends AsyncTask<FaceResult[], Void, Void > {
    public Activity activityScren;
    public ProgressBar progress;
    public MyAsyncTask(Activity activity) {
        activityScren = activity;
    }

    @Override
    protected Void doInBackground(FaceResult[]... faceResults) {
        for (int i = 0; i < faceResults.length; i ++) {
            Toast.makeText(activityScren, i,
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        progress = (ProgressBar)activityScren.findViewById(R.id.progressBarCamera);
        progress.setVisibility(View.VISIBLE);
        Toast.makeText(activityScren, "Saving!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.setVisibility(View.INVISIBLE);
    }
}

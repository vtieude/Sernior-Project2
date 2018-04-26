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
        imageButton.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    protected FaceResult[] doInBackground(FaceResult... faceResults) {
        for (int i = 0; i < faceResults.length; i++) {
            SystemClock.sleep(100);
            check = false;
            if (faceResults[i].getId() != 0) {
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
                SystemClock.sleep(1000);
            }
//            publishProgress(check);
        }

        return faceResults;
    }

    @Override
    protected void onProgressUpdate(FaceResult... values) {
        super.onProgressUpdate(values);
            Toast.makeText(activityScren, "Saving:" + values[0].getAttractive() ,
                    Toast.LENGTH_SHORT).show();

    }

    public byte[] ConverttoArrayByte(Bitmap bitmapConvert)
    {
        int chunkNumbers = 10;
        int bitmapSize = bitmapConvert.getRowBytes() * bitmapConvert.getHeight();
        byte[] imageBytes = new byte[bitmapSize];
        int rows, cols;
        int chunkHeight, chunkWidth;
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmapConvert.getHeight() / rows;
        chunkWidth = bitmapConvert.getWidth() / cols;

        int yCoord = 0;
        int bitmapsSizes = 0;

        for (int x = 0; x < rows; x++)
        {
            int xCoord = 0;
            for (int y = 0; y < cols; y++)
            {
                Bitmap bitmapChunk = Bitmap.createBitmap(bitmapConvert, xCoord, yCoord, chunkWidth, chunkHeight);
                byte[] bitmapArray = getBytesFromBitmapChunk(bitmapChunk);
                System.arraycopy(bitmapArray, 0, imageBytes, bitmapsSizes, bitmapArray.length);
                bitmapsSizes = bitmapsSizes + bitmapArray.length;
                xCoord += chunkWidth;

                bitmapChunk.recycle();
                bitmapChunk = null;
            }
            yCoord += chunkHeight;
        }

        return imageBytes;
    }
    private byte[] getBytesFromBitmapChunk(Bitmap bitmap)
    {
        int bitmapSize = bitmap.getRowBytes() * bitmap.getHeight();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bitmapSize);
        bitmap.copyPixelsToBuffer(byteBuffer);
        byteBuffer.rewind();
        return byteBuffer.array();
    }


    @Override
    protected void onPostExecute(FaceResult[] faceResults) {
        super.onPostExecute(faceResults);
        if (check)
        Toast.makeText(activityScren, "Save Success!" + faceResults.length,
                Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.VISIBLE);
    }
}

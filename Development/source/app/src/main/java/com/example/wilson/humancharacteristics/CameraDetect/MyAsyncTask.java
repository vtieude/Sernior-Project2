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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.bean.HumanDatabaseHelper;
import com.example.wilson.humancharacteristics.bean.HumanModel;
import com.example.wilson.humancharacteristics.model.FaceResult;

import org.opencv.face.Face;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public TextView textcharacterRecognize;
    public MyAsyncTask(Activity activity) {
        activityScren = activity;
        imageButton = activityScren.findViewById(R.id.takePhoto);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = (ProgressBar)activityScren.findViewById(R.id.progressBarCamera);
        textcharacterRecognize = activityScren.findViewById(R.id.text_characteristic);
        Toast.makeText(activityScren, activityScren.getString(R.string.wait_for_detect) ,
                Toast.LENGTH_SHORT).show();
        progress.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.INVISIBLE);
        SystemClock.sleep(1000);

    }
    public void getNameHuman(final FaceResult faceResult) {
        final int id = faceResult.getId();
        final HumanDatabaseHelper database = new HumanDatabaseHelper(activityScren);
        AlertDialog.Builder builder = new AlertDialog.Builder(activityScren);
        builder.setTitle("Save");
        Context context = activityScren.getApplicationContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        final ImageView imageView = new ImageView(context);
        Bitmap bmp32 = Bitmap.createScaledBitmap(faceResult.getBitmapFaceCrop(), 200, 200, false);
        imageView.setImageBitmap(bmp32);
        layout.addView(imageView);
        final EditText editName = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editName.setLayoutParams(lp);
        editName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_LENGTH)});
        editName.setText(activityScren.getString(R.string.type_name_here));
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName.setText("");
            }
        });
        layout.addView(editName);
        builder.setView(layout);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nameHuman = editName.getText().toString();
                if (nameHuman.equals("")){
                    nameHuman = activityScren.getString(R.string.default_name);
                }
                database.updateNameHuman(nameHuman, id);
                Toast.makeText(activityScren, activityScren.getString(R.string.saved) + ": "+ nameHuman ,
                        Toast.LENGTH_SHORT).show();
                dialog.dismiss();


//                textName.setText(editName.getText().toString());
//                textAge.setText(editAge.getText().toString());
            }
        });

//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        builder.getContext().setTheme(R.style.StyleForAlertDialgo);
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
                    humanModel.setAttracttive(String.valueOf(Integer.parseInt(faceResults[i].getAttractive().substring(1,2))+ 1));
                    humanModel.setDominant(String.valueOf(Integer.parseInt(faceResults[i].getDominant().substring(1,2))+ 1));
                    humanModel.setCompetent(String.valueOf(Integer.parseInt(faceResults[i].getCompetent().substring(1,2))+ 1));
                    humanModel.setExtroverted(String.valueOf(Integer.parseInt(faceResults[i].getExtroverted().substring(1,2))+ 1));
                    humanModel.setLikeability(String.valueOf(Integer.parseInt(faceResults[i].getLikeability().substring(1,2))+ 1));
                    humanModel.setThreadCharacteristic(String.valueOf(Integer.parseInt(faceResults[i].getThread().substring(1,2))+ 1));
                    humanModel.setTrustworthy(String.valueOf(Integer.parseInt(faceResults[i].getTrustworthy().substring(1,2))+ 1));
                    Bitmap bmp32 = Bitmap.createScaledBitmap(faceResults[i].getBitmapFaceCrop(), 100, 100, false);
                    humanModel.setImage(ConverttoArrayByte(bmp32));
                    humanModel.setName(nameHuman);
                    humanModel.setAge(18);
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    humanModel.setDateCreatAt(df.format(c));
                    humanModel.setComment(textcharacterRecognize.getText().toString());
                    humanModel.setEmail("Type");
                    humanModel.setPhone("Type");
                    database.addHuman(humanModel);
                    faceResults[i].setId(database.getLastHumanRow());
                    database.close();
                    publishProgress(faceResults[i]);

                }
//            publishProgress(check);

            }

        return faceResults;
    }

    @Override
    protected void onProgressUpdate(FaceResult... values) {
        super.onProgressUpdate(values);
        if (values[0].getBitmapFaceCrop()!= null){
            getNameHuman(values[0]);
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

        if (check) {
            Toast.makeText(activityScren, activityScren.getString(R.string.saving),
                    Toast.LENGTH_SHORT).show();
            check = false;
        }
        else {
            Toast.makeText(activityScren, "Cannot detect!",
                    Toast.LENGTH_SHORT).show();
        }
        progress.setVisibility(View.INVISIBLE);
        imageButton.setVisibility(View.VISIBLE);
//        imageButton.setEnabled(false);
        max_face = 0;
    }
}

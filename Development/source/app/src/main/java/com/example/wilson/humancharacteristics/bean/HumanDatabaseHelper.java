package com.example.wilson.humancharacteristics.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.nfc.tech.NfcA;
import android.util.Log;
import android.widget.Toast;

import com.example.wilson.humancharacteristics.R;
import com.example.wilson.humancharacteristics.Storage.StorageActivity;
import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Wilson on 3/7/2018.
 */

public class HumanDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static String DB_PATH = "/data/data/com.example.wilson.humancharacteristics/databases/";
    private static final String DATABASE_NAME = "Human";
    private static final String TABLE_HUMAN = "HumanInfor";
    private static final String TABLE_IMAGE = "ImageHuman";
    private static final String COLUMN_HUMAN_ID ="Id";
    private static final String COLUMN_HUMAN_NAME ="Name";
    private static final String COLUMN_HUMAN_AGE = "Age";
    private static final String COLUMN_HUMAN_COMMENT = "Comment";
    private static final String COLUMN_HUMAN_PHONE = "Phone";
    private static final String COLUMN_HUMAN_EMAIL = "Email";
    private static final String COLUMN_HUMAN_IMAGE = "Image";
    private static final String COLUMN_IMAGE = "Image";
    private static final String COLUMN_ATTRACTIVENESS = "Atractiveness";
    private static final String COLUMN_COMPETENT = "Competent";
    private static final String COLUMN_DOMINANT = "Dominant";
    private static final String COLUMN_Extroverted = "Extroverted";
    private static final String COLUMN_LIKEABILITY = "Likeability";
    private static final String COLUMN_THREAD = "Thread";
    private static final String COLUMN_TRUSTWORTHY = "Trustworthy";
    private static final String COLUMN_IMAGE_ID_HUMAN = "IdHuman";
    private static final String COLUMN_IMAGE_ID = "Id";
    private static final String COLUMN_DATE_CREAT = "DateCreat";
    public static final String DATE_FORMAT = "yyyyMMddHHmm";
    private static final SimpleDateFormat dateFormat = new
            SimpleDateFormat(DATE_FORMAT);
    public HumanDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String scriptHuman = "CREATE TABLE " + TABLE_HUMAN + "("
                + COLUMN_HUMAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_HUMAN_NAME + " TEXT,"
                + COLUMN_HUMAN_AGE + " INTEGER,"+ COLUMN_HUMAN_PHONE + " TEXT," + COLUMN_HUMAN_EMAIL
                + " TEXT," + COLUMN_HUMAN_COMMENT + " TEXT,"+ COLUMN_ATTRACTIVENESS+" TEXT,"
                + COLUMN_COMPETENT+" TEXT,"
                + COLUMN_DOMINANT+" TEXT,"
                + COLUMN_Extroverted+" TEXT,"
                + COLUMN_LIKEABILITY+" TEXT,"
                + COLUMN_THREAD+" TEXT,"
                + COLUMN_TRUSTWORTHY+" TEXT,"
                + COLUMN_DATE_CREAT + " TEXT,"
                + COLUMN_HUMAN_IMAGE + " BLOB" + ")";
        String scriptImage = "CREATE TABLE " + TABLE_IMAGE + "("
                + COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_IMAGE + " BLOB,"
                + COLUMN_IMAGE_ID_HUMAN + " INTEGER" + ")";
        sqLiteDatabase.execSQL(scriptHuman);
//        sqLiteDatabase.execSQL(scriptImage);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HUMAN);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(sqLiteDatabase);
    }
    //create default value to check
//    public void createDefaultValue() {
//        int count = this.getHumanCount();
//        if(count == 0 ) {
//            HumanModel humanModel = new HumanModel("Mynameis",30,"Handsome boy","0976554445","gmail@gmail.com");
//            HumanModel humanModel2 = new HumanModel("TenToiLa",40,"Handsome boy","0976554445","gmail@gmail.com");
//            this.addHuman(humanModel);
//            this.addHuman(humanModel2);
//        }
//    }
    public int getHumanCount() {
        String countQuery = "SELECT  * FROM " + TABLE_HUMAN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /// code with human table
    public void addHuman(HumanModel human) throws SQLException {
        Log.i(TAG, "Add image ... " );
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HUMAN_NAME, human.getName());
        values.put(COLUMN_HUMAN_AGE, human.getAge());
        values.put(COLUMN_HUMAN_PHONE, human.getPhone());
        values.put(COLUMN_HUMAN_EMAIL, human.getEmail());
        values.put(COLUMN_HUMAN_COMMENT, human.getComment());
        values.put(COLUMN_HUMAN_IMAGE, human.getImage());
        values.put(COLUMN_DATE_CREAT, human.getDateCreatAt());
        values.put(COLUMN_ATTRACTIVENESS, human.getAttracttive());
        values.put(COLUMN_COMPETENT, human.getCompetent());
        values.put(COLUMN_LIKEABILITY, human.getLikeability());
        values.put(COLUMN_DOMINANT, human.getDominant());
        values.put(COLUMN_TRUSTWORTHY, human.getTrustworthy());
        values.put(COLUMN_THREAD, human.getThreadCharacteristic());
        values.put(COLUMN_Extroverted, human.getExtroverted());

        db.insert(TABLE_HUMAN,null, values);
        db.close();
    }
    public HumanModel getHuman(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HUMAN + " where " + COLUMN_HUMAN_ID + " = " +id+ "", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        HumanModel humanModel = new HumanModel(
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_AGE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_COMMENT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_PHONE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_EMAIL)),
                cursor.getBlob(cursor.getColumnIndex(COLUMN_HUMAN_IMAGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ATTRACTIVENESS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMPETENT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DOMINANT)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Extroverted)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIKEABILITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_THREAD)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TRUSTWORTHY))
        );
        humanModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_ID)));
        cursor.close();
        return humanModel;
    }
    public int getLastHumanRow() {
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HUMAN, null);
        if (cursor != null) {
            cursor.moveToLast();
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_ID));
        }
        cursor.close();
        return id ;
    }
    public List<HumanModel> getListHuman(){
        List<HumanModel> list = new ArrayList<HumanModel>();

        String selectQuery = "SELECT * FROM "+ TABLE_HUMAN ; //+ " Limit 0,5"
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToLast()) {
            do {
                HumanModel humanModel = new HumanModel();
                humanModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_ID)));
                humanModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_NAME)));
                humanModel.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_AGE)));
                humanModel.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_PHONE)));
                humanModel.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_EMAIL)));
                humanModel.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_COMMENT)));
                humanModel.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_HUMAN_IMAGE)));
                humanModel.setDateCreatAt(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_CREAT)));
                humanModel.setAttracttive(cursor.getString(cursor.getColumnIndex(COLUMN_ATTRACTIVENESS)));
                humanModel.setCompetent(cursor.getString(cursor.getColumnIndex(COLUMN_COMPETENT)));
                humanModel.setDominant(cursor.getString(cursor.getColumnIndex(COLUMN_DOMINANT)));
                humanModel.setExtroverted(cursor.getString(cursor.getColumnIndex(COLUMN_Extroverted)));
                humanModel.setLikeability(cursor.getString(cursor.getColumnIndex(COLUMN_LIKEABILITY)));
                humanModel.setThreadCharacteristic(cursor.getString(cursor.getColumnIndex(COLUMN_THREAD)));
                humanModel.setTrustworthy(cursor.getString(cursor.getColumnIndex(COLUMN_TRUSTWORTHY)));
                list.add(humanModel);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void updateNameHuman(String name, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HUMAN_NAME, name);
        db.update(TABLE_HUMAN, values, COLUMN_HUMAN_ID + " = ? ", new String[] { Integer.toString(id) } );
        db.close();
    }
    public int updateHuman (HumanModel human)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HUMAN_NAME, human.getName());
        values.put(COLUMN_HUMAN_AGE, human.getAge());
        values.put(COLUMN_HUMAN_PHONE, human.getPhone());
        values.put(COLUMN_HUMAN_EMAIL, human.getEmail());
        values.put(COLUMN_HUMAN_COMMENT, human.getComment());
//        values.put(COLUMN_HUMAN_IMAGE,human.getImage());
//        values.put(COLUMN_ATTRACTIVENESS, human.getAttracttive());
//        values.put(COLUMN_COMPETENT, human.getCompetent());
//        values.put(COLUMN_DOMINANT, human.getDominant());
//        values.put(COLUMN_Extroverted, human.getExtroverted());
//        values.put(COLUMN_LIKEABILITY, human.getLikeability());
//        values.put(COLUMN_THREAD, human.getThreadCharacteristic());
//        values.put(COLUMN_TRUSTWORTHY, human.getTrustworthy());
        db.update(TABLE_HUMAN, values, COLUMN_HUMAN_ID + " = ? ", new String[] { Integer.toString(human.getId()) } );
        db.close();
        return 0;
    }
    public void deleteHuman (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HUMAN,
                COLUMN_HUMAN_ID + " = ? ",
                new String[] { Integer.toString(id) });
        db.close();
    }


    //code with image human
    public void addImage(ImageHumanModel image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.putNull(COLUMN_IMAGE_ID_HUMAN);
        values.put(COLUMN_IMAGE, image.getImage());
        db.insert(TABLE_HUMAN,null, values);
        db.close();
    }


    public ImageHumanModel getImageDefault(int idHuman) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_IMAGE + " where " + COLUMN_IMAGE_ID_HUMAN + " = "+idHuman+"", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ImageHumanModel imageModel = new ImageHumanModel(
                cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_ID)),
                idHuman,
                cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
        );
        cursor.close();
        db.close();
        return imageModel;
    }
    public List<ImageHumanModel> getListImage(int idHuman){
        List<ImageHumanModel> list = new ArrayList<ImageHumanModel>();
        String selectQuery = "SELECT * FROM "+ TABLE_IMAGE + " WHERE " + COLUMN_HUMAN_ID + " = " + idHuman + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                ImageHumanModel imageModel = new ImageHumanModel();
                imageModel.setIdHuman(cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGE_ID_HUMAN)));
                imageModel.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                list.add(imageModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public void deleteImageById() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE,
                COLUMN_IMAGE_ID_HUMAN + " is NULL ",
                null);
        db.close();
    }
    public void deleteImageById (int idHuman)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGE,
                COLUMN_IMAGE_ID_HUMAN + " = ? ",
                new String[] { Integer.toString(idHuman) });
        db.close();
    }
}

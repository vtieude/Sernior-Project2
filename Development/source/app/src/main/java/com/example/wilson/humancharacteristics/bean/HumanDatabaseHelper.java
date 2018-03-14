package com.example.wilson.humancharacteristics.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wilson.humancharacteristics.bean.HumanModel;

import java.util.ArrayList;
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
    private static final String COLUMN_HUMAN_ID ="Id";
    private static final String COLUMN_HUMAN_NAME ="Name";
    private static final String COLUMN_HUMAN_AGE = "Age";
    private static final String COLUMN_HUMAN_COMMENT = "Comment";
    private static final String COLUMN_HUMAN_PHONE = "Phone";
    private static final String COLUMN_HUMAN_EMAIL = "Email";
    private static final String COLUMN_HUMAN_IMAGE = "Image";

    public HumanDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String script = "CREATE TABLE " + TABLE_HUMAN + "("
                + COLUMN_HUMAN_ID + " INTEGER PRIMARY KEY," + COLUMN_HUMAN_NAME + " TEXT,"
                + COLUMN_HUMAN_AGE + " INTEGER,"+ COLUMN_HUMAN_PHONE + " TEXT," + COLUMN_HUMAN_EMAIL
                + " TEXT," + COLUMN_HUMAN_COMMENT + "TEXT," + COLUMN_HUMAN_IMAGE + "BLOB" + ")";
        sqLiteDatabase.execSQL(script);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_HUMAN);
        onCreate(sqLiteDatabase);
    }
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
        db.insert(TABLE_HUMAN,null, values);
        db.close();
    }
    public HumanModel getHuman(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_NAME + " where " + " id =  "+id+"", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        HumanModel humanModel = new HumanModel(
                cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_NAME)),
                cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_AGE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_COMMENT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_PHONE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_EMAIL)),
                cursor.getBlob(cursor.getColumnIndex(COLUMN_HUMAN_IMAGE))
        );
        return humanModel;
    }
    public List<HumanModel> getListData(){
        List<HumanModel> list = new ArrayList<HumanModel>();
        String selectQuery = "SELECT * FROM "+ TABLE_HUMAN;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                HumanModel humanModel = new HumanModel();
                humanModel.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_ID)));
                humanModel.setName(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_NAME)));
                humanModel.setAge(cursor.getInt(cursor.getColumnIndex(COLUMN_HUMAN_AGE)));
                humanModel.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_PHONE)));
                humanModel.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_EMAIL)));
                humanModel.setComment(cursor.getString(cursor.getColumnIndex(COLUMN_HUMAN_COMMENT)));
                humanModel.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_HUMAN_IMAGE)));
                list.add(humanModel);
            } while (cursor.moveToNext());
        }
        return list;
    }
    public boolean updateHuman (HumanModel human)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HUMAN_NAME, human.getName());
        values.put(COLUMN_HUMAN_AGE, human.getAge());
        values.put(COLUMN_HUMAN_PHONE, human.getPhone());
        values.put(COLUMN_HUMAN_EMAIL, human.getEmail());
        values.put(COLUMN_HUMAN_COMMENT, human.getComment());
        values.put(COLUMN_HUMAN_IMAGE, human.getImage());
        db.update(TABLE_HUMAN, values, "id = ? ", new String[] { Integer.toString(human.getId()) } );
        return true;
    }
    public Integer deleteHuman (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HUMAN,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

}

package com.example.p2124702assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class PictureHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Picture.db";
    private static final int SCHEMA_VERSION = 1;

    public PictureHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String exec = "CREATE TABLE picturest(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT, captions TEXT, " +
                " image BLOB, lat REAL, lon REAL); ";
        db.execSQL(exec);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Wont be called unless schema version increases, allows for new features like adding more tables
        db.execSQL("DROP TABLE IF EXISTS picturest");
    }

    public Boolean insertData(String title, String caption, double lat, double lon, Bitmap img){ //Insert data into database
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        byte[] data = getBitmapAsByteArray(img);
        contentValues.put("title",title);
        contentValues.put("captions", caption);
        contentValues.put("lat", lat);
        contentValues.put("lon",lon);
        contentValues.put("image",data);

        long result = MyDB.insert("picturest",null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public int getProfilesCount() {
        String countQuery = "SELECT  * FROM  picturest";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public Cursor getAll(){
        String exec = "SELECT _id , title ," +
                " captions , " +
                " image , lat , lon  FROM picturest ORDER BY _id";
        return (getReadableDatabase().rawQuery(exec,null));
    }

    public Cursor getById(String id){
        String [] args = {id};
        String exec = "SELECT _id ," +
                " title , captions , " +
                " image , lat , lon  FROM picturest WHERE _id = ?";

        return (getReadableDatabase().rawQuery(exec,args));
    }

    public Boolean update(String title, String captions, String id, double lat, double lon, Bitmap img){
        ContentValues cv = new ContentValues();
        byte[] data = getBitmapAsByteArray(img);
        String[] args = {id};
        cv.put("title", title);
        cv.put("captions", captions);
        cv.put("lat", lat);
        cv.put("lon", lon);
        cv.put("image",data);

        long result = getWritableDatabase().update("picturest", cv, "_id = ?", args);
        if(result==-1) return false;
        else
            return true;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        return outputStream.toByteArray();
    }

    public String getID(Cursor c) { return (c.getString(0));}
    public String getTitle(Cursor c) {
        return (c.getString(1));
    }
    public String getCaptions(Cursor c) {
        return (c.getString(2));
    }
    public double getLatitude(Cursor c) {
        return (c.getDouble(4));
    }
    public double getLongitude(Cursor c) {
        return (c.getDouble(5));
    }
    public byte[] getImage(Cursor c) {return (c.getBlob(3));}

//    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
//
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
//
//        // The new size we want to scale to
//        final int REQUIRED_SIZE = 100;
//
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//        int scale = 1;
//        while (true) {
//            if (width_tmp / 2 < REQUIRED_SIZE
//                    || height_tmp / 2 < REQUIRED_SIZE) {
//                break;
//            }
//            width_tmp /= 2;
//            height_tmp /= 2;
//            scale *= 2;
//        }
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
//
//    }



}

package com.speedsumm.bu.gba2l3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by bu on 26.07.2016.
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DB_MARKERS";
    private static final String TABLE_NAME = "TB_MARKERS";
    private static final String ID_KEY = "ID";
    private static final String CELL_ID_KEY = "CELL_ID";
    private static final String CELL_LAT_KEY = "CELL_LAT";
    private static final String CELL_LON_KEY = "CELL_LON";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_STRING = "CREATE TABLE " + TABLE_NAME + " (" + ID_KEY + " INTEGER PRIMARY KEY, " + CELL_ID_KEY + " INTEGER, " + CELL_LAT_KEY + " DOUBLE, " + CELL_LON_KEY + " DOUBLE)";
        sqLiteDatabase.execSQL(CREATE_STRING);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }

    public void addMMarker(Marker marker) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CELL_ID_KEY, marker.getCellID());
        cv.put(CELL_LAT_KEY, marker.getCellLat());
        cv.put(CELL_LON_KEY, marker.getCellLon());
        sqLiteDatabase.insert(TABLE_NAME, null, cv);
    }

    public HashSet<Marker> getAllMarkers(HashSet hashMarkers) {
//        ArrayList<Task> taskArrayList = new ArrayList<>();
        hashMarkers.clear();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (cursor.moveToNext()) {
            Marker marker = new Marker();
            marker.setId(cursor.getInt(cursor.getColumnIndex(ID_KEY)));
            marker.setCellID(cursor.getInt(cursor.getColumnIndex(CELL_ID_KEY)));
            marker.setCellLat(cursor.getDouble(cursor.getColumnIndex(CELL_LAT_KEY)));
            marker.setCellLon(cursor.getDouble(cursor.getColumnIndex(CELL_LON_KEY)));
            hashMarkers.add(marker);
        }
        cursor.close();
        return hashMarkers;
    }
}

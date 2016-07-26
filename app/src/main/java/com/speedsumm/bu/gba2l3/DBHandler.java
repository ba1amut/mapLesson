package com.speedsumm.bu.gba2l3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bu on 26.07.2016.
 */
public class DBHandler extends SQLiteOpenHelper{


    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "DB_CELLS";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

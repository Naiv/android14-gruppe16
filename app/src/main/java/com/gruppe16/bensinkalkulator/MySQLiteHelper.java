package com.gruppe16.bensinkalkulator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TRIPS = "trips";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATETIME = "datetime";
    public static final String COLUMN_COST = "cost";
    public static final String COLUMN_DISTANCE = "distance";

    private static final String DATABASE_NAME = "trips.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table "+ TABLE_TRIPS + "("
            + COLUMN_ID+ " integer primary key autoincrement, "
            + COLUMN_DATETIME+ " text not null, "
<<<<<<< HEAD
            + COLUMN_COST+ " integer not null, "
            + COLUMN_DISTANCE+ " integer not null);";
=======
            + COLUMN_COST+ " text not null, "
            + COLUMN_DISTANCE+ " text not null);";
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data"
        );
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
        onCreate(db);
    }

}
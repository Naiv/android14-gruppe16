package com.gruppe16.bensinkalkulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikael on 24.03.14.
 */
public class TripsDataSource {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DATETIME, MySQLiteHelper.COLUMN_COST, MySQLiteHelper.COLUMN_DISTANCE};

    public TripsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

<<<<<<< HEAD
    public Trip createTrip(String datetime, long cost, long distance) {
=======
    public Trip createTrip(String datetime, String cost, String distance) {
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATETIME, datetime);
        values.put(MySQLiteHelper.COLUMN_COST, cost);
        values.put(MySQLiteHelper.COLUMN_DISTANCE, distance);
        long insertId = database.insert(MySQLiteHelper.TABLE_TRIPS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRIPS,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Trip newTrip = cursorToTrip(cursor);
        cursor.close();
        return newTrip;
    }

    public void deleteAll(){
        database.delete(MySQLiteHelper.TABLE_TRIPS, null, null);
    }
    //Ikke i bruk for øyeblikket
    /*
    public void deleteComment(Trip trip) {
        long id = trip.getId();
        System.out.println("Trip deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_TRIPS, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }
    */

    public List<Trip> getAllTrips() {
        List<Trip> trips = new ArrayList<Trip>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_TRIPS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Trip trip = cursorToTrip(cursor);
            trips.add(trip);
            cursor.moveToNext();
        }
        cursor.close();
        return trips;
    }

    private Trip cursorToTrip(Cursor cursor) {
        Trip trip = new Trip();
        trip.setId(cursor.getLong(0));
        trip.setDatetime(cursor.getString(1));
<<<<<<< HEAD
        trip.setCost(cursor.getLong(2));
        trip.setDistance(cursor.getLong(3));
=======
        trip.setCost(cursor.getString(2));
        trip.setDistance(cursor.getString(3));
>>>>>>> c5d27ba699e3df585172e9c4ba5ed865c712c3f5
        return trip;
    }
}

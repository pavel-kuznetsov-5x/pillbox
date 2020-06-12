package com.cucumber007.pillbox.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class PillboxEventsTable {

    // Database table
    public static final String TABLE_NAME = "pillbox_events";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MED_ID = "med_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_STATUS = "status";

    // Database creation sql statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MED_ID + " integer, "
            + COLUMN_DATE + " text not null, "
            + COLUMN_TIME + " text not null, "
            + COLUMN_STATUS + " integer not null, "
            + " FOREIGN KEY ("+ COLUMN_MED_ID +") REFERENCES "+MedsTable.TABLE_NAME+" ("+MedsTable.COLUMN_ID+") ON DELETE CASCADE" +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
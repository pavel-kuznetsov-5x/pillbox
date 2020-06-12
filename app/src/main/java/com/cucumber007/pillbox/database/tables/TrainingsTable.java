package com.cucumber007.pillbox.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class TrainingsTable {

    // Database table
    public static final String TABLE_NAME = "trainings";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_GLOBAL_ID = "global_id";
    public static final String COLUMN_JSON = "json";

    // Database creation sql statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_GLOBAL_ID + " integer, "
            + COLUMN_JSON + " text " +
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

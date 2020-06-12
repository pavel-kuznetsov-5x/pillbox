package com.cucumber007.pillbox.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class MedsTable {

    // Database table
    public static final String TABLE_NAME = "meds";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_ICON_COLOR = "icon_color";

    public static final String COLUMN_DOSAGE_VALUE = "dosage_value";
    public static final String COLUMN_DOSAGE_UNIT_ID = "dosage_unit_id";

    public static final String COLUMN_DAILY_USAGE_VALUE = "daily_usage_value";
    public static final String COLUMN_DAILY_USAGE_UNIT_ID = "daily_usage_unit_id";

    public static final String COLUMN_RECURRENCE_VALUE = "recurrence_value";
    public static final String COLUMN_RECURRENCE_UNIT_ID = "recurrence_unit_id";

    public static final String COLUMN_DURATION_VALUE = "duration_value";
    public static final String COLUMN_DURATION_UNIT_ID = "duration_unit_id";
    
    public static final String COLUMN_START_DATE_EPOCH_DAYS = "start_date_epoch_days";

    public static final String COLUMN_START_TIME_SECONDS = "start_time_seconds";
    public static final String COLUMN_END_TIME_SECONDS = "end_time_seconds";
    public static final String COLUMN_TIME_TO_TAKE_LIST = "time_to_take_list";

    // Database creation sql statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_NAME + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NAME + " text, "

            + COLUMN_DOSAGE_VALUE + " integer, "
            + COLUMN_DOSAGE_UNIT_ID + " text, "

            + COLUMN_DAILY_USAGE_VALUE + " integer, "
            + COLUMN_DAILY_USAGE_UNIT_ID + " text, "

            + COLUMN_RECURRENCE_VALUE + " integer, "
            + COLUMN_RECURRENCE_UNIT_ID + " text, "

            + COLUMN_DURATION_VALUE + " integer, "
            + COLUMN_DURATION_UNIT_ID + " text, "

            + COLUMN_START_DATE_EPOCH_DAYS + " int, "

            + COLUMN_START_TIME_SECONDS + " int, "
            + COLUMN_END_TIME_SECONDS + " int, "
            + COLUMN_TIME_TO_TAKE_LIST + " text, "

            + COLUMN_ICON + " text, "
            + COLUMN_ICON_COLOR + " integer " +
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

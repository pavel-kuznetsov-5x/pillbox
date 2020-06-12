package com.cucumber007.pillbox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.database.tables.PillboxEventsTable;
import com.cucumber007.pillbox.database.tables.TrainingsTable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        PillboxEventsTable.onCreate(database);
        MedsTable.onCreate(database);
        TrainingsTable.onCreate(database);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PillboxEventsTable.onUpgrade(db, oldVersion, newVersion);
        MedsTable.onUpgrade(db, oldVersion, newVersion);
        TrainingsTable.onUpgrade(db, oldVersion, newVersion);
        onCreate(db);
    }

}
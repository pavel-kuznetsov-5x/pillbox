package com.cucumber007.pillbox.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.database.tables.PillboxEventsTable;
import com.cucumber007.pillbox.database.tables.TrainingsTable;

import java.util.HashMap;


public class PillboxContentProvider extends ContentProvider {

    // database
    private DbHelper database;

    // Uri codes used for the UriMacher
    private static final int PILLBOX_EVENTS = 1;
    private static final int PILLBOX_EVENT_ID = 2;
    private static final int MEDS = 3;
    private static final int MED_ID = 4;
    private static final int TRAININGS = 5;
    private static final int TRAINING_ID = 6;

    private static final String AUTHORITY = "com.cucumber007.pillbox.contentprovider";
    private static final String BASE_PATH = "pillbox";

    private static final String PILLBOX_EVENTS_PATH = BASE_PATH + "/" + PillboxEventsTable.TABLE_NAME;
    private static final String MEDS_PATH = BASE_PATH + "/" + MedsTable.TABLE_NAME;
    private static final String TRAININGS_PATH = BASE_PATH + "/" + TrainingsTable.TABLE_NAME;

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    
    public static final Uri PILLBOX_EVENTS_URI = Uri.parse("content://" + AUTHORITY + "/" + PILLBOX_EVENTS_PATH);
    public static final Uri MEDS_URI = Uri.parse("content://" + AUTHORITY + "/" + MEDS_PATH);
    public static final Uri TRAININGS_URI = Uri.parse("content://" + AUTHORITY + "/" + TRAININGS_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PILLBOX_EVENTS_PATH, PILLBOX_EVENTS);
        sURIMatcher.addURI(AUTHORITY, PILLBOX_EVENTS_PATH + "/#", PILLBOX_EVENT_ID);
        sURIMatcher.addURI(AUTHORITY, MEDS_PATH, MEDS);
        sURIMatcher.addURI(AUTHORITY, MEDS_PATH + "/#", MED_ID);
        sURIMatcher.addURI(AUTHORITY, TRAININGS_PATH, TRAININGS);
        sURIMatcher.addURI(AUTHORITY, TRAININGS_PATH + "/#", TRAINING_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        HashMap<String, String> pillboxEventsJoinProjectionMap = new HashMap();

        //LET'S GET READY TO THE RUUUUUUMBLE
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_ID, "PE" + "." + MedsTable.COLUMN_ID + " as _id");
        pillboxEventsJoinProjectionMap.put(PillboxEventsTable.COLUMN_DATE, PillboxEventsTable.COLUMN_DATE);
        pillboxEventsJoinProjectionMap.put(PillboxEventsTable.COLUMN_TIME, PillboxEventsTable.COLUMN_TIME);
        pillboxEventsJoinProjectionMap.put(PillboxEventsTable.COLUMN_STATUS, PillboxEventsTable.COLUMN_STATUS);
        pillboxEventsJoinProjectionMap.put(PillboxEventsTable.COLUMN_MED_ID, PillboxEventsTable.COLUMN_MED_ID);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DOSAGE_UNIT_ID, MedsTable.COLUMN_DOSAGE_UNIT_ID);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DOSAGE_VALUE, MedsTable.COLUMN_DOSAGE_VALUE);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DAILY_USAGE_UNIT_ID, MedsTable.COLUMN_DAILY_USAGE_UNIT_ID);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DAILY_USAGE_VALUE, MedsTable.COLUMN_DAILY_USAGE_VALUE);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DURATION_UNIT_ID, MedsTable.COLUMN_DURATION_UNIT_ID);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_DURATION_VALUE, MedsTable.COLUMN_DURATION_VALUE);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_ICON, MedsTable.COLUMN_ICON);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_ICON_COLOR, MedsTable.COLUMN_ICON_COLOR);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_NAME, MedsTable.COLUMN_NAME);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_RECURRENCE_UNIT_ID, MedsTable.COLUMN_RECURRENCE_UNIT_ID);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_RECURRENCE_VALUE, MedsTable.COLUMN_RECURRENCE_VALUE);
        pillboxEventsJoinProjectionMap.put(MedsTable.COLUMN_START_DATE_EPOCH_DAYS, MedsTable.COLUMN_START_DATE_EPOCH_DAYS);

        String join = PillboxEventsTable.TABLE_NAME+" as PE LEFT OUTER JOIN "+MedsTable.TABLE_NAME+" as MED"+
                " on PE."+PillboxEventsTable.COLUMN_MED_ID+" = MED."+MedsTable.COLUMN_ID;

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PILLBOX_EVENTS:
                queryBuilder.setTables(join);
                queryBuilder.setProjectionMap(pillboxEventsJoinProjectionMap);
                break;
            case PILLBOX_EVENT_ID:
                queryBuilder.setTables(join);
                queryBuilder.setProjectionMap(pillboxEventsJoinProjectionMap);
                queryBuilder.appendWhere(PillboxEventsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case MEDS:
                queryBuilder.setTables(MedsTable.TABLE_NAME);
                break;
            case MED_ID:
                queryBuilder.setTables(MedsTable.TABLE_NAME);
                queryBuilder.appendWhere(MedsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case TRAININGS:
                queryBuilder.setTables(TrainingsTable.TABLE_NAME);
                break;
            case TRAINING_ID:
                queryBuilder.setTables(TrainingsTable.TABLE_NAME);
                queryBuilder.appendWhere(TrainingsTable.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case PILLBOX_EVENTS:
                id = sqlDB.insert(PillboxEventsTable.TABLE_NAME, null, values);
                break;
            case MEDS:
                id = sqlDB.insert(MedsTable.TABLE_NAME, null, values);
                break;
            case TRAININGS:
                id = sqlDB.insert(TrainingsTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PILLBOX_EVENTS:
                rowsDeleted = sqlDB.delete(PillboxEventsTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PILLBOX_EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PillboxEventsTable.TABLE_NAME,
                            PillboxEventsTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(PillboxEventsTable.TABLE_NAME,
                            PillboxEventsTable.COLUMN_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case MEDS:
                rowsDeleted = sqlDB.delete(MedsTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case MED_ID:
                String med_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(MedsTable.TABLE_NAME,
                            MedsTable.COLUMN_ID + "=" + med_id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(MedsTable.TABLE_NAME,
                            MedsTable.COLUMN_ID + "=" + med_id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            case TRAININGS:
                rowsDeleted = sqlDB.delete(TrainingsTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case TRAINING_ID:
                String training_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(TrainingsTable.TABLE_NAME,
                            MedsTable.COLUMN_ID + "=" + training_id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(TrainingsTable.TABLE_NAME,
                            MedsTable.COLUMN_ID + "=" + training_id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PILLBOX_EVENTS:
                rowsUpdated = sqlDB.update(PillboxEventsTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PILLBOX_EVENT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(PillboxEventsTable.TABLE_NAME,
                            values,
                            PillboxEventsTable.COLUMN_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(PillboxEventsTable.TABLE_NAME,
                            values,
                            PillboxEventsTable.COLUMN_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case MEDS:
                rowsUpdated = sqlDB.update(MedsTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case  MED_ID:
                String med_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(MedsTable.TABLE_NAME,
                            values,
                            MedsTable.COLUMN_ID + "=" + med_id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(MedsTable.TABLE_NAME,
                            values,
                            MedsTable.COLUMN_ID + "=" + med_id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            case TRAININGS:
                rowsUpdated = sqlDB.update(TrainingsTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TRAINING_ID:
                String training_id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(TrainingsTable.TABLE_NAME,
                            values,
                            MedsTable.COLUMN_ID + "=" + training_id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(TrainingsTable.TABLE_NAME,
                            values,
                            MedsTable.COLUMN_ID + "=" + training_id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}

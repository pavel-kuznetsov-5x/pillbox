package com.cucumber007.pillbox.models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RecoverySystem;

import com.cucumber007.pillbox.database.PillboxContentProvider;
import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.database.tables.PillboxEventsTable;
import com.cucumber007.pillbox.objects.pills.PillboxEvent;
import com.cucumber007.pillbox.objects.pills.parameters.Dosage;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

public class ReminderModel {
    private Context context;
    private final ContentResolver contentResolver;


    public ReminderModel(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
    }


    public boolean isEventsForDay(LocalDate day) {
        Cursor result = contentResolver.query(PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                                              PillboxEventsTable.COLUMN_DATE + " = ?", new String[]{day.toString()}, null, null);
        int count = result.getCount();
        result.close();
        return count != 0;
    }


    public List<PillboxEvent> getEventsByDay(LocalDate day) {
        List<PillboxEvent> events = new ArrayList<>();

        Cursor result = contentResolver.query(
                PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                PillboxEventsTable.COLUMN_DATE + " = ?", new String[]{day.toString()}, null, null);

        if (result.getCount() == 0) return events;
        if (result.moveToFirst()) {
            do {
                events.add(getPillboxEventFromCursor(result));
            } while (result.moveToNext());
        }
        result.close();
        return events;
    }


    public List<PillboxEvent> getEventsByMed(int medId) {
        List<PillboxEvent> events = new ArrayList<>();

        Cursor result = contentResolver.query(
                PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                PillboxEventsTable.COLUMN_MED_ID + " = " + medId, null, null, null);

        if (result.getCount() == 0) return events;
        if (result.moveToFirst()) {
            do {
                events.add(getPillboxEventFromCursor(result));
            } while (result.moveToNext());
        }
        result.close();
        return events;
    }


    public List<PillboxEvent> getEvents() {
        List<PillboxEvent> events = new ArrayList<>();

        Cursor result = contentResolver.query(
                PillboxContentProvider.PILLBOX_EVENTS_URI, null, null, null, null, null);

        if (result != null) {
            if (result.getCount() == 0) return events;
            if (result.moveToFirst()) {
                do {
                    events.add(getPillboxEventFromCursor(result));
                } while (result.moveToNext());
            }
            result.close();
        }
        return events;
    }


    public List<PillboxEvent> createPillboxEvents(int medId, String medName,
                                                  List<PillboxEvent> events) {
        return createPillboxEvents(medId, medName, events, new RecoverySystem.ProgressListener() {
            @Override
            public void onProgress(int progress) {

            }
        });
    }


    public List<PillboxEvent> createPillboxEvents(int medId, String medName,
                                                  List<PillboxEvent> events, RecoverySystem.ProgressListener listener) {
        for (int i = 0; i < events.size(); i++) {
            PillboxEvent event = events.get(i);
            event.setMedId(medId);
            event.setMedName(medName);
            event.setId(createPillboxEvent(event));
            listener.onProgress((int) (((float) i / events.size()) * 100));
        }
        return events;
    }


    public int createPillboxEvent(PillboxEvent event) {
        ContentValues values = new ContentValues();
        values.put(PillboxEventsTable.COLUMN_MED_ID, event.getMedId());
        values.put(PillboxEventsTable.COLUMN_DATE, event.getDate().toString());
        values.put(PillboxEventsTable.COLUMN_TIME, event.getTime().toString());
        values.put(PillboxEventsTable.COLUMN_STATUS, event.getStatus());
        Uri uri = contentResolver.insert(PillboxContentProvider.PILLBOX_EVENTS_URI, values);
        return Integer.parseInt(uri.getLastPathSegment());
    }


    public void setPillboxEventStatus(long eventId, int status) {
        ContentValues values = new ContentValues();
        values.put(PillboxEventsTable.COLUMN_STATUS, status);
        contentResolver.update(PillboxContentProvider.PILLBOX_EVENTS_URI, values, "_id = " + eventId, null);
    }


    public int getEventsQuantityByMed(int medId, int status) {
        Cursor result = contentResolver.query(PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                                              PillboxEventsTable.COLUMN_MED_ID + " = " + medId + " AND " + PillboxEventsTable.COLUMN_STATUS + " = " + status, null, null, null);
        int count = result.getCount();
        result.close();
        return count;
    }


    public int getEventsQuantityByMed(int medId) {
        Cursor result = contentResolver.query(PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                                              PillboxEventsTable.COLUMN_MED_ID + " = " + medId, null, null, null);
        int count = result.getCount();
        result.close();
        return count;
    }


    private PillboxEvent getPillboxEventFromCursor(Cursor cursor) {
        return new PillboxEvent(
                cursor.getInt(cursor.getColumnIndex(PillboxEventsTable.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(PillboxEventsTable.COLUMN_MED_ID)),
                LocalDate.parse(
                        cursor.getString(cursor.getColumnIndex(PillboxEventsTable.COLUMN_DATE))),
                LocalTime.parse(
                        cursor.getString(cursor.getColumnIndex(PillboxEventsTable.COLUMN_TIME))),
                new Dosage(
                        cursor.getInt(cursor.getColumnIndex(MedsTable.COLUMN_DOSAGE_VALUE)),
                        cursor.getInt(cursor.getColumnIndex(MedsTable.COLUMN_DOSAGE_UNIT_ID))
                ),
                cursor.getInt(cursor.getColumnIndex(PillboxEventsTable.COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(MedsTable.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(MedsTable.COLUMN_ICON)),
                cursor.getInt(cursor.getColumnIndex(MedsTable.COLUMN_ICON_COLOR))
        );
    }

}

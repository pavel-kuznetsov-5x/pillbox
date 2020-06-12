package com.cucumber007.pillbox.models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.cucumber007.pillbox.database.PillboxContentProvider;
import com.cucumber007.pillbox.database.tables.MedsTable;
import com.cucumber007.pillbox.database.tables.PillboxEventsTable;
import com.cucumber007.pillbox.objects.pills.Med;
import com.cucumber007.pillbox.objects.pills.PillboxEvent;
import com.cucumber007.pillbox.objects.pills.parameters.Dosage;
import com.cucumber007.pillbox.objects.pills.parameters.Duration;
import com.cucumber007.pillbox.objects.pills.parameters.Recurrence;
import com.cucumber007.pillbox.objects.pills.parameters.StartDate;
import com.cucumber007.pillbox.objects.pills.parameters.TimeToTake;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

public class PillsModel {
    private Context context;
    private final ContentResolver contentResolver;

    public PillsModel(Context context, ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.context = context;
    }

    public int createMed(Med med) {
        Uri uri = contentResolver.insert(PillboxContentProvider.MEDS_URI, med.toContentValues());
        return Integer.parseInt(uri.getLastPathSegment());
    }

    public void editMed(Med med) {
        contentResolver.delete(PillboxContentProvider.PILLBOX_EVENTS_URI,
                PillboxEventsTable.COLUMN_MED_ID + " = ?" + " AND " + PillboxEventsTable.COLUMN_STATUS + " == ?",
                new String[]{"" + med.getId(), PillboxEvent.STATUS_NONE+""});
        contentResolver.update(PillboxContentProvider.MEDS_URI, med.toContentValues(), "_id = " + med.getId(), null);
    }

    public void deleteMed(int medId) {
        contentResolver.delete(PillboxContentProvider.MEDS_URI, "_id = " + medId, null);
    }

    public Med getMed(int medId) {
        Cursor result = contentResolver.query(PillboxContentProvider.MEDS_URI, null, "_id = " + medId, null, null);
        result.moveToFirst();
        if(result.getCount() == 0) return null;
        Med med = new Med(medId,
                result.getString(result.getColumnIndex(MedsTable.COLUMN_NAME)),
                result.getString(result.getColumnIndex(MedsTable.COLUMN_ICON)),
                result.getInt(result.getColumnIndex(MedsTable.COLUMN_ICON_COLOR)),
                new Dosage(
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_DOSAGE_VALUE)),
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_DOSAGE_UNIT_ID))
                ),
                new Recurrence(
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_RECURRENCE_VALUE)),
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_RECURRENCE_UNIT_ID))
                ),
                new Duration(
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_DURATION_VALUE)),
                        result.getInt(result.getColumnIndex(MedsTable.COLUMN_DURATION_UNIT_ID))
                ),
                new StartDate(result.getInt(result.getColumnIndex(MedsTable.COLUMN_START_DATE_EPOCH_DAYS))),
                new TimeToTake(
                        result.getString(result.getColumnIndex(MedsTable.COLUMN_TIME_TO_TAKE_LIST)),
                        LocalTime.ofSecondOfDay(result.getInt(result.getColumnIndex(MedsTable.COLUMN_START_TIME_SECONDS))),
                        LocalTime.ofSecondOfDay(result.getInt(result.getColumnIndex(MedsTable.COLUMN_END_TIME_SECONDS)))
                )
        );
        result.close();
        return med;
    }

    public Cursor getMedsCursor() {
        return contentResolver.query(PillboxContentProvider.MEDS_URI, null, null, null, null);
    }

    public int getUntakenPillsForTodayQuantity() {
        LocalDate day = LocalDate.now();
        Cursor result = contentResolver.query(PillboxContentProvider.PILLBOX_EVENTS_URI, null,
                                              PillboxEventsTable.COLUMN_DATE + " = ?" + " AND "+PillboxEventsTable.COLUMN_STATUS+" = ?", new String[]{ day.toString(), PillboxEvent.STATUS_NONE+""}, null, null);

        if (result != null) {
            int count = result.getCount();
            result.close();
            return count;
        } else return 0;

    }
}

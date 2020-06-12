package com.cucumber007.pillbox.models;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.cucumber007.pillbox.database.PillboxContentProvider;
import com.cucumber007.pillbox.database.tables.TrainingsTable;
import com.cucumber007.pillbox.objects.gym.Training;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GymModel {

    private Context context;
    private final ContentResolver contentResolver;
    private Gson gson = new Gson();


    public GymModel(Context context, ContentResolver contentResolver) {
        this.context = context;
        this.contentResolver = contentResolver;
    }

    public boolean isTrainingDownloaded(int globalId) {
        Cursor result = contentResolver.query(PillboxContentProvider.TRAININGS_URI, null,
                                              TrainingsTable.COLUMN_GLOBAL_ID + " = "+globalId, null, null, null);
        int count = result.getCount();
        result.close();
        return count != 0;
    }

    public void saveTraining(Training training) {
        ContentValues values = new ContentValues();
        values.put(TrainingsTable.COLUMN_GLOBAL_ID, training.getId());
        values.put(TrainingsTable.COLUMN_JSON, gson.toJson(training));
        Uri uri = contentResolver.insert(PillboxContentProvider.TRAININGS_URI, values);

        Cursor all = contentResolver.query(PillboxContentProvider.TRAININGS_URI, null, null, null, null);
        //Log.d("cutag", "" + DatabaseUtils.dumpCursorToString(all));
        int a = 5;
    }

    public void removeTraining(int globalId) {
        contentResolver.delete(PillboxContentProvider.TRAININGS_URI, "global_id = " + globalId, null);
    }

    public Training getTrainingByGlobalId(int trainingGlobalId) {
        Cursor result = contentResolver.query(PillboxContentProvider.TRAININGS_URI, null, TrainingsTable.COLUMN_GLOBAL_ID + " = " + trainingGlobalId, null, null);
        result.moveToFirst();
        if(result.getCount() == 0) return null;
        Training training = gson.fromJson(result.getString(result.getColumnIndex(TrainingsTable.COLUMN_JSON)), Training.class);
        training.setIsDownloaded(true);
        result.close();
        return training;
    }

    public List<Integer> getDownloadedTrainingsIds() {
        List<Integer> trainingsIds = new ArrayList<>();
        Cursor result = contentResolver.query(
                PillboxContentProvider.TRAININGS_URI, null, null, null, null, null);

        if(result.getCount() == 0) return trainingsIds;
        if (result.moveToFirst()){
            do {
                trainingsIds.add(result.getInt(result.getColumnIndex(TrainingsTable.COLUMN_GLOBAL_ID)));
            } while(result.moveToNext());
        }
        result.close();
        return trainingsIds;
    }

    public List<Training> getDownloadedTrainings() {
        List<Training> trainings = new ArrayList<>();
        Cursor result = contentResolver.query(
                PillboxContentProvider.TRAININGS_URI, null,null, null, null, null);

        if(result.getCount() == 0) return trainings;
        if (result.moveToFirst()){
            do {
                Training training = gson.fromJson(result.getString(result.getColumnIndex(TrainingsTable.COLUMN_JSON)), Training.class);
                training.setIsDownloaded(true);
                trainings.add(training);
            } while(result.moveToNext());
        }
        result.close();
        return trainings;
    }

    public HashMap<Integer, Training> getDownloadedTrainingsHashMap() {
        HashMap<Integer, Training> trainings = new HashMap<>();
        Cursor result = contentResolver.query(
                PillboxContentProvider.TRAININGS_URI, null,null, null, null, null);

        if(result.getCount() == 0) return trainings;
        if (result.moveToFirst()){
            do {
                Training training = gson.fromJson(result.getString(result.getColumnIndex(TrainingsTable.COLUMN_JSON)), Training.class);
                training.setIsDownloaded(true);
                trainings.put(training.getId(), training);
            } while(result.moveToNext());
        }
        result.close();
        return trainings;
    }

    public void clearTrainingsCache() {
        contentResolver.delete(PillboxContentProvider.TRAININGS_URI, null, null);
    }
}

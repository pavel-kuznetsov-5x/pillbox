package com.cucumber007.pillbox.utils;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.RecoverySystem;
import android.util.Log;

import com.cucumber007.pillbox.R;
import com.cucumber007.pillbox.activities.MainActivity;
import com.cucumber007.pillbox.activities.settings.AbstractSettingActivity;
import com.cucumber007.pillbox.activities.settings.NotificationSettingActivity;
import com.cucumber007.pillbox.models.ModelManager;
import com.cucumber007.pillbox.objects.pills.PillboxEvent;
import com.cucumber007.pillbox.objects.water.WaterNotification;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.List;

public class PillboxNotificationManager extends BroadcastReceiver {
    //Class that manages Android Notifications for different events

    //время выдачи в 11.00, 13.00, 15.00, 17.00, 19.00.
    int START_HOURS = 11;
    int QUANTITY = 5;
    int DELAY_HOURS = 2;
    int START_MINUTES = 0;
    int DELAY_DAY_MS = 24*60*60*1000;
//    int START_HOURS = LocalDateTime.now().getHour();
//    int START_MINUTES = LocalDateTime.now().getMinute();
//    int DELAY_HOURS = 2;
//    int QUANTITY = 1;
//    int DELAY_DAY_MS = 60*1000;
//    int DELAY_DAY_MS = 10*1000;

    int REQUEST_CODE_REMINDER = 0;
    int REQUEST_CODE_WATER = 1;

    private static PillboxNotificationManager instance;
    private AlarmManager alarmManager;;
    private Context context;

    public PillboxNotificationManager() {}

    public PillboxNotificationManager(Context context) {
        this.context = context;
        instance = this;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public static PillboxNotificationManager getInstance(Context context) {
        if (instance == null) {
            instance = new PillboxNotificationManager(context);
        }
        return instance;
    }

    public void createAlarmsForWater() {
        Intent intent = new Intent(context, PillboxNotificationManager.class);
        intent.putExtra("type", "water");
        LocalDate dateNow = LocalDate.from(LocalDateTime.now());

        for (int i = 0; i < QUANTITY; i++) {

                if(!isBroadcastExist(i, intent)) {
                    //Log.d("cutag", ""+"water created");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    ZonedDateTime setTime = ZonedDateTime.of(dateNow, LocalTime.of(START_HOURS, START_MINUTES).plus(i * DELAY_HOURS, ChronoUnit.HOURS), ZoneId.systemDefault());
                    //todo interval_day?
                    ZonedDateTime zoneNow = ZonedDateTime.now(ZoneId.systemDefault()).minus(1, ChronoUnit.MINUTES);
                    if (setTime.isAfter(zoneNow))
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setTime.toEpochSecond() * 1000, DELAY_DAY_MS, pendingIntent);
                } //else Log.d("cutag", ""+"falsestart");
        }

    }

    public void deleteAlarmsForWater() {
        Intent intent = new Intent(context, PillboxNotificationManager.class);
        intent.putExtra("type", "water");

        for (int i = 0; i < QUANTITY; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    public void createAlarmsForEvents(List<PillboxEvent> events) {
        createAlarmsForEvents(events, new RecoverySystem.ProgressListener() {
            @Override
            public void onProgress(int progress) {

            }
        });
    }

    public void createAlarmsForEvents(List<PillboxEvent> events, RecoverySystem.ProgressListener listener) {
        for (int i = 0; i < events.size(); i++) {
            PillboxEvent event = events.get(i);
            setAlarmForEvent(event);
            listener.onProgress((int) ((float) i / events.size()));
        }
    }

    public void setAlarmForEvent(PillboxEvent event) {
        if (!LocalDateTime.of(event.getDate(), event.getTime()).isBefore(LocalDateTime.now())) {
            Intent intent = new Intent(context, PillboxNotificationManager.class);
            int id = event.getId();
            intent.putExtra("id", id);
            /*if (event.getMedName() == null || event.getMedName().equals(""))
                Log.d("cutag", "GOT IT");*/
            intent.putExtra("med_name", event.getMedName());
            intent.putExtra("med_time", event.getTime().toString());
            intent.putExtra("type", "pills");

            if(!isBroadcastExist(id, intent)) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                ZonedDateTime setTime = ZonedDateTime.of(event.getDate(), event.getTime(), ZoneId.systemDefault());
                alarmManager.set(AlarmManager.RTC_WAKEUP, setTime.toEpochSecond() * 1000, pendingIntent);
            }
        }
    }

    public void deleteAlarmsForEvents(List<PillboxEvent> events) {
        deleteAlarmsForEvents(events, new RecoverySystem.ProgressListener() {
            @Override
            public void onProgress(int progress) {

            }
        });
    }

    public void deleteAlarmsForEvents(List<PillboxEvent> events, RecoverySystem.ProgressListener listener) {
        for (int i = 0; i < events.size(); i++) {
            PillboxEvent event = events.get(i);
            deleteAlarmForEvent(event);
            listener.onProgress((int)((float) i / events.size()*100));
        }
    }

    public void deleteAlarmForEvent(PillboxEvent event) {
        Intent intent = new Intent(context, PillboxNotificationManager.class);
        int id = event.getId();
        intent.putExtra("id", id);
        intent.putExtra("med_name", event.getMedName());
        intent.putExtra("med_time", event.getTime().toString());
        intent.putExtra("type", "pills");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getStringExtra("type")) {
            case "pills":
                sendPillboxEventNotification(context, intent);
                break;
            case "water":
                sendWaterEventNotification(context, intent);
                break;
        }

    }

    private int buildEventUniqueId(PillboxEvent event) {
        long pre = LocalDateTime.of(event.getDate(), event.getTime()).toEpochSecond(ZoneOffset.MIN);
        return (int)(pre - ((pre / 1000000) * 1000000));
    }

    private void sendPillboxEventNotification(Context context, Intent receivedIntent) {
        int id = receivedIntent.getIntExtra("id", -1);
        if (id == -1) throw new IllegalArgumentException("Id can't be < 0");
        String medName = receivedIntent.getStringExtra("med_name");
        String medTime = receivedIntent.getStringExtra("med_time");

        Log.d("cutag", medName + " " + medTime + " " + id);

        Notification.Builder notificationBuilder =
                new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ht_logo)
                        .setContentTitle(medName)
                        .setContentText(medTime)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("fragment", "reminder");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        REQUEST_CODE_REMINDER,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id + 1, notificationBuilder.build());
    }

    private void sendWaterEventNotification(Context context, Intent intent) {
        String text = WaterNotification.getRandomOne();

        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentTitle("Drink water!")
                .setContentText(text)
                .setSmallIcon(R.drawable.ht_logo)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        notificationBuilder.setStyle(new Notification.BigTextStyle(notificationBuilder)
                        .bigText(text)
                        .setBigContentTitle("Drink water!")
        );

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("fragment", "water");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(
                        REQUEST_CODE_WATER,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, notificationBuilder.build());
    }

    public void rescheduleAlarms() {
        //Log.d("cutag", "reboot");

        if(context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(NotificationSettingActivity.WATER_NOTIFICATIONS, true))
            createAlarmsForWater();

        if(context.getSharedPreferences(AbstractSettingActivity.SETTINGS_SHARED_PREFERENCES, Context.MODE_PRIVATE)
                .getBoolean(NotificationSettingActivity.PILLS_NOTIFICATIONS, true)) {
            createAlarmsForEvents(ModelManager.getInstance(context).getReminderModel().getEvents());
        }

    }

    public boolean isBroadcastExist(int id, Intent intent) {
        return  (PendingIntent.getBroadcast(context, id,
                intent, PendingIntent.FLAG_NO_CREATE) != null);
    }

}

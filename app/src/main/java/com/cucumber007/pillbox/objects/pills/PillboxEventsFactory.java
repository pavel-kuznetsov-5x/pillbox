package com.cucumber007.pillbox.objects.pills;


import android.content.Context;
import android.widget.Toast;

import com.cucumber007.pillbox.objects.pills.parameters.DailyUsage;
import com.cucumber007.pillbox.objects.pills.parameters.Duration;
import com.cucumber007.pillbox.objects.pills.parameters.Recurrence;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

public class PillboxEventsFactory {
    //Creates list of events based on med data

    //todo settings
    public static LocalTime DAY_START = LocalTime.MIN.plusHours(8);
    public static LocalTime DAY_END = LocalTime.MIN.plusHours(22);
    private static int EVENTS_LIMIT = 1500;

    private Med med;
    private Context context;

    public PillboxEventsFactory(Med med) {
        this.med = med;
    }

    public List<PillboxEvent> createEvents(Context context) {
        this.context = context;
        Recurrence recurrence = med.getRecurrence();
        Duration duration = med.getDuration();

        LocalDateTime timeStart = LocalDateTime.of(
                med.getStartDate().getDate(),
                LocalTime.now()
        );
        LocalDateTime timeStop;

        timeStop = timeStart;
        switch ((Duration.DurationUnit) duration.getUnit()) {
            case day:
                timeStop = timeStart.plusDays(duration.getValue()-1);
                break;
            case week:
                timeStop = timeStart.plusWeeks(duration.getValue());
                break;
            case month:
                timeStop = timeStart.plusMonths(duration.getValue());
                break;
            /*case dose:
                ///NO ACTION
                timeStop = timeStart;
                break;*/
            default: throw new IllegalStateException("Wrong unit");
        }

        return calculateEvents(timeStart, timeStop, recurrence);
    }

    private List<PillboxEvent> calculateEvents(LocalDateTime timeStart, LocalDateTime timeStop, Recurrence recurrence) {
        List<PillboxEvent> events = new ArrayList<>();
        LocalDate stopDate = LocalDate.from(timeStop);
        LocalTime stopTime = LocalTime.from(timeStop);

        LocalDate dayPointer = LocalDate.from(timeStart);

        int limit = EVENTS_LIMIT;
        while (!dayPointer.isAfter(stopDate) && limit>0) {

            for (int i = 0; i < med.getTimeToTake().getTimeList().size(); i++) {
                LocalTime time = med.getTimeToTake().getTimeList().get(i);
                if(limit < 0) {
                    //todo android string
                    Toast.makeText(context, "Events limit of 1500 exceeded. Course may be not full", Toast.LENGTH_SHORT).show();
                    break;
                } else limit--;
                if(!LocalDateTime.of(dayPointer, time).isBefore(LocalDateTime.now())) {
                    PillboxEvent event = new PillboxEvent(-1, med.getId(),
                            dayPointer, time, med.getDosage(), PillboxEvent.STATUS_NONE, med.getName(), med.getIcon(), med.getIconColor());
                    events.add(event);
                }
            }
            dayPointer = dayPointer.plus(recurrence.getValue(), ((Recurrence.RecurrenceUnit)recurrence.getUnit()).getChronoUnit());
        }
        return events;
    }


    private List<PillboxEvent> calculateEventsHourly(LocalDateTime timeStart, LocalDateTime timeStop, DailyUsage dailyUsage) {
        List<PillboxEvent> events = new ArrayList<>();
        LocalDateTime pointer = timeStart;

        int limit = EVENTS_LIMIT;
        while (pointer.isBefore(timeStop) && limit>0) {
            if(limit < 0) {
                //todo android string
                Toast.makeText(context, "Events limit of 1500 exceeded. Course may be not full", Toast.LENGTH_SHORT).show();
                break;
            } else limit--;
            if(!pointer.isBefore(LocalDateTime.now())) {
                PillboxEvent event = new PillboxEvent(-1, med.getId(),
                        LocalDate.from(pointer), LocalTime.from(pointer), med.getDosage(), PillboxEvent.STATUS_NONE, med.getName(), med.getIcon(), med.getIconColor());
                events.add(event);
            }
            pointer = pointer.plus(dailyUsage.getValue(), ChronoUnit.HOURS);
        }
        return events;
    }

}

package com.cucumber007.pillbox.objects.pills.parameters;


import com.cucumber007.pillbox.objects.MedParameterException;

import org.threeten.bp.LocalTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeToTake {

    private List<LocalTime> timeList = new ArrayList<>();
    private DailyUsage dailyUsage;
    private LocalTime startTime = LocalTime.of(8,0);
    private LocalTime endTime = LocalTime.of(22,0);


    public TimeToTake(DailyUsage dailyUsage) {
        this.dailyUsage = dailyUsage;
        timeList = createTimeList(dailyUsage, startTime, endTime);
    }

    public TimeToTake(DailyUsage dailyUsage, TimeToTake oldTimeToTake) {
        this.dailyUsage = dailyUsage;
        startTime = oldTimeToTake.getStartTime();
        endTime = oldTimeToTake.getEndTime();
        timeList = createTimeList(dailyUsage, startTime, endTime);
    }

   /* public TimeToTake(String list) {
        String[] result = list.split(",");

        for (int i = 0; i < result.length; i++) {
            timeList.add(LocalTime.ofSecondOfDay(Integer.parseInt(result[i])));
        }
        Collections.sort(timeList);
        dailyUsage = new DailyUsage(result.length, DailyUsage.DailyUsageUnit.daily);
        startTime = timeList.get(0);
        endTime = timeList.get(timeList.size() - 1);
    }*/

    public TimeToTake(String list, LocalTime startTime, LocalTime endTime) {
        String[] result = list.split(",");

        for (int i = 0; i < result.length; i++) {
            timeList.add(LocalTime.ofSecondOfDay(Integer.parseInt(result[i])));
        }
        Collections.sort(timeList);
        dailyUsage = new DailyUsage(result.length, DailyUsage.DailyUsageUnit.daily);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private List<LocalTime> createTimeList(DailyUsage dailyUsage, LocalTime timeStart, LocalTime timeStop) {
        //float delta = (float)(timeStop.getHour() - timeStart.getHour()) / dailyUsage.getValue();
        float delta = (float)(timeStop.getHour()*60+timeStop.getMinute() - timeStart.getHour()*60+timeStart.getMinute() - 1) / dailyUsage.getValue() / 60;
        return createTimeList(dailyUsage, timeStart, delta);
    }

    private List<LocalTime>
    createTimeList(DailyUsage dailyUsage, LocalTime timeStart, LocalTime timeStop, Period period)
    throws MedParameterException {
        if(( dailyUsage.getValue()*period.getValue()*3600 + timeStart.toSecondOfDay() ) > timeStop.toSecondOfDay())
            throw new MedParameterException("Selected period with this daily usage is out of day bounds");
        if(period.getValue() == 0) throw new MedParameterException("Zero period is not allowed");
        return createTimeList(dailyUsage, timeStart, period.getValue());
    }

    private List<LocalTime> createTimeList(DailyUsage dailyUsage, LocalTime timeStart, float delta) {
        //if(dailyUsage.getUnit().equals(DailyUsage.DailyUsageUnit.h24)) timeStart = LocalTime.MIN;
        List<LocalTime> list = new ArrayList<>();

        LocalTime dayPointer = LocalTime.from(timeStart);
        for (int i = 0; i < dailyUsage.getValue(); i++) {
            list.add(dayPointer);
            LocalTime dayPointerNext = dayPointer.plus(Math.round(delta*60), ChronoUnit.MINUTES);
            if(dayPointerNext.isBefore(dayPointer)) break;
            dayPointer = dayPointerNext;
        }

        return list;
    }

    public void setPeriod(Period period) throws MedParameterException {
        timeList = createTimeList(dailyUsage, startTime, endTime, period);
    }

    public void changeElement(int index, LocalTime localTime) {
        timeList.set(index, localTime);
        Collections.sort(timeList);
    }

    public String getSecondsStringOutput() {
        String res = "";

        for (int i = 0; i < timeList.size(); i++) {
            LocalTime time = timeList.get(i);

            int seconds = time.toSecondOfDay();
            res += i == timeList.size()-1 ? seconds+"" : seconds+",";
        }

        return res;

    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        timeList = createTimeList(dailyUsage, startTime, endTime);
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
        timeList = createTimeList(dailyUsage, startTime, endTime);
    }

    ////////////////////////////////////////////////


    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public List<LocalTime> getTimeList() {
        return timeList;
    }

    public DailyUsage getDailyUsage() {
        return dailyUsage;
    }
}

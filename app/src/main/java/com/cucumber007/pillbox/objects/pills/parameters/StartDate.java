package com.cucumber007.pillbox.objects.pills.parameters;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class StartDate extends AbstractMedParameter {

    public enum StartDateTimeUnit implements Unit {
        dummy;

        int minValue = 1;
        int maxValue = 23;

        @Override
        public int getMaxValue() {
            return maxValue;
        }

        @Override
        public int getMinValue() {
            return minValue;
        }

        @Override
        public int getId() {
            return 0;
        }

        @Override
        public String generateTag(int value, Unit unit) {
            return LocalDate.ofEpochDay(value)
                    .format(DateTimeFormatter.ofPattern("MM-dd-yy"));
        }

        @Override
        public String[] getValues() {
            return new String[0];
        }
    }

    public StartDate(LocalDate date) {
        super((int) date.toEpochDay(), StartDateTimeUnit.dummy);
    }

    public StartDate(long datestamp) {
        //int = epoch day
        super((int)datestamp, StartDateTimeUnit.dummy);
    }

    public StartDate(int datestamp) {
        //int = epoch day
        super(datestamp, StartDateTimeUnit.dummy);
    }

    public LocalDate getDate() {
        return LocalDate.ofEpochDay(getValue());
    }

    @Override
    public String[] getValues() {
        return new String[0];
    }

    @Override
    public Unit[] getUnitValues() {
        return StartDateTimeUnit.values();
    }
}

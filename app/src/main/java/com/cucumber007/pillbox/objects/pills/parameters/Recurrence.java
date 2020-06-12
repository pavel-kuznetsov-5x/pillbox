package com.cucumber007.pillbox.objects.pills.parameters;


import org.threeten.bp.temporal.ChronoUnit;

public class Recurrence extends AbstractMedParameter {

    public enum RecurrenceUnit implements Unit {
        day(0, 30, ChronoUnit.DAYS),
        week(1 , 4, ChronoUnit.WEEKS),
        month(2, 2, ChronoUnit.MONTHS);

        final int id;
        final ChronoUnit chrono;

        int minValue = 1;
        int maxValue = 23;

        RecurrenceUnit(int id, int maxValue, ChronoUnit chrono) {
            this.id = id;
            this.maxValue = maxValue;
            this.chrono = chrono;
        }

        @Override
        public int getMaxValue() {
            return maxValue;
        }

        @Override
        public int getMinValue() {
            return minValue;
        }

        RecurrenceUnit(int id, ChronoUnit chrono) {
            this.id = id;
            this.chrono = chrono;
        }

        @Override
        public String generateTag(int value, Unit unit) {
            String stringValue;
            if (value == 0) stringValue = "...";
            else stringValue = "" + value;

            switch (unit.getId()) {
                case 0:
                    if (value == 0) return "days";
                    if (value == 1) return "Every " + unit;
                    else return "Every " + stringValue + " " + unit + "s";
                case 1:
                    if (value == 0) return "weeks";
                    if (value == 1) return "Every " + unit;
                    else return "Every " + stringValue + " " + unit + "s";
                case 2:
                    if (value == 0) return "months";
                    if (value == 1) return "Every " + unit;
                    return "Every " + stringValue + " " + "months";
                default:
                    throw new IllegalArgumentException("No such unit");
            }
        }

        public int getId() { return id;}

        public String[] getValues() {
            String[] res = new String[values().length];

            for (int i = 0; i < values().length; i++) {
                res[i] = values()[i].toString();
            }

            return res;
        }

        public ChronoUnit getChronoUnit() {
            return chrono;
        }
    }


    public Recurrence(int value, Unit unit) {
        super(value, unit);
    }

    public Recurrence(int value, int unit_id) {
        super(value, RecurrenceUnit.values()[unit_id]);
    }

    @Override
    public Unit[] getUnitValues() {
        return RecurrenceUnit.values();
    }

}

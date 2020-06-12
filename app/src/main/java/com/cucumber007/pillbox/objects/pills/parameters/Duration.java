package com.cucumber007.pillbox.objects.pills.parameters;


public class Duration extends AbstractMedParameter {

    public enum DurationUnit implements Unit {
        day(0, 30),
        week(1, 3),
        month(2, 3);
        //dose(3);

        final int id;

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

        DurationUnit(int id) { this.id = id;}

        DurationUnit(int id, int maxValue) {
            this.id = id;
            this.maxValue = maxValue;
        }

        @Override
        public String generateTag(int value, Unit unit) {
            String stringValue;
            if (value == 0) stringValue = "...";
            else stringValue = "" + value;

            switch (unit.getId()) {
                case 0:
                    if (value == 1) return "1 " + unit;
                    else return "" + stringValue + " " + unit + "s";
                case 1:
                    if (value == 1) return "1 " + unit;
                    else return "" + stringValue + " " + unit + "s";
                case 2:
                    return "" + stringValue + " " + unit;
                case 3:
                    if (value == 1) return "1 " + unit;
                    else return "" + stringValue + " " + unit + "s";
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

    }

    public Duration(int value, Unit unit) {
        super(value, unit);
    }

    public Duration(int value, int unitId) {
        super(value, DurationUnit.values()[unitId]);
    }

    @Override
    public Unit[] getUnitValues() {
        return DurationUnit.values();
    }

}

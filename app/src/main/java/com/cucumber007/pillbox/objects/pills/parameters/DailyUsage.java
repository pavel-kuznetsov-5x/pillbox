package com.cucumber007.pillbox.objects.pills.parameters;

public class DailyUsage extends AbstractMedParameter {

    public enum DailyUsageUnit implements Unit {
        daily(0, 30);//,
        //h24(1);
        //todo refactor

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

        DailyUsageUnit(int id) { this.id = id;}

        DailyUsageUnit(int id, int maxValue) {
            this.id = id;
            this.maxValue = maxValue;
        }

        @Override
        public String generateTag(int value, Unit unit) {
            //todo to android strings
            String stringValue;
            if (value == 0) stringValue = "...";
            else stringValue = "" + value;

            switch (unit.getId()) {
                case 0:
                    if(value == 0) return "times a day";
                    if(value == 1) return "Once a day";
                    else return ""+stringValue + " times a day";
                /*case 1:
                    if(value == 1) return "once a 24 h";
                    else return ""+stringValue + " times in 24 h";*/
                default:
                    throw new IllegalArgumentException("No such unit");
            }
        }

        public int getId() { return id;}

        public String[] getValues() {
            String[] res = new String[values().length];

            for (int i = 0; i < values().length; i++) {
                res[i] = values()[i].generateTag(0, values()[i]);
            }

            return res;
        }
    }

    public DailyUsage(int value, Unit unit) {
        super(value, unit);
    }

    public DailyUsage(int value, int unitId) {
        super(value, DailyUsageUnit.values()[unitId]);
    }

    @Override
    public Unit[] getUnitValues() {
        return DailyUsageUnit.values();
    }
}

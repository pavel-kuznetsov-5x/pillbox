package com.cucumber007.pillbox.objects.pills.parameters;

public class Period extends AbstractMedParameter {

    public enum PeriodUnit implements Unit {
        hour(12);

        int minValue = 1;
        int maxValue = 23;

        PeriodUnit(int maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public int getMaxValue() {
            return maxValue;
        }

        @Override
        public int getMinValue() {
            return minValue;
        }

         @Override
        public String generateTag(int value, Unit unit) {
            //todo to android strings
            String stringValue;
            if (value == 0) stringValue = "";
            else stringValue = "" + value;

            switch (unit.getId()) {
                case 0:
                    if(value == 1) return ""+stringValue+" hour";
                    else return ""+stringValue + " hours";
                default:
                    throw new IllegalArgumentException("No such unit");
            }
        }

        public int getId() { return 0;}

        public String[] getValues() {
            //todo from generate tag?
            String[] res = new String[values().length];

            for (int i = 0; i < values().length; i++) {
                res[i] = values()[i].toString();
            }

            return res;
        }
    }

    public Period(int value) {
        super(value, PeriodUnit.hour);

    }

    @Override
    public Unit[] getUnitValues() {
        return PeriodUnit.values();
    }
}

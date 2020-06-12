package com.cucumber007.pillbox.objects.pills.parameters;

public class Dosage extends AbstractMedParameter {

    public enum DosageUnit implements Unit {
        tablet(0, 10),
        ml(1, 200),
        mg(2, 500);

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

        DosageUnit(int id, int maxValue) {
            this.id = id;
            this.maxValue = maxValue;
        }

        DosageUnit(int id) { this.id = id;}

        @Override
        public String generateTag(int value, Unit unit) {
            String stringValue;
            if (value == 0) stringValue = "...";
            else stringValue = "" + value;

            switch (unit.getId()) {
                case 0:
                    if (value == 0) return "tablets";
                    if (value == 1) return "1 " + unit;
                    else return "" + stringValue + " " + unit + "s";
                case 1:
                    if (value == 0) return "unit";
                    return "" + stringValue + " " + unit;
                case 2:
                    if (value == 0) return "unit";
                    return "" + stringValue + " " + unit;
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

    public Dosage(int value, DosageUnit unit) {
        super(value, unit);
    }

    public Dosage(int value, int unit_id) {
        super(value, DosageUnit.values()[unit_id]);
        DosageUnit[] units = DosageUnit.values();
    }

    @Override
    public Unit[] getUnitValues() {
        return DosageUnit.values();
    }
}

package com.cucumber007.pillbox.objects.pills.parameters;


public abstract class AbstractMedParameter {
    //Base for parameters of each meds
    //Every parameter has it's own unit (like hours, tablets, liters)

    public interface Unit {
        int getId();
        String generateTag(int value, Unit unit);
        String[] getValues();
        int getMinValue();
        int getMaxValue();
    }

    private int value;
    private Unit unit;

    public AbstractMedParameter() {
    }

    public AbstractMedParameter(int value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public String generateTag(int value, Unit unit) {
        return unit.generateTag(value, unit);
    }

    public String getTag() {
        return unit.generateTag(value, unit);
    }

    public String[] getValues() {
        return getUnit().getValues();
    }

    public abstract Unit[] getUnitValues();

    /////////////////////////////

    public int getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}

package org.meeuw.math;

/**
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class MeasurementImpl extends Measurement {

    private final double value;
    private final double uncertainty;

    public MeasurementImpl(double value, double uncertainty) {
        this(value, uncertainty, null);
    }

    public MeasurementImpl(double value, double uncertainty, Units units) {
        this.value = value;
        this.uncertainty = uncertainty;
        this.units = units;
    }

    @Override
    public double getUncertainty() {
        return uncertainty;

    }

    @Override
    public int intValue() {
        return (int) Math.round(value);

    }

    @Override
    public long longValue() {
        return Math.round(value);

    }

    @Override
    public float floatValue() {
        return (float) value;

    }

    @Override
    public double doubleValue() {
        return value;

    }

}

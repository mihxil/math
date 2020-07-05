package org.meeuw.math;

/**
 * A number with a uncertainty where the uncertainty is simply explicitely stated.
 *
 * This is e.g. the single result of a measurement.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Measurement extends UncertainNumber {

    private final double value;
    private final double uncertainty;

    public Measurement(double value, double uncertainty) {
        this(value, uncertainty, null);
    }

    public Measurement(double value, double uncertainty, Units units) {
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

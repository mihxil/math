package org.meeuw.math;

import org.meeuw.math.physics.*;

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
        super(units);
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public double getUncertainty() {
        return uncertainty;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public UncertainNumber plus(double value) {
        return new Measurement(this.value + value, uncertainty);
    }


}

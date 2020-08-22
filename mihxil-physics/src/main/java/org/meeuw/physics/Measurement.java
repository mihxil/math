package org.meeuw.physics;

import org.meeuw.math.uncertainnumbers.ImmutableUncertainDouble;
import org.meeuw.math.uncertainnumbers.UncertainDouble;

/**
 * A number with a uncertainty where the uncertainty is simply explicitely stated.
 *
 * This is e.g. the single result of a measurement.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Measurement extends PhysicalNumber {

    public Measurement(double value, double uncertainty, Units units) {
        this(new ImmutableUncertainDouble(value, uncertainty), units);
    }

    public Measurement(UncertainDouble wrapped, Units units) {
        super(wrapped, units);
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }


    @Override
    protected Measurement copy(UncertainDouble wrapped, Units units) {
        return new Measurement(wrapped, units);
    }



}

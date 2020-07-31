package org.meeuw.physics;

import org.meeuw.math.ImmutableUncertainNumber;
import org.meeuw.math.UncertainNumbers;

/**
 * A number with a uncertainty where the uncertainty is simply explicitely stated.
 *
 * This is e.g. the single result of a measurement.
 *
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public class Measurement extends PhysicalNumber<Measurement> {

    public Measurement(double value, double uncertainty) {
        this(value, uncertainty, null);
    }

    public Measurement(double value, double uncertainty, Units units) {
        this(new ImmutableUncertainNumber(value, uncertainty), units);
    }

    public Measurement(ImmutableUncertainNumber wrapped, Units units) {
        super(wrapped, units);
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public UncertainNumbers<Measurement> structure() {
        return new UncertainNumbers<>(new Measurement(0, 0, units), new Measurement(1, 0, units));
    }

    @Override
    protected Measurement copy(ImmutableUncertainNumber wrapped, Units units) {
        return new Measurement(wrapped, units);

    }


}

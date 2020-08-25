package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Predicate;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Getter
public
class DoubleConfidenceInterval implements Predicate<Double> {
    private final double low;
    private final double high;

    public static DoubleConfidenceInterval of(double value, double uncertainty, double interval) {
        double halfRange = Double.isNaN(uncertainty) ? Math.abs(value * UncertainDouble.NaN_EPSILON) : uncertainty * interval;
        return new DoubleConfidenceInterval(value - halfRange, value + halfRange);
    }

    public DoubleConfidenceInterval(double low, double high) {
        this.low = low;
        this.high = high;
    }

    public boolean contains(double value) {
        return this.low <= value && value <= this.high;
    }

    @Override
    public boolean test(Double value) {
        return value != null && contains(value);
    }

    @Override
    public String toString() {
        return "[" + low + "," + high + "]";
    }
}

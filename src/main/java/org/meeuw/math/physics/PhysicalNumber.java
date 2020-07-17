package org.meeuw.math.physics;

import lombok.Getter;

import org.meeuw.math.statistics.UncertainNumber;
import org.meeuw.math.statistics.ImmutableUncertainNumber;

/**
 * An uncertain number but also with {@link Units}
 *  *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber<T extends PhysicalNumber<T>> extends Number implements UncertainNumber<T> {

    @Getter
    protected final ImmutableUncertainNumber wrapped;

    @Getter
    protected final Units units;

    public PhysicalNumber(ImmutableUncertainNumber wrapped, Units units) {
        this.units = units;
        this.wrapped = wrapped;
    }

     /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return  wrapped.toString() + (units == null ? "" : " " + units.toString());
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return Math.round(doubleValue());
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public double doubleValue() {
        return  wrapped.doubleValue();
    }
    @Override
    public double getUncertainty() {
        return  wrapped.getUncertainty();
    }


    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public T combined(T m) {
        return copy(wrapped.combined(m.wrapped), units);
    }

    @Override
    public T times(double multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public T times(UncertainNumber<?> multiplier) {
        Units newUnits;
        if (multiplier instanceof PhysicalNumber) {
             newUnits = Units.forMultiplication(units, ((PhysicalNumber<?>) multiplier).getUnits());
        } else {
            newUnits = units;
        }
        return copy(wrapped.times(multiplier), newUnits);
    }

    @Override
    public T dividedBy(UncertainNumber<?> divisor) {
        return times(divisor.inverse());
    }

    @Override
    public T pow(int exponent) {
        return copy(wrapped.pow(exponent), Units.forExponentiation(units, exponent));

    }

    @Override
    public T plus(UncertainNumber<?> summand) {
        Units newUnits;
        if (summand instanceof PhysicalNumber) {
             newUnits = Units.forAddition(units, ((PhysicalNumber<?>) summand).getUnits());
        } else {
            newUnits = units;
        }
        return copy(wrapped.plus(summand), newUnits);
    }

    @Override
    public T minus(UncertainNumber<?> subtrahend) {
        return plus(subtrahend.negate());
    }

    @Override
    public T plus(double summand) {
        return copy(wrapped.plus(summand), units);
    }

    protected abstract T copy(ImmutableUncertainNumber wrapped, Units units);
}


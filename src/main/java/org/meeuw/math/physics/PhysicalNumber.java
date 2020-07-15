package org.meeuw.math.physics;

import lombok.Getter;

import org.meeuw.math.*;

/**
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber extends Number  implements Comparable<Number>, Field<PhysicalNumber> {

    /**
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     *
     * This is used in {@link #toString()}
     */
    @Getter
    protected int minimumExponent = 4;

    @Getter
    protected final Units units;

    public PhysicalNumber(Units units) {
        this.units = units;
    }

    @Override
    public long longValue() {
        return Math.round(doubleValue());
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public float floatValue() {
        return (float) doubleValue();
    }

    @Override
    public byte byteValue() {
        return (byte) longValue();
    }
    @Override
    public short shortValue() {
        return (short) longValue();
    }

     /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return  Utils.scientificNotation(doubleValue(), minimumExponent) + (units == null ? "" : " " + units.toString());
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    public abstract double getUncertainty();

    /**
     * Creates a new {@link PhysicalNumber} representing the negated value of this one.
     */
    @Override
    public PhysicalNumber negate() {
        return times(-1d);
    }
    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    public abstract PhysicalNumber times(double multiplication);

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    public PhysicalNumber div(double divisor) {
        return times(1d/divisor);
    }

}


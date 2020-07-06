package org.meeuw.math;

import lombok.Getter;

import java.util.Objects;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

/**
 * A number with an uncertainty {@link #getUncertainty()}, and (optionally) {@link #getUnits()}
 *
 * http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf
 * @author Michiel Meeuwissen
 * @since 0.3
 */
public abstract class UncertainNumber extends Number implements Comparable<Number> {

    public static final BinaryOperator<UncertainNumber> TIMES = UncertainNumber::times;
    public static final UnaryOperator<UncertainNumber> UMINUS = UncertainNumber::negate;
    public static final BinaryOperator<UncertainNumber> PLUS   = UncertainNumber::plus;
    public static final BinaryOperator<UncertainNumber> MINUS = (a1, a2) -> UMINUS.apply(a2).plus(a1);




    @Getter
    protected int minimumExponent = 4;

    @Getter
    protected final Units units;

    protected UncertainNumber(Units units) {
        this.units = units;
    }

    public abstract double getUncertainty();

    public UncertainNumber measurementCopy() {
        return new Measurement(doubleValue(), getUncertainty(), units);
    }

    public UncertainNumber combined(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u= getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new Measurement(value, uncertaintity, units);

    }

    public UncertainNumber plus(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() + m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }

    public UncertainNumber minus(UncertainNumber m) {
        if (! Objects.equals(units, m.units)) {
            throw new IllegalArgumentException();
        }
        double u = getUncertainty();
        double mu = m.getUncertainty();
        return new Measurement(
            doubleValue() - m.doubleValue(),
            Math.sqrt(u * u + mu * mu),
            units
        );
    }
    public UncertainNumber times(UncertainNumber m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.times(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() * m.doubleValue();
        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            //Math.abs(newValue) * (u + mu),
            newUnits
        );
    }

    public UncertainNumber div(UncertainNumber m) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.dividedBy(m.units);
        }
        double u = getUncertainty() / doubleValue();
        double mu = m.getUncertainty() / m.doubleValue();
        double newValue = doubleValue() / m.doubleValue();

        return new Measurement(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu)),
            newUnits
        );
    }
    public UncertainNumber pow(int d) {
        Units newUnits = null;
        if (units != null) {
            newUnits = units.pow(d);
        }
        return new Measurement(
            Math.pow(doubleValue(), d),
            Math.abs(d) * Math.pow(doubleValue(), d -1) * getUncertainty(),
            newUnits);
    }

    /**
     * Creates a new {@link UncertainNumber} representing the negated value of this one.
     */
    public UncertainNumber negate() {
        return times(-1d);
    }

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    public UncertainNumber times(double multiplication) {
        return new Measurement(multiplication * doubleValue(),
            Math.abs(multiplication) * getUncertainty(), units);
    }

    /**
     * Creates a new {@link UncertainNumber} representing a multiple of this one.
     */
    public UncertainNumber div(double divisor) {
        return times(1d/divisor);
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
     * The minimum exponent defined how close a number must be to 1, to not use scientific notation
     * for it. Defaults to 4, which means that numbers between 0.0001 and 10000 (and -0.0001 and
     * -10000) are presented without useage of scientific notation
     *
     * This is used in {@link #toString()}
     */
    public void setMinimumExponent(int m) {
        minimumExponent = m;
    }


    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return  Utils.scientificNotation(doubleValue(), getUncertainty(), minimumExponent) +
            (units == null ? "" : " " + units.toString());
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

}

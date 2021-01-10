package org.meeuw.physics;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

/**
 * An uncertain number but also with {@link Units}
 *
 * This makes it not a {@link org.meeuw.math.abstractalgebra.FieldElement}, but merely a {@link MultiplicativeGroupElement}, since you cannot add any physical number to another.
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber extends Number
    implements MultiplicativeGroupElement<PhysicalNumber>,
    UncertainDouble<PhysicalNumber>,
    Scalar<PhysicalNumber>,
    Comparable<PhysicalNumber>,
    SignedNumber {

    @Getter
    protected final UncertainReal wrapped;

    @Getter
    protected final Units units;

    public PhysicalNumber(@NonNull UncertainReal wrapped, @NonNull Units units) {
        this.units = units;
        this.wrapped = wrapped;
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return wrapped + " " + units;
    }

    @Override
    public int intValue() {
        return (int) longValue();
    }

    @Override
    public long longValue() {
        return Math.round(getValue());
    }

    @Override
    public float floatValue() {
        return (float) getValue();
    }

    @Override
    public double getValue() {
        return  wrapped.getValue();
    }

    @Override
    public BigDecimal bigDecimalValue() {
        return wrapped.bigDecimalValue();
    }

    @Override
    public double getUncertainty() {
        return  wrapped.getUncertainty();
    }

    @Override
    public PhysicalNumber combined(UncertainReal m) {
        return copy(wrapped.combined(m), units);
    }

    @Override
    public PhysicalNumber times(double multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public PhysicalNumber times(PhysicalNumber multiplier) {
        return copy(
            wrapped.times(multiplier.wrapped),
            Units.forMultiplication(units, multiplier.getUnits())
        );
    }

    public PhysicalNumber times(UncertainReal multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public PhysicalNumber dividedBy(PhysicalNumber  divisor) {
        return times(divisor.reciprocal());
    }

    @Override
    public PhysicalNumber pow(int exponent) {
        return copy(
            wrapped.pow(exponent),
            Units.forExponentiation(units, exponent));
    }

    /**
     * Return a new physical number which is the sum of this one and another one.
     *
     * Units will implicitely be converted, and the resulting value will have the units of this.
     *
     * @throws IllegalArgumentException If the summand has dimensions incompatible with the dimensions of this. (e.g. you cannot add meters to seconds).
     */
    @Override
    public PhysicalNumber plus(PhysicalNumber summand) {
        summand = summand.toUnits(this.getUnits());
        return copy(wrapped.plus(summand.wrapped), Units.forAddition(units, summand.getUnits())
        );
    }

    /**
     * Converts this to a new physical number but represented in the given units.
     *
     * @throws IllegalArgumentException if the target units are not compatible (have different dimensions)
     */
    public PhysicalNumber toUnits(Units target) {
        if (getUnits().equals(target)) {
            return this;
        }
        double factor = getUnits().conversionFactor(target);
        return copy(wrapped.times(factor), target);
    }

    public PhysicalNumber toUnits(Unit... units) {
        return toUnits(UnitsImpl.of(units));
    }

    /**
     * Just adds {@link #plus(PhysicalNumber)}{@link #negation()}.
     * @see #plus(PhysicalNumber)
     */
    public PhysicalNumber minus(PhysicalNumber subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    public PhysicalNumber plus(double summand) {
        return copy(wrapped.plus(summand), units);
    }

    protected abstract PhysicalNumber copy(UncertainReal wrapped, Units units);

    @Override
    public PhysicalNumbers getStructure() {
        return PhysicalNumbers.INSTANCE;
    }

    @Override
    public PhysicalNumber negation() {
        return times(-1);
    }

    @Override
    public int signum() {
        return wrapped.signum();
    }

    @Override
    public PhysicalNumber abs() {
        if (isPositive()) {
            return this;
        } else {
            return new Measurement(wrapped.abs(), getUnits());
        }
    }

    @Override
    public double doubleValue() {
        return wrapped.doubleValue();
    }

    @Override
    public PhysicalNumber of(double value, double uncertainty) {
        return new Measurement(value, uncertainty, units);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalNumber)) return false;
        PhysicalNumber of = (PhysicalNumber) o;
        return wrapped.equals(of.wrapped, 1) && units.equals(of.units);
    }

    @Override
    public int hashCode() {
        return  (units != null ? units.hashCode() : 0);
    }

    @Override
    public int compareTo(PhysicalNumber f) {
        return Double.compare(getValue(),f.getValue());
    }
}


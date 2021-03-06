package org.meeuw.physics;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;
import org.meeuw.math.numbers.Scalar;
import org.meeuw.math.numbers.SignedNumber;
import org.meeuw.math.text.spi.FormatService;
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
     * Units will implicitely be converted, and the resulting value will have the units of this.
     *
     * @throws DimensionsMismatchException If the summand has dimensions incompatible with the dimensions of this. (e.g. you cannot add meters to seconds).
     * @param summand the physical number to add to this one
     * @return  a new physical number which is the sum of this one and another one.
     */
    @Override
    public PhysicalNumber plus(PhysicalNumber summand) {
        summand = summand.toUnits(this.getUnits());
        return copy(wrapped.plus(summand.wrapped), Units.forAddition(units, summand.getUnits()));
    }

    @Override
    public PhysicalNumber reciprocal() {
        return pow(-1);
    }

    /**
     * Converts this to a new physical number but represented in the given units.
     *
     * @throws DimensionsMismatchException if the target units are not compatible (have different dimensions)
     * @param target the new units
     * @return a new physical number representing the same value as this one, only in different units
     */
    public PhysicalNumber toUnits(Units target) {
        if (getUnits().equals(target)) {
            return this;
        }
        UncertainReal factor = getUnits().conversionFactor(target);
        return copy(wrapped.times(factor), target);
    }

    public PhysicalNumber toUnits(Unit... units) {
        return toUnits(Units.of(units));
    }

    /**
     * Just adds {@link #plus(PhysicalNumber)}{@link #negation()}.
     * @see #plus(PhysicalNumber)
     * @param subtrahend to physical number to substract form this one
     * @return the current number minus the subtrahend
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
    public PhysicalNumber _of(double value, double uncertainty) {
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
        if (equals(f)) {
            return 0;
        }
        return Double.compare(getValue(), f.getValue());
    }

    /**
     * Represents the mean value in a scientific notation (using unicode characters).
     * The value of the standard deviation is used to determin how many digits can sensibly be shown.
     */
    @Override
    public String toString() {
        return FormatService.toString(this);
    }

}


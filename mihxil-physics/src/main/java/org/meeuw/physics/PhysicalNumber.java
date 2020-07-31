package org.meeuw.physics;

import lombok.Getter;
import org.meeuw.math.ImmutableUncertainNumber;
import org.meeuw.math.UncertainNumber;
import org.meeuw.math.abstractalgebra.AlgebraicNumber;

/**
 * An uncertain number but also with {@link Units}
 *  *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber
    extends AlgebraicNumber<PhysicalNumber>
    implements  UncertainNumber<PhysicalNumber> {

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

    public int compareTo(PhysicalNumber o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public PhysicalNumber combined(PhysicalNumber m) {
        return copy(wrapped.combined(m.wrapped), units);
    }

    @Override
    public PhysicalNumber times(double multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public PhysicalNumber times(PhysicalNumber multiplier) {
        return copy(wrapped.times(multiplier),  Units.forMultiplication(units, multiplier.getUnits()));
    }

    public PhysicalNumber times(UncertainNumber<?> multiplier) {
        return copy(wrapped.times(multiplier), units);
    }

    @Override
    public PhysicalNumber dividedBy(PhysicalNumber  divisor) {
        return times(divisor.reciprocal());
    }

    @Override
    public PhysicalNumber pow(int exponent) {
        return copy(wrapped.pow(exponent), Units.forExponentiation(units, exponent));

    }

    @Override
    public PhysicalNumber plus(PhysicalNumber summand) {
        return copy(wrapped.plus(summand), Units.forAddition(units, summand.getUnits()));
    }

    @Override
    public PhysicalNumber minus(PhysicalNumber subtrahend) {
        return plus(subtrahend.negation());
    }

    @Override
    public PhysicalNumber plus(double summand) {
        return copy(wrapped.plus(summand), units);
    }

    protected abstract PhysicalNumber copy(ImmutableUncertainNumber wrapped, Units units);

    @Override
    public PhysicalNumber self() {
        return  this;
    }

    @Override
    public PhysicalNumbers  structure() {
        return new PhysicalNumbers(
                new Measurement(0, 0, units),
                new Measurement(1, 0, Units.DIMENSIONLESS));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhysicalNumber that = (PhysicalNumber) o;

        if (wrapped != null ? !wrapped.equals(that.wrapped) : that.wrapped != null) {
            return false;
        }
        return units != null ? units.equals(that.units) : that.units == null;
    }

    @Override
    public int hashCode() {
        int result = wrapped != null ? wrapped.hashCode() : 0;
        result = 31 * result + (units != null ? units.hashCode() : 0);
        return result;
    }
}


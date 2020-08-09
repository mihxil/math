package org.meeuw.physics;

import lombok.Getter;
import org.meeuw.math.uncertainnumbers.ImmutableUncertainNumber;
import org.meeuw.math.uncertainnumbers.UncertainNumber;
import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * An uncertain number but also with {@link Units}
 *  *
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public abstract class PhysicalNumber
    extends Number
    implements MultiplicativeGroupElement<PhysicalNumber>, UncertainNumber<PhysicalNumber> {

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

    public PhysicalNumber plus(PhysicalNumber summand) {
        return copy(wrapped.plus(summand), Units.forAddition(units, summand.getUnits()));
    }

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
    public PhysicalNumbers getStructure() {
        return PhysicalNumbers.INSTANCE;
    }

    public PhysicalNumber negation() {
        return times(-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhysicalNumber)) return false;
        return wrapped.toString().equals(wrapped.toString()) && units.equals(((PhysicalNumber) o).units);
    }

    @Override
    public int hashCode() {
        int result = wrapped != null ? wrapped.hashCode() : 0;
        result = 31 * result + (units != null ? units.hashCode() : 0);
        return result;
    }
}


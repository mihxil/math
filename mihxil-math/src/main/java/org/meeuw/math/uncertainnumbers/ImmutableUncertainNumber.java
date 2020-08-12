package org.meeuw.math.uncertainnumbers;

import lombok.Getter;
import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber extends AbstractUncertainNumber<ImmutableUncertainNumber> implements FieldElement<ImmutableUncertainNumber> {

    public static final ImmutableUncertainNumber ZERO = new ImmutableUncertainNumber(0, 0);
    public static final ImmutableUncertainNumber ONE = new ImmutableUncertainNumber(1, 0);

    private final double value;
    @Getter
    private final double uncertainty;

    public ImmutableUncertainNumber(double value, double uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public UncertainNumbersField<ImmutableUncertainNumber> getStructure() {
        return new UncertainNumbersField<>(ONE, ZERO);
    }

    @Override
    public ImmutableUncertainNumber negation() {
        return times(-1);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public ImmutableUncertainNumber combined(ImmutableUncertainNumber m) {
        double u = getUncertainty();
        double mu = m.getUncertainty();
        double weight = 1d / (u * u);
        double mweight = 1d / (mu * mu);
        double value = (doubleValue() * weight + m.doubleValue() * mweight) / (weight + mweight);

        // I'm not absolutely sure about this:
        double uncertaintity = 1d/ Math.sqrt((weight + mweight));
        return new ImmutableUncertainNumber(value, uncertaintity);
    }

    @Override
    public ImmutableUncertainNumber times(double multiplier) {
        return new ImmutableUncertainNumber(multiplier * doubleValue(),
            Math.abs(multiplier) * getUncertainty());
    }

    public ImmutableUncertainNumber times(UncertainNumber<?> multiplier) {
        double u = getUncertainty() / doubleValue();
        double mu = multiplier.getUncertainty() / multiplier.doubleValue();
        double newValue = doubleValue() * multiplier.doubleValue();
        return new ImmutableUncertainNumber(
            newValue,
            Math.abs(newValue) * Math.sqrt( (u * u)  + (mu * mu))
        );
    }

    @Override
    public ImmutableUncertainNumber plus(double summand) {
        return new ImmutableUncertainNumber(summand + doubleValue(), getUncertainty());
    }

    @Override
    public ImmutableUncertainNumber pow(int exponent) {
        return new ImmutableUncertainNumber(Math.pow(doubleValue(), exponent),
            Math.abs(exponent) * Math.pow(doubleValue(), exponent -1) * getUncertainty());
    }

    public ImmutableUncertainNumber plus(UncertainNumber<?> summand) {
        double u = getUncertainty();
        double mu = summand.getUncertainty();
        return new ImmutableUncertainNumber(
            doubleValue() + summand.doubleValue(),
            Math.sqrt(u * u + mu * mu));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableUncertainNumber that = (ImmutableUncertainNumber) o;

        if (Double.compare(that.value, value) != 0) {
            return false;
        }
        return Double.compare(that.uncertainty, uncertainty) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(value);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(uncertainty);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public int compareTo(UncertainNumber<?> o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public ImmutableUncertainNumber plus(ImmutableUncertainNumber summand) {
        return plus((UncertainNumber<?>) summand);
    }

    @Override
    public ImmutableUncertainNumber times(ImmutableUncertainNumber multiplier) {
        return times((UncertainNumber<?>) multiplier);
    }
}

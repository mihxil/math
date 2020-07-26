package org.meeuw.math;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber extends AbstractUncertainNumber<ImmutableUncertainNumber> {

    private final double value;
    @Getter
    private final double uncertainty;

    public ImmutableUncertainNumber(double value, double uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
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

    @Override
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

    @Override
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
}

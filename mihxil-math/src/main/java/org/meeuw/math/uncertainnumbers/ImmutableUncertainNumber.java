package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber
    extends AbstractUncertainNumber<UncertainNumberElement>
    implements UncertainNumberElement {

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
    public UncertainNumbersField getStructure() {
        return UncertainNumbersField.INSTANCE;
    }

    @Override
    public ImmutableUncertainNumber negation() {
        return (ImmutableUncertainNumber) times(-1);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public ImmutableUncertainNumber times(UncertainNumberElement multiplier) {
        return (ImmutableUncertainNumber) super.times(multiplier);
    }

    @Override
    public ImmutableUncertainNumber plus(UncertainNumberElement summand) {
        return (ImmutableUncertainNumber) super.plus(summand);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return equals(o, 1);
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

    public int compareTo(UncertainNumber o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

}

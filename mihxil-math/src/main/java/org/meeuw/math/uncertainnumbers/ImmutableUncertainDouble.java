package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainDouble
    extends AbstractUncertainDouble<UncertainDoubleElement>
    implements UncertainDoubleElement {

    public static final ImmutableUncertainDouble ZERO = new ImmutableUncertainDouble(0, EXACT);
    public static final ImmutableUncertainDouble ONE = new ImmutableUncertainDouble(1, EXACT);

    private final double value;
    @Getter
    private final double uncertainty;

    public ImmutableUncertainDouble(double value, double uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
    }

    @Override
    public UncertainDoubleField getStructure() {
        return UncertainDoubleField.INSTANCE;
    }

    @Override
    public ImmutableUncertainDouble negation() {
        return (ImmutableUncertainDouble) times(-1);
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public ImmutableUncertainDouble times(UncertainDoubleElement multiplier) {
        return (ImmutableUncertainDouble) super.times(multiplier);
    }

    @Override
    public ImmutableUncertainDouble plus(UncertainDoubleElement summand) {
        return (ImmutableUncertainDouble) super.plus(summand);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        return equals(o, 1);
    }

    @Override
    public int hashCode() {
        // must return constant to ensure that this is consistent with equals
        return 0;
    }

    @Override
    public int compareTo(Number o) {
        return Double.compare(doubleValue(), o.doubleValue());
    }

}

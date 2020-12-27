package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import org.meeuw.math.numbers.NumberOperations;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber<N extends Number> implements UncertainNumber<N> {

    private final N value;

    @Getter
    private final N uncertainty;

    public ImmutableUncertainNumber(N value, N uncertainty) {
        this.value = value;
        this.uncertainty = uncertainty;
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
    public N getValue() {
        return value;
    }

    @Override
    public NumberOperations<N> operations() {
        return NumberOperations.of(value);
    }

}

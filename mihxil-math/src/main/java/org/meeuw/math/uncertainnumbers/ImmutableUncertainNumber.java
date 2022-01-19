package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber<N extends Number> implements UncertainNumber<N> {

    @Getter
    private final N value;

    @Getter
    private final N uncertainty;

    public static <N extends Number> ImmutableUncertainNumber<N> of(N value, N uncertainty) {
        return new ImmutableUncertainNumber<>(value, uncertainty);
    }

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
}

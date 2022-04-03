package org.meeuw.math.uncertainnumbers;

import lombok.Getter;

import java.util.function.Supplier;

import static org.meeuw.math.CollectionUtils.memoize;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public class ImmutableUncertainNumber<N extends Number> implements UncertainNumber<N> {

    @Getter
    private final N value;

    private final Supplier<N> uncertainty;

    public static <N extends Number> ImmutableUncertainNumber<N> of(N value, Supplier<N> uncertainty) {
        return new ImmutableUncertainNumber<>(value, uncertainty);
    }

    public ImmutableUncertainNumber(N value, N uncertainty) {
        this.value = value;
        this.uncertainty = () -> uncertainty;
    }

    public ImmutableUncertainNumber(N value, Supplier<N> uncertainty) {
        this.value = value;
        this.uncertainty = memoize(uncertainty);
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
    public N getUncertainty() {
        return this.uncertainty.get();
    }
}

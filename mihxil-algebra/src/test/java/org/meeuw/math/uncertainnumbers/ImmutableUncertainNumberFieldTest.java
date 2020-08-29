package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ImmutableUncertainNumberFieldTest implements FieldTheory<UncertainDoubleElement> {

    @Test
    public void testToString() {
        ImmutableUncertainDouble uncertainDouble = new ImmutableUncertainDouble(5, 1);
        assertThat(uncertainDouble.toString()).isEqualTo("5.0 ± 1.0");

    }

    @Override
    public Arbitrary<ImmutableUncertainDouble> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new ImmutableUncertainDouble(value, Math.abs(value * r.nextDouble()));
        });

    }
}

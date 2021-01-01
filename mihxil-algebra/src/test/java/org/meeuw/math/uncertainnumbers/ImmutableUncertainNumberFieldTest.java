package org.meeuw.math.uncertainnumbers;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.uncertainnumbers.field.UncertainDoubleElement;
import org.meeuw.math.uncertainnumbers.field.UncertainReal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class ImmutableUncertainNumberFieldTest implements FieldTheory<UncertainReal> {

    @Test
    public void testToString() {
        UncertainDoubleElement uncertainDouble = new UncertainDoubleElement(5, 1);
        assertThat(uncertainDouble.toString()).isEqualTo("5.0 ± 1.0");

    }

    @Override
    public Arbitrary<UncertainDoubleElement> elements() {
        return Arbitraries.randomValue(r -> {
            double value = 10000 * (r.nextDouble() - 0.5d);
            return new UncertainDoubleElement(value, Math.abs(value * r.nextDouble()));
        });
    }

    @Test
    public void pow() {
        UncertainDoubleElement w = new UncertainDoubleElement(-1971, 680);
        assertThat(w.pow(-2).getUncertainty()).isPositive();
    }
}

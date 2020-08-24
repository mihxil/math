package org.meeuw.math.abstractalgebra.reals;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.abstractalgebra.test.NumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RealNumberTest implements FieldTheory<RealNumber>, NumberTheory<RealNumber> {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));

        assertThat(of(0d).epsilon().value).isEqualTo(RealNumber.EPSILON_FACTOR * 2.220446049250313E-16);

    }

    @Provide
    public Arbitrary<RealNumber> elements() {
        return Arbitraries.randomValue((random) -> of(random.nextDouble())).injectDuplicates(0.1);
    }
}

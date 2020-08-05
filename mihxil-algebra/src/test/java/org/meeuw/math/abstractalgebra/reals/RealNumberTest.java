package org.meeuw.math.abstractalgebra.reals;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.FieldTheory;
import org.meeuw.math.abstractalgebra.NumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class RealNumberTest implements FieldTheory<RealNumber>, NumberTheory<RealNumber>
 {


    @Test
    public void test() {
        assertThat(of(5d).times(of(6d))).isEqualTo(of(30d));
    }

    @Provide
    public Arbitrary<RealNumber> elements() {
        return Arbitraries.randomValue((random) -> of(random.nextDouble()));
    }
}

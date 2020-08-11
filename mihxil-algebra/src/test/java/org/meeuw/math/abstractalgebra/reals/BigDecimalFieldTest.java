package org.meeuw.math.abstractalgebra.reals;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Provide;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.FieldTheory;
import org.meeuw.math.abstractalgebra.NumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class BigDecimalFieldTest implements FieldTheory<BigDecimalElement>, NumberTheory<BigDecimalElement>
 {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
    }

    @Provide
    public Arbitrary<BigDecimalElement> elements() {
        return Arbitraries.randomValue((random) -> of(random.nextDouble()));
    }
}

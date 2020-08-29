package org.meeuw.math.abstractalgebra.reals;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RealNumberTest implements FieldTheory<RealNumber>, SignedNumberTheory<RealNumber> {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
        assertThat(of(0d).epsilon().value).isEqualTo(RealNumber.EPSILON_FACTOR * 4.9E-324);

    }

    @Provide
    public Arbitrary<RealNumber> elements() {
        return Arbitraries.randomValue((random) -> of(2000 * random.nextDouble() - 100)).injectDuplicates(0.1);
    }
}

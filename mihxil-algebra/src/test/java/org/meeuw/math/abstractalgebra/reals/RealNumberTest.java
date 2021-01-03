package org.meeuw.math.abstractalgebra.reals;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RealNumberTest implements CompleteFieldTheory<RealNumber>, SignedNumberTheory<RealNumber> {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
        assertThat(of(0d).getConfidenceInterval().getLow()).isEqualTo(RealNumber.EPSILON_FACTOR * -4.9E-324);
        assertThat(of(0d).getConfidenceInterval().getHigh()).isEqualTo(RealNumber.EPSILON_FACTOR * 4.9E-324);
    }

    @Test
    public void minus() {
        assertThat(of(1).minus(of(0))).isEqualTo(of(1));
    }

    @Test
    public void fractionalUncertainty() {
        RealNumber ex = new RealNumber(2.36, 0.04);
        assertThat(ex.getFractionalUncertainty()).isEqualTo(0.016666666666666666);
        assertThat(ex.sqr().getFractionalUncertainty()).isEqualTo(0.03225806451612903);
        assertThat(ex.sqr().toString()).isEqualTo("5.57 ± 0.19");
    }

    @Test
    public void nearZero() {
        RealNumber zero = RealNumber.SMALLEST;
        RealNumber someNumber = new RealNumber(5, 0.1);

        RealNumber product = someNumber.times(zero);
        log.info("{} . {} = {}", someNumber, zero, product);
    }

    @Override
	@Provide
    public Arbitrary<RealNumber> elements() {
        return Arbitraries.randomValue((random) -> of(2000 * random.nextDouble() - 100)).injectDuplicates(0.1);
    }
}

package org.meeuw.math.abstractalgebra.reals;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.abstractalgebra.test.SignedNumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalField.INSTANCE;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
strictfp class BigDecimalFieldTest implements CompleteFieldTheory<BigDecimalElement>, SignedNumberTheory<BigDecimalElement> {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
    }

    @Test
    public void uncertainty() {
        assertThat(of(5).getUncertainty()).isEqualTo("1.8E-15");
        assertThat(of(50).getUncertainty()).isEqualTo("1.4E-14");
        assertThat(of(5e-4).getUncertainty()).isEqualTo("2.2E-19");

        assertThat(of(5e-4).times(5).getUncertainty()).isEqualTo("1.1E-18");

        assertThat(of("5").getUncertainty()).isEqualTo("0");
        assertThat(of(4_503_599_627_370_497d).getUncertainty()).isEqualTo("2.0");
        assertThat(of(4_503_599_627_370_497d).minus(of(4_503_599_627_370_496d)).getUncertainty()).isEqualTo("2.8");
    }

    @Test
    public void basic() {
        assertThat(of(5).minus(of(4))).isEqualTo(of(1));
        assertThat(of(5).plus(INSTANCE.zero())).isEqualTo(of(5));
        assertThat(of("-539.4562718339926").plus(INSTANCE.zero())).isEqualTo(of("-539.4562718339926"));
    }

    @Test
    public void reciprocalExample() {
        BigDecimalElement e = of(-859.3420301563415);
        BigDecimalElement reciprocal = e.reciprocal();
        BigDecimalElement timesItself = reciprocal.times(e);
        assertThat(timesItself.equals(e.getStructure().one())).isTrue();
    }

    @Override
	@Provide
    public Arbitrary<BigDecimalElement> elements() {
        return Arbitraries.randomValue((random) -> of(2000 * random.nextDouble() - 1000)).injectDuplicates(0.1);
    }
}

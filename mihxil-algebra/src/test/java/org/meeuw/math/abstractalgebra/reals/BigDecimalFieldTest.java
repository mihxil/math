package org.meeuw.math.abstractalgebra.reals;

import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.FieldTheory;
import org.meeuw.math.abstractalgebra.test.NumberTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.reals.BigDecimalElement.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
strictfp class BigDecimalFieldTest implements FieldTheory<BigDecimalElement>, NumberTheory<BigDecimalElement>  {

    @Test
    public void test() {
        assertThat(of(5d).times(2).times(of(6d))).isEqualTo(of(60d));
    }

    @Test
    public void uncertainty() {
        assertThat(of(5).getUncertainty()).isEqualTo("8.9E-16");
        assertThat(of(50).getUncertainty()).isEqualTo("7.2E-15");
        assertThat(of(5e-4).getUncertainty()).isEqualTo("1.1E-19");

        assertThat(of(5e-4).times(5).getUncertainty()).isEqualTo("5.5E-19");

        assertThat(of("5").getUncertainty()).isEqualTo("0");
        assertThat(of(4_503_599_627_370_497d).getUncertainty()).isEqualTo("1");
        assertThat(of(4_503_599_627_370_497d).minus(of(4_503_599_627_370_496d)).getUncertainty()).isEqualTo("1.4142135623730951");


    }

    @Provide
    public Arbitrary<BigDecimalElement> elements() {
        return Arbitraries.randomValue((random) -> of(random.nextDouble())).injectDuplicates(0.1);
    }
}

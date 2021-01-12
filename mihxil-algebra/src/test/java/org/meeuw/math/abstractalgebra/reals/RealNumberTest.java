package org.meeuw.math.abstractalgebra.reals;

import lombok.extern.log4j.Log4j2;
import net.jqwik.api.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.test.CompleteFieldTheory;
import org.meeuw.math.abstractalgebra.test.MetricSpaceTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.meeuw.math.abstractalgebra.reals.RealNumber.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
@Log4j2
class RealNumberTest implements
    CompleteFieldTheory<RealNumber>,
    MetricSpaceTheory<RealNumber, RealNumber> {

    @Test
    public void test() {
        assertThatThrownBy(() -> new RealNumber(1, 0).of(1, -1)).isInstanceOf(IllegalArgumentException.class);
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

    @Test
    public void considerMultiplicationByZero() {
        RealNumber nan = new RealNumber(Double.NaN, 1d);
        RealNumber zero = new RealNumber(0, 1d);

        assertThat(RealField.INSTANCE.considerMultiplicationBySpecialValues(nan, zero).getValue()).isEqualTo(Double.valueOf(Double.NaN));
        assertThat(RealField.INSTANCE.considerMultiplicationBySpecialValues(zero, nan).getValue()).isEqualTo(Double.valueOf(Double.NaN));

        assertThat(new RealNumber(5, 1).times(RealNumber.ZERO)).isEqualTo(RealNumber.ZERO);
        assertThat(new RealNumber(5, 1).times(new RealNumber(0, 1)).getValue()).isEqualTo(0);
        assertThat(new RealNumber(5, 1).times(new RealNumber(0, 1)).getUncertainty()).isEqualTo(1);
        assertThat(new RealNumber(0, 1).times(new RealNumber(0, 1)).getUncertainty()).isEqualTo(4.9E-324);

    }

    @Test
    public void considerMultiplicationByNaN() {
        RealNumber a = new RealNumber(Double.NaN, 1d);
        RealNumber b = new RealNumber(1, 1d);


        assertThat(RealField.INSTANCE.considerMultiplicationBySpecialValues(a, b).getValue()).isEqualTo(Double.valueOf(Double.NaN));
        assertThat(RealField.INSTANCE.considerMultiplicationBySpecialValues(b, a).getValue()).isEqualTo(Double.valueOf(Double.NaN));

        assertThat(new RealNumber(5, 1).times(RealNumber.ZERO)).isEqualTo(RealNumber.ZERO);
    }


    @Override
	@Provide
    public Arbitrary<RealNumber> elements() {
        return
            Arbitraries.randomValue(
                (random) -> of(2000 * random.nextDouble() - 1000))
                .injectDuplicates(0.1)
                .edgeCases(realNumberConfig -> {
                    realNumberConfig.add(RealNumber.of(0));
                    realNumberConfig.add(RealNumber.of(-1));
                    realNumberConfig.add(RealNumber.of(1));
                })
            ;
    }
}

package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.lifecycle.AfterProperty;
import net.jqwik.api.lifecycle.BeforeProperty;

import java.math.*;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.reals.RealField;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.MultiplicativeGroupTheory;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.Matrix3.of;

/**
 * @author Michiel Meeuwissen
 */
class Matrix3Test implements MultiplicativeGroupTheory<Matrix3>, WithScalarTheory<Matrix3, RealNumber> {


    @BeforeProperty
    public void approx() {
        Matrix3Group.INSTANCE.setDoubleEquivalence((v1, v2) -> round(v1) == round(v2));
    }
    @AfterProperty
    public  void shutdown() {
        Matrix3Group.INSTANCE.clearDoubleEquivalence();
    }
    public static double round(double v) {
        if (Double.isNaN(v)){
            return v;
        }
        return BigDecimal.valueOf(v).round(new MathContext(2)).setScale(0, RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue((random) ->
            new RealNumber(random.nextDouble() * 200 - 100, random.nextDouble() * 10))
            .edgeCases(config -> {
                config.add(RealField.INSTANCE.zero());
                config.add(RealField.INSTANCE.one());
                config.add(RealField.INSTANCE.one().times(-1));
            });
    }

    @Test
    void times() {
        Matrix3 example = of(
            1, 2, 4,
            4, 5, 6,
            7, 8, 9
        );
        assertThat(example.timesDouble(2)).isEqualTo(
            new double[] {
                2, 4, 8,
                8, 10, 12,
                14, 16, 18
            }
        );
        assertThat(example.times(example.getStructure().one()))
                .isEqualTo(example);
    }



    @Override
    public Arbitrary<Matrix3> elements() {
        return Arbitraries.of(
            of(
                1, 2, 4,
                4, 5, 6,
                7, 8, 9
            ),
            of(
                4, 2.2, 5,
                0, 1, 6,
                -9, 8, -0.005
            )

        );
    }
}

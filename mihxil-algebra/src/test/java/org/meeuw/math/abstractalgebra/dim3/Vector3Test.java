package org.meeuw.math.abstractalgebra.dim3;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import org.junit.jupiter.api.Test;
import org.meeuw.math.abstractalgebra.reals.RealNumber;
import org.meeuw.math.abstractalgebra.test.WithScalarTheory;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.dim3.Vector3.of;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
class Vector3Test implements
    ElementTheory<Vector3>,
    WithScalarTheory<Vector3, RealNumber> {

    @Override
    public Arbitrary<? extends Vector3> elements() {
        return Arbitraries.randomValue(r ->
            new Vector3(r.nextDouble(), r.nextDouble(), r.nextDouble())
        );
    }

    @Override
    public Arbitrary<RealNumber> scalars() {
        return Arbitraries.randomValue(random ->
            RealNumber.of(random.nextDouble())
        ).edgeCases(c  -> {
            c.add(RealNumber.ONE);
            c.add(RealNumber.ZERO);
        });
    }

    @Test
    void timesMatrix() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.times(Matrix3Group.INSTANCE.one())).isEqualTo(example);
        assertThat(example.times(Matrix3Group.INSTANCE.one().times(2))).isEqualTo(example.times(2));
    }

    @Test
    void timesDouble() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.times(2)).isEqualTo(of(2, 4, 6));
    }

    @Test
    void dividedByDouble() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.dividedBy(2)).isEqualTo(of(0.5, 1, 1.5));
    }

    @SuppressWarnings({"EqualsWithItself", "EqualsBetweenInconvertibleTypes"})
    @Test
    public void stringEqualsHashCode() {
        Vector3 example = of(1, 2, 3);
        assertThat(example.toString()).isEqualTo("(1.0,2.0,3.0)");

        assertThat(example.equals(example)).isTrue();
        assertThat(example.equals("bla")).isFalse();
        assertThat(example.equals(of(3, 2, 1))).isFalse();
        assertThat(example.equals(of(1, 2, 4))).isFalse();
        assertThat(example.equals(of(2, 2, 3))).isFalse();
        assertThat(example.equals(of(1, 2, 3))).isTrue();

        assertThat(example.hashCode()).isEqualTo(of(1, 2, 3).hashCode());

    }
}

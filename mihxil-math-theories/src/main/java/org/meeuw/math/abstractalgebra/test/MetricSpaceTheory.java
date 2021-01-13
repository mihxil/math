package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.Utils;
import org.meeuw.math.abstractalgebra.MetricSpaceElement;
import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.util.test.ElementTheory.ELEMENTS;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MetricSpaceTheory<E extends MetricSpaceElement<E, S>, S extends Scalar<S>> {


    @Property
    default void distanceSymmetry(@ForAll(ELEMENTS) E a, @ForAll(ELEMENTS) E b) {
        assertThat(a.distanceTo(b)).isEqualTo(b.distanceTo(a));
    }

    @Property
    default void identifyOfIndiscernibles(@ForAll(ELEMENTS) E a) {
        assertThat(a.distanceTo(a).isZero()).isTrue();
    }

    @Property
    default void triangleInequality(
        @ForAll(ELEMENTS) E a,
        @ForAll(ELEMENTS) E b,
        @ForAll(ELEMENTS) E c
        ) {
        double distance = a.distanceTo(c).doubleValue();
        assertThat(distance)
            .isLessThanOrEqualTo(a.distanceTo(b).doubleValue() + b.distanceTo(c).doubleValue() + Utils.uncertaintyForDouble(distance));
    }
}

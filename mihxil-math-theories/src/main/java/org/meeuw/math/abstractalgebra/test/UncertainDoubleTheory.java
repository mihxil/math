package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;

import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

public interface UncertainDoubleTheory<E extends UncertainDouble<? extends E>>
    extends ElementTheory<E> {

    @Property
    default void timesDouble(
        @ForAll(ELEMENTS) E e,
        @ForAll("doubles") Double multiplier) {
        assertThat(e.times(multiplier).getValue()).isEqualTo(e.getValue() * multiplier);
    }

    @Provide
    default DoubleArbitrary doubles() {
        return Arbitraries.doubles();
    }
}

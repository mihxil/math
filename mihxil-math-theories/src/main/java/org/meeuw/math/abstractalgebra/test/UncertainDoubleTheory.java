package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.DoubleArbitrary;

import org.assertj.core.data.Percentage;

import org.meeuw.math.uncertainnumbers.UncertainDouble;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

public interface UncertainDoubleTheory<E extends UncertainDouble<? extends E>>
    extends ElementTheory<E> {

    @Property
    default void timesDouble(
        @ForAll(ELEMENTS) E e,
        @ForAll("doubles") Double multiplier) {
        assertThat(e.times(multiplier).getValue()).isCloseTo(e.getValue() * multiplier, Percentage.withPercentage(0.001));
    }

    @Provide
    default DoubleArbitrary doubles() {
        return Arbitraries.doubles();
    }
}

package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.abstractalgebra.test.ElementTheory.ELEMENTS;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface WithScalarTheory<E extends WithScalarOperations<E, S> & AlgebraicElement<E>,
    S extends ScalarFieldElement<S>> {


    String SCALARS = "scalars";

    @Property
    default void times(@ForAll(ELEMENTS) E e, @ForAll(SCALARS) S scalar) {
        assertThat(e.times(scalar)).isNotNull();
        assertThat(e.times(scalar.sqr())).isEqualTo(e.times(scalar).times(scalar));
    }

    @Property
    default void dividedBy(@ForAll(ELEMENTS) E e, @ForAll(SCALARS) S scalar) {
        try {
            assertThat(e.dividedBy(scalar)).isNotNull();
        } catch (ArithmeticException ae) {
            //
        }
    }

    @Provide
    Arbitrary<S> scalars();



}

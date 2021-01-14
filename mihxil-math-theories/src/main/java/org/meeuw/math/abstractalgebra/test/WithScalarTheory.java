package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.WithScalarOperations;
import org.meeuw.util.test.ElementTheory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface WithScalarTheory<
    E extends WithScalarOperations<E, S>,
    S> extends ElementTheory<E> {


    String SCALARS = "scalars";

    @Property
    default void times(@ForAll(ELEMENTS) E e, @ForAll(SCALARS) S scalar) {
        assertThat(e.times(scalar)).isNotNull();

        try {
            assertThat(e.times(scalar).dividedBy(scalar)).isEqualTo(e);
        } catch (ArithmeticException ae) {
            getLogger().info("{} * {} / {} -> {}", e, scalar, scalar, ae.getMessage());
        }
        //assertThat(e.times(scalar.sqr())).isEqualTo(e.times(scalar).times(scalar));
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

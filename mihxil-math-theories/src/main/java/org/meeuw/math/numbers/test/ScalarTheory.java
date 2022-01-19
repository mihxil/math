package org.meeuw.math.numbers.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.numbers.Scalar;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface ScalarTheory<S extends Scalar<S>>
    extends SizeableScalarTheory<S, S> {


    @Property
    default void implementsScalar(@ForAll(ELEMENTS) S e1) {
        assertThat(e1).isInstanceOf(Scalar.class);
        assertThat(e1.abs()).isInstanceOf(Scalar.class);
    }

    @Property
    default void absSignum(@ForAll(ELEMENT) S e) {
        assertThat(e.abs().signum()).isIn(0, 1);
    }


}

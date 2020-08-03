package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RingTheory<F extends RingElement<F>> extends AdditiveGroupTheory<F>, RngTheory<F> {



    @Property
    default void one(
        @ForAll("elements") F v) {
        assertThat(v.times(v.structure().one())).isEqualTo(v);
    }
}

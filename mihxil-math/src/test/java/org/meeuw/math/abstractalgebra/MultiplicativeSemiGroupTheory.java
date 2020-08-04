package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupTheory<E extends MultiplicativeSemiGroupElement<E>> extends AlgebraicStructureTheory<E> {

    @Property
    default void multiplicativeSemiGroupOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        assertThat(s.supportedOperators()).contains(Operator.MULTIPLICATION);
    }


    @Property
    default void multiplicativeCommutativity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {
        assertThat(v1.times(v2)).isEqualTo(v2.times(v1));
    }


    @Property
    default void multiplicativeAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.times(v2)).times(v3)).isEqualTo(v1.times((v2.times(v3))));
    }

}

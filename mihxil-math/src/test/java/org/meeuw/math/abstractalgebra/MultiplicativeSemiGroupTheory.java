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
        assertThat(s.getSupportedOperators()).contains(Operator.MULTIPLICATION);
    }


    @Property
    default void multiplicativeAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.times(v2)).times(v3)).isEqualTo(v1.times((v2.times(v3))));
    }

    @Property
    default void powPositiveExponents(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(1)).isEqualTo(v1);
        assertThat(v1.pow(2)).isEqualTo(v1.times(v1));
        assertThat(v1.pow(3)).isEqualTo(v1.times(v1).times(v1));
    }

}

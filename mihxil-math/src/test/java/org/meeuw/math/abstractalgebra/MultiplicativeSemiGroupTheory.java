package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupTheory<F extends MultiplicativeSemiGroupElement<F>> extends AlgebraicStructureTheory<F> {

    @Property
    default void multiplicativeSemiGroupOperators(@ForAll("element") F v1) {
        assertThat(v1.structure().supportedOperators()).contains(Operator.MULTIPLICATION);
    }


    @Property
    default void multiplicativeCommutativity (
        @ForAll("elements") F v1,
        @ForAll("elements") F v2) {
        assertThat(v1.times(v2)).isEqualTo(v2.times(v1));
    }


    @Property
    default void multiplicativeAssociativity (
            @ForAll("elements") F v1,
            @ForAll("elements") F v2,
            @ForAll("elements") F v3
            ) {
        assertThat((v1.times(v2)).times(v3)).isEqualTo(v1.times((v2.times(v3))));
    }

}

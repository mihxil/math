package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupTheory<F extends AdditiveGroupElement<F>> extends AlgebraicStructureTheory<F> {

    @Property
    default void additiveGroupOperators(@ForAll(ELEMENT) F v1) {
        assertThat(v1.structure().supportedOperators()).contains(Operator.ADDITION, Operator.SUBTRACTION);
    }

    @Property
    default void minus(
            @ForAll(ELEMENTS) F v1,
            @ForAll(ELEMENTS) F v2) {
        assertThat(v1.minus(v2)).isEqualTo(v1.plus(v2.negation()));
    }

    @Property
    default void additiveCommutativity (
            @ForAll(ELEMENTS) F v1,
            @ForAll(ELEMENTS) F v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }

    @Property
    default void additiveAssociativity (
            @ForAll(ELEMENTS) F v1,
            @ForAll(ELEMENTS) F v2,
            @ForAll(ELEMENTS) F v3
            ) {
        assertThat((v1.plus(v2)).plus(v3)).isEqualTo(v1.plus((v2.plus(v3))));
    }


    @Property
    default void zero(@ForAll("elements") F v) {
        assertThat(v.plus(v.structure().zero())).isEqualTo(v);
    }

}

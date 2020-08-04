package org.meeuw.math.abstractalgebra;

import net.jqwik.api.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupTheory<E extends AdditiveGroupElement<E>> extends AlgebraicStructureTheory<E> {

    @Property
    default void additiveGroupOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        assertThat(s.supportedOperators()).contains(Operator.ADDITION, Operator.SUBTRACTION);
    }

    @Property
    default void minus(
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.minus(v2)).isEqualTo(v1.plus(v2.negation()));
    }

    @Property
    default void additiveCommutativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.plus(v2)).isEqualTo(v2.plus(v1));
    }

    @Property
    default void additiveAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.plus(v2)).plus(v3)).isEqualTo(v1.plus((v2.plus(v3))));
    }


    @Property
    default void zero(@ForAll("elements") E v) {
        assertThat(v.plus(v.structure().zero())).isEqualTo(v);
    }

}

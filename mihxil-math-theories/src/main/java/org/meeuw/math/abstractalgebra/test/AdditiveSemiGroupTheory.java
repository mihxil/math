package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveSemiGroupTheory<E extends AdditiveSemiGroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void additiveSemiGroupOperators(@ForAll(STRUCTURE) AdditiveSemiGroup<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.ADDITION);
    }

    @Property
    default void additiveAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.plus(v2)).plus(v3)).isEqualTo(v1.plus((v2.plus(v3))));
    }

}

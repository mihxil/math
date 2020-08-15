package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldTheory<E extends FieldElement<E>> extends MultiplicativeAbelianGroupTheory<E>, AdditiveGroupTheory<E>  {

    @Property
    default void fieldOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        assertThat(s.getSupportedOperators()).contains(Operator.values());
    }

    @Property
    default void distributivity (
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2,
        @ForAll(ELEMENTS) E v3
    ) {
        assertThat(v1.times(v2.plus(v3))).isEqualTo(v1.times(v2).plus(v1.times(v3)));
    }

}

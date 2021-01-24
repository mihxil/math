package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface AdditiveGroupTheory<E extends AdditiveGroupElement<E>>
    extends AdditiveMonoidTheory<E> {

    @Property
    default void additiveGroupOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        assertThat(s.getSupportedOperators()).contains(Operator.ADDITION, Operator.SUBTRACTION);
    }

    @Property
    default void minus(
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2) {
        assertThat(v1.minus(v2)).isEqualTo(v1.plus(v2.negation()));
    }


    @Property
    default void repeatedPlus(
            @ForAll(ELEMENTS) E v1
            ) {
        assertThat((v1.repeatedPlus(0))).isEqualTo(v1.getStructure().zero());
        assertThat((v1.repeatedPlus(5))).isEqualTo(v1.repeatedPlus(3).plus(v1.repeatedPlus(2)));
        assertThat((v1.repeatedPlus(-5))).isEqualTo(v1.repeatedPlus(5).negation());
    }


}

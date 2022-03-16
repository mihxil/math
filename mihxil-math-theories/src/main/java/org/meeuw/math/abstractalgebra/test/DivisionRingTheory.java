package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.operators.BasicAlgebraicBinaryOperator.*;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.NEGATION;
import static org.meeuw.math.operators.BasicAlgebraicUnaryOperator.SQR;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisionRingTheory<E extends DivisionRingElement<E>> extends
    MultiplicativeGroupTheory<E>,
    AdditiveGroupTheory<E>  {

    @Property
    default void fieldOperators(@ForAll(STRUCTURE) AlgebraicStructure<E> s) {
        assertThat(s.getSupportedOperators()).contains(ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION);
    }

    @Property
    default void operatorEnums(
        @ForAll(ELEMENTS) E e1,
        @ForAll(ELEMENTS) E e2) {
        assertThat(MULTIPLICATION.andThen(NEGATION).apply(e1, e2)).isEqualTo(e1.times(e2).negation());
        assertThat(ADDITION.andThen(SQR.compose(NEGATION)).apply(e1, e2)).isEqualTo((e1.plus(e2).negation()).sqr());
        assertThat(ADDITION.andThen(SQR.andThen(NEGATION)).apply(e1, e2)).isEqualTo((e1.plus(e2).sqr()).negation());

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

package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.ReciprocalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeSemiGroupTheory<E extends MultiplicativeSemiGroupElement<E>>
    extends AlgebraicStructureTheory<E> {

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
    default void pow1(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(1)).isEqualTo(v1);
    }

    @Property
    default void pow2(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(2)).isEqualTo(v1.times(v1));
    }

    @Property
    default void pow3(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(3)).isEqualTo(v1.times(v1).times(v1));
    }


    @Property
    default void pow0(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(0)).isInstanceOf(ReciprocalException.class);
    }

    @Property
    @Label("powNegative1 semigroup")
    default void powNegative1(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(-1)).isInstanceOf(ReciprocalException.class);
    }
    @Property
    default void powNegative2(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(-2)).isInstanceOf(ReciprocalException.class);

    }
    @Property
    default void powNegative3(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThatThrownBy(() -> v1.pow(-3)).isInstanceOf(ReciprocalException.class);
    }

    @Property
    default void sqr(@ForAll(ELEMENTS) E v) {
        assertThat(v.sqr()).isNotNull().isEqualTo(v.times(v));
    }


}

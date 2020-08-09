package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupTheory<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoidTheory<E> {

    @Property
    default void division(
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {

        assertThat(v1.dividedBy(v2)).isEqualTo(v1.times(v2.reciprocal()));
    }
    @Property
    default void one(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.times(v.getStructure().one())).isEqualTo(v);
    }

    @Property
    default void powNegativeExponents(
         @ForAll(ELEMENTS) E v1
    )  {
        assertThat(v1.pow(-1)).isEqualTo(v1.reciprocal());
        assertThat(v1.pow(-2)).isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1)));
        assertThat(v1.pow(-3)).isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1).times(v1)));
    }


    @Property
    default void reciprocal(
         @ForAll(ELEMENTS) E e
    )  {
        assertThat(e.reciprocal().reciprocal()).isEqualTo(e);
        assertThat(e.reciprocal().times(e)).isEqualTo(e.getStructure().one());


    }


}

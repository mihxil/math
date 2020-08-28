package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeGroupTheory<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeMonoidTheory<E> {

    @Property
    default void multiplicativeGroupOperators(@ForAll(STRUCTURE) MultiplicativeGroup<?> group) {
        assertThat(group.getSupportedOperators()).contains(Operator.MULTIPLICATION, Operator.DIVISION);
    }

    @Property
    default void division(
        @ForAll(ELEMENTS) E v1,
        @ForAll(ELEMENTS) E v2) {

        try {
            assertThat(v1.dividedBy(v2)).isEqualTo(v1.times(v2.reciprocal()));
        } catch (ArithmeticException ae) {
            getLogger().info(v1 + " / " + v2 + ": " + ae.getMessage());
        }
    }

    @Property
    default void powNegativeExponents(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-1)).isEqualTo(v1.reciprocal());
            assertThat(v1.pow(-2)).isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1)));
            assertThat(v1.pow(-3)).isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1).times(v1)));
        } catch (ArithmeticException ae) {
            getLogger().warn("Negative power of " + v1 + ": " + ae.getMessage());
        }
    }


    @Property(shrinking = ShrinkingMode.OFF)
    default void reciprocal(
         @ForAll(ELEMENTS) E e
    )  {
        try {
            assertThat(e.reciprocal().reciprocal()).isEqualTo(e);
            assertThat(e.reciprocal().times(e)).isEqualTo(e.getStructure().one());
        } catch (ArithmeticException ae) {
            // The element may be zero
            getLogger().warn("{}: {} = zero?", ae.getMessage(), e);
        }
    }




}

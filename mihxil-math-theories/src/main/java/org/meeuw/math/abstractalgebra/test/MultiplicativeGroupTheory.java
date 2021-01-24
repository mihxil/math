package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.DivisionByZeroException;
import org.meeuw.math.exceptions.ReciprocalException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;

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
        } catch (ReciprocalException ae) {
            getLogger().info(v1 + " / " + v2 + ": " + ae.getMessage());
        }
    }

    @Override
    @Property
    @Label("powNegative1 group")
    default void powNegative1(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-1).equals(v1.reciprocal())).isTrue();
        } catch (DivisionByZeroException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-1) + ": " + ae.getMessage());
        }
    }
    @Override
    @Property
    default void powNegative2(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-2)).usingDefaultComparator().isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1)));
        } catch (DivisionByZeroException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-2) + ": " + ae.getMessage());
        }
    }
    @Override
    @Property
    default void powNegative3(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(-3)).isEqualTo(v1.getStructure().one().dividedBy(v1.times(v1).times(v1)));
        } catch (DivisionByZeroException ae) {
            getLogger().warn("Negative power of " + v1 + superscript(-3) + ": " + ae.getMessage());
        }
    }

    @Property(shrinking = ShrinkingMode.OFF)
    default void reciprocal(
         @ForAll(ELEMENTS) E e
    )  {
        try {
            assertThat(e.reciprocal().reciprocal()).isEqualTo(e);
            assertThat(e.reciprocal().times(e)).isEqualTo(e.getStructure().one());
        } catch (ReciprocalException ae) {
            // The element may be zero
            getLogger().warn("{}: {} = zero?", ae.getMessage(), e);
        }
    }




}

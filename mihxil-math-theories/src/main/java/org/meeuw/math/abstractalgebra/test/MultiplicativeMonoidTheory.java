package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.*;

import org.meeuw.math.abstractalgebra.MultiplicativeMonoidElement;
import org.meeuw.math.exceptions.DivisionByZeroException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.meeuw.math.text.TextUtils.superscript;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeMonoidTheory<E extends MultiplicativeMonoidElement<E>>
    extends MultiplicativeSemiGroupTheory<E> {

    @Property
    default void one(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.times(v.getStructure().one())).isEqualTo(v);
    }

    @Override
    @Property
    default void pow0(
         @ForAll(ELEMENTS) E v1
    )  {
        try {
            assertThat(v1.pow(0)).isEqualTo(v1.getStructure().one());
        } catch (DivisionByZeroException ae){
            getLogger().warn("" + v1 + superscript(0) + ": " + ae.getMessage());
        }
    }

    @Property
    default void isOne(
         @ForAll(ELEMENTS) E v1,
         @ForAll(ELEMENTS) E v2
    )  {
        if (v1.isOne()) {
            assertThat(v2.times(v1)).isEqualTo(v2);
            assertThat(v1.times(v2)).isEqualTo(v2);
        }
        if (v2.isOne()) {
            assertThat(v1.times(v2)).isEqualTo(v1);
            assertThat(v2.times(v1)).isEqualTo(v1);
        }


    }
}

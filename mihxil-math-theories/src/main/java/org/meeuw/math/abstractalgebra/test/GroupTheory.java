package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.exceptions.InverseException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface GroupTheory<E extends GroupElement<E>>
    extends MagmaTheory<E> {

    @Property
    default void groupOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedOperators()).contains(Operator.OPERATION);
    }

    @Property
    default void groupUnitaryOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedUnaryOperators()).contains(UnaryOperator.INVERSION);
    }


    @Property
    default void operateAssociativity (
            @ForAll(ELEMENTS) E v1,
            @ForAll(ELEMENTS) E v2,
            @ForAll(ELEMENTS) E v3
            ) {
        assertThat((v1.operate(v2)).operate(v3)).isEqualTo(v1.operate((v2.operate(v3))));
    }

    @Property
    default void unity(
        @ForAll(ELEMENTS) E v) {
        assertThat(v.operate(v.getStructure().unity()).equals(v)).isTrue();
    }


    @Property
    default void inverse(
        @ForAll(ELEMENTS) E v) {
        try {
            assertThat(v.inverse().operate(v).equals(v.getStructure().unity())).isTrue();
        } catch (InverseException ie) {
            getLogger().info(ie.getMessage());
        }
    }


}

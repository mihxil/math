package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

import org.meeuw.math.operators.BasicAlgebraicBinaryOperator;

/**
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface MagmaTheory<E extends MagmaElement<E>>
    extends AlgebraicStructureTheory<E> {

    @Property
    default void magmaOperators(@ForAll(STRUCTURE) Magma<E> s) {
        assertThat(s.getSupportedOperators()).contains(BasicAlgebraicBinaryOperator.OPERATION);
    }
}

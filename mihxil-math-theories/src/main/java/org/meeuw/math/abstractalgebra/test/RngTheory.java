package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.Operator;
import org.meeuw.math.abstractalgebra.RngElement;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RngTheory<E extends RngElement<E>> extends AdditiveAbelianGroupTheory<E>, MultiplicativeSemiGroupTheory<E> {

    @Property
    default void rngOperators(@ForAll(ELEMENT) E v1) {
        assertThat(v1.getStructure().getSupportedOperators()).contains(Operator.MULTIPLICATION, Operator.ADDITION, Operator.SUBTRACTION);
    }
}

package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RngTheory<E extends RngElement<E>> extends AdditiveGroupTheory<E>, MultiplicativeSemiGroupTheory<E> {

    @Property
    default void rngOperators(@ForAll(ELEMENT) E v1) {
        assertThat(v1.getStructure().getSupportedOperators()).contains(Operator.MULTIPLICATION, Operator.ADDITION, Operator.SUBTRACTION);
    }
}

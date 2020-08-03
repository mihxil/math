package org.meeuw.math.abstractalgebra;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface RngTheory<F extends RngElement<F>> extends AdditiveGroupTheory<F>, MultiplicativeSemiGroupTheory<F> {

    @Property
    default void rngOperators(@ForAll("elements") F v1) {
        assertThat(v1.structure().supportedOperators()).contains(Operator.MULTIPLICATION, Operator.ADDITION, Operator.SUBTRACTION);
    }
}

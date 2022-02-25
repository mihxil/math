package org.meeuw.math.abstractalgebra.test;

import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

import org.meeuw.math.abstractalgebra.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface GroupTheory<E extends GroupElement<E>>
    extends AlgebraicStructureTheory<E> {

    @Property
    default void groupOperators(@ForAll(STRUCTURE) Group<E> s) {
        assertThat(s.getSupportedOperators()).contains(Operator.OPERATE);
    }


}

package org.meeuw.math.abstractalgebra.test;

import org.meeuw.math.abstractalgebra.FieldElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface FieldTheory<E extends FieldElement<E>> extends
    DivisibleGroupTheory<E>,
    DivisionRingTheory<E>,
    GroupTheory<E> {

}

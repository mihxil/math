package org.meeuw.math.abstractalgebra;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface Group<F extends GroupElement<F, A>, A extends Group<F, A>> extends AlgebraicStructure<F> {

    F one();

}

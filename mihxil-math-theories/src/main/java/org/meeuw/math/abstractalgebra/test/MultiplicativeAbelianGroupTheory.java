package org.meeuw.math.abstractalgebra.test;

import org.meeuw.math.abstractalgebra.MultiplicativeGroupElement;

/**
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface MultiplicativeAbelianGroupTheory<E extends MultiplicativeGroupElement<E>>
    extends
    MultiplicativeGroupTheory<E>,
    MultiplicativeAbelianSemiGroupTheory<E> {

}

package org.meeuw.math.abstractalgebra;

/**
 * A general group , with one operation, and a 'unity' element.
 *
 * @see MultiplicativeGroup For a group where the operation is explicitely called 'multiplication'
 * @see AdditiveGroup       For a group where the operation is 'addition'.
 * @since 0.8
 */
public interface Group<E extends GroupElement<E>> extends Magma<E> {

    E unity();

}

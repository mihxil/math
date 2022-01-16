package org.meeuw.math.abstractalgebra;

/**
 * An element for the algebraic 'group' (where the operation is multiplication)
 *
 * @author Michiel Meeuwissen
 * @since 0.8
 */
public interface DivisibleGroupElement<E extends DivisibleGroupElement<E>>
    extends MultiplicativeGroupElement<E> {

    @Override
    DivisibleGroup<E> getStructure();

    E dividedBy(long divisor);

    E times(long multiplier);


}

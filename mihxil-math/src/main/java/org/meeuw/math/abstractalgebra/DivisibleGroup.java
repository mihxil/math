package org.meeuw.math.abstractalgebra;

/**
 * Just adds to {@link MultiplicativeAbelianGroup} the concept of division and multiplication by integers.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Divisible_group">Divisible Group</a>
 * @see DivisibleGroupElement#dividedBy(long)
 * @see DivisibleGroupElement#times(long)
 * @author Michiel Meeuwissen
 * @since 0.4
 */
public interface DivisibleGroup<E extends MultiplicativeGroupElement<E>>
    extends MultiplicativeAbelianGroup<E> {


}

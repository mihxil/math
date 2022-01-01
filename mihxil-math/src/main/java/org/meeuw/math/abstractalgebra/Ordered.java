package org.meeuw.math.abstractalgebra;

/**
 *
 *  {@link #lte} and {@link #gte} are transitive too.
 *
 * @author Michiel Meeuwissen
 * @since 0.7
 */
public interface Ordered<E extends Ordered<E>>  extends StrictlyOrdered<E> {


}

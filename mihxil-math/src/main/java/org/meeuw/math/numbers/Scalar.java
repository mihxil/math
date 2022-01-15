package org.meeuw.math.numbers;

/**
 * A scalar is the closest thing to a {@link Number} interface
 *
 * @author Michiel Meeuwissen
 * @since 0.4
 * @param <SELF> self reference
 */
public interface Scalar<SELF extends Scalar<SELF>>
    extends SizeableScalar<SELF, SELF> {

}

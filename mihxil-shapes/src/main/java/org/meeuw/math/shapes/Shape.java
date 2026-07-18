package org.meeuw.math.shapes;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.Uncertain;

/**
 * @param <E> Type of the coordinates
 * @param <C> Completion of that type
 * @param <SELF> Type of the solid itself
 */
public interface Shape<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>, SELF extends Shape<E, C, SELF>>  extends Uncertain {

    /**
     * A completion of the shape itself.
     * @param <S> Type of the completed shape
     */
    <S extends Shape<C, C, S>> S complete();

    ScalarField<E, C> field();

    boolean eq(SELF other);

    SELF times(E multiplier);

    SELF times(int multiplier);

    SELF times(double multiplier);

    @Override
    default boolean isExact() {
        return ! field().elementsAreUncertain();
    }

    default Stream<Info> info() {
        return  Stream.empty();
    }
}

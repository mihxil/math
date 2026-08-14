package org.meeuw.math.shapes;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.*;
import org.meeuw.math.uncertainnumbers.Uncertain;

/**
 * @param <E> Type of the coordinates
 * @param <C> Completion of that type
 * @param <SELF> Type of the shape itself
 * @param <CSELF> Type of the completed shape itself
 */
public interface Shape<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>,
    SELF extends Shape<E, C, SELF, CSELF>,
    CSELF extends Shape<C, C, CSELF, CSELF>>  extends Uncertain {

    /**
     * A completion of the shape itself.
     */
    CSELF complete();

    ScalarField<E, C> field();

    boolean eq(SELF other);

    SELF times(E multiplier);

    default CSELF timesc(C multiplier) {
        return complete().times(multiplier);
    }


    SELF times(int multiplier);

    SELF times(double multiplier);

    @Override
    default boolean isExact() {
        return ! field().elementsAreUncertain();
    }

    default Stream<Info> info() {
        return Stream.empty();
    }
}

package org.meeuw.math.shapes.dim2;

import java.util.function.Supplier;
import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.exceptions.FieldIncompleteException;
import org.meeuw.math.uncertainnumbers.Uncertain;

public interface Shape<E extends ScalarFieldElement<E>, SELF extends Shape<E, SELF>> extends Uncertain {

    E perimeter();

    E area();

    @Override
    default boolean isExact() {
        return ! field().elementsAreUncertain();
    }


    default String info(Supplier<Object> value) {
        try {
            return value.get().toString();
        } catch (FieldIncompleteException e) {
            return e.getMessage();
        }
    }

    default Stream<String[]> info() {
        return Stream.of(
            new String[]{"area", info(this::area)},
            new String[]{"perimeter", info(this::perimeter)}
        );
    }

    /**
     * Returns a {@link LocatedShape located} (unrotated) rectangle that precisely contains this shape (after rotation by the given angle (in radians)).
     */
    LocatedShape<E, Rectangle<E>> circumscribedRectangle();


    /**
     * Returns a {@link LocatedShape located} circle that precisely contains this shape.
     */
    LocatedShape<E, Circle<E>> circumscribedCircle();

    ScalarField<E> field();

    boolean eq(SELF other);

    SELF times(E multiplier);

    SELF times(int multiplier);

    SELF times(double multiplier);

    SELF rotate(E angle);

}

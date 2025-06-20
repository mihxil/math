package org.meeuw.math.shapes.dim2;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.Info;
import org.meeuw.math.uncertainnumbers.Uncertain;

public interface Shape<E extends ScalarFieldElement<E>, SELF extends Shape<E, SELF>> extends Uncertain {

    E perimeter();

    E area();

    @Override
    default boolean isExact() {
        return ! field().elementsAreUncertain();
    }


    default Stream<Info> info() {
        return Stream.of(
            new Info(Info.Key.AREA, this::area),
            new Info(Info.Key.PERIMETER, this::perimeter),
            new Info(Info.Key.CIRCUMSCRIBED_RECTANGLE, this::circumscribedRectangle),
            new Info(Info.Key.CIRCUMSCRIBED_CIRCLE, this::circumscribedCircle)
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

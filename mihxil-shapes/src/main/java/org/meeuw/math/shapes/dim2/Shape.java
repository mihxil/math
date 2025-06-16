package org.meeuw.math.shapes.dim2;

import org.checkerframework.checker.units.qual.radians;
import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Shape<E extends ScalarFieldElement<E>, SELF extends Shape<E, SELF>>  {

    E perimeter();

    E area();

    /**
     * Returns a {@link LocatedShape located} rectangle that precisely contains this shape (after rotation by the given angle (in radians)).
     */
    LocatedShape<E, Rectangle<E>> circumscribedRectangle(@radians E angle);

    /**
     * Returns a {@link LocatedShape located} circle that precisely contains this shape.
     */
    LocatedShape<E, Circle<E>> circumscribedCircle();

    ScalarField<E> field();

    boolean eq(SELF other);

    SELF times(E multiplier);

    SELF times(int multiplier);

    SELF times(double multiplier);

}

package org.meeuw.math.shapes.dim2;

import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Shape<E extends ScalarFieldElement<E>, SELF extends Shape<E, SELF>>  {

    E perimeter();

    E area();

    LocatedShape<E, Rectangle<E>> circumscribedRectangle(E angle);

    LocatedShape<E, Circle<E>> circumscribedCircle();

    ScalarField<E> field();

    boolean eq(SELF other);

    SELF times(E multiplier);

    SELF times(int multiplier);

    SELF times(double multiplier);

}

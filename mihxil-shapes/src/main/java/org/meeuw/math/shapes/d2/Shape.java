package org.meeuw.math.shapes.d2;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

public interface Shape<F extends CompleteScalarFieldElement<F>, SELF extends Shape<F, SELF>>  {

    F perimeter();

    F area();

    Rectangle<F> circumscribedRectangle(F angle);

    Circle<F> circumscribedCircle();

    boolean eq(SELF other);

}

package org.meeuw.math.shapes.dim2;

import org.meeuw.math.abstractalgebra.ScalarField;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Shape<F extends ScalarFieldElement<F>, SELF extends Shape<F, SELF>>  {

    F perimeter();

    F area();

    LocatedShape<F, Rectangle<F>> circumscribedRectangle(F angle);

    LocatedShape<F, Circle<F>> circumscribedCircle();

    ScalarField<F> field();

    boolean eq(SELF other);

    SELF times(F multiplier);

    SELF times(int multiplier);

}

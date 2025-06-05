package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;

public interface Volume <F extends CompleteScalarFieldElement<F>, SELF extends Volume<F, SELF>>  {

    F volume();

    F surfaceArea();

    boolean eq(SELF other);
}

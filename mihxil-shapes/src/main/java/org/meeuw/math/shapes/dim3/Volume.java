package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Volume <F extends ScalarFieldElement<F>, SELF extends Volume<F, SELF>>  {

    F volume();

    F surfaceArea();

    boolean eq(SELF other);
}

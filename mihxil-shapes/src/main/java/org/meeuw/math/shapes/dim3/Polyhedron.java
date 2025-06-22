package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Polyhedron<F extends ScalarFieldElement<F>, SELF extends Polyhedron<F, SELF>> extends Volume<F, SELF> {

    /**
     * Returns the number of vertices of this polyhedron.
     */
    int vertices();

    /**
     * Returns the number of edges of this polyhedron.
     */
    int edges();

    /**
     * Returns the number of faces of this polyhedron.
     */
    int faces();

    default int eulerCharacteristic() {
        return vertices() - edges() + faces();
    }

    /**
     * Returns the number of holes in this polyhedron.
     */
    default int holes() {
        return 0;
    }

    @Override
    boolean eq(SELF other);

    @Override
    String toString();
}

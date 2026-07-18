package org.meeuw.math.shapes.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;

public interface Polyhedron<F extends ScalarFieldElement<F, C>, C extends CompleteScalarFieldElement<C>,  SELF extends Polyhedron<F, C, SELF>> extends Solid<F, C, SELF> {

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


    @Override
    boolean eq(SELF other);

    @Override
    String toString();
}

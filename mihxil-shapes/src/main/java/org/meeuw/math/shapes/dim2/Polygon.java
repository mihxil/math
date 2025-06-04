package org.meeuw.math.shapes.dim2;

import java.util.stream.Stream;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.dim2.FieldVector2;

public interface Polygon<F extends CompleteScalarFieldElement<F>, SELF extends Shape<F, SELF>> extends Shape<F, SELF>   {

    int numberOfEdges();
    default int numberOfVertices() {
        return numberOfEdges();
    }

    Stream<FieldVector2<F>> vertices();

}

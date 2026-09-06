package org.meeuw.math.svg.dim3;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.dim3.RectangularCuboid;
import org.meeuw.math.svg.SVGDocument;
import org.meeuw.math.svg.SVGGroup;
import org.w3c.dom.Element;

@lombok.AllArgsConstructor
@lombok.Builder
public class SVGCuboidGrid<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>> implements SVGGroup<E, C> {


    private final RectangularCuboid<E, C> spacing;


    @Override
    public void fill(SVGDocument<E, C> svgDocument, Element g) {

    }
}

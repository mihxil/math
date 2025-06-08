package org.meeuw.math.svg;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.shapes.dim2.Rectangle;

public class SVGRectangle<F extends CompleteScalarFieldElement<F>> extends SVGPolygon<F,  Rectangle<F>> {

    @lombok.Builder(builderMethodName = "rectangleBuilder")
    public SVGRectangle(Rectangle<F> rectangle, boolean circumscribedCircle) {
        super(rectangle, circumscribedCircle);
    }

    @Override
    public void fillShape(SVGDocument svgDocument, org.w3c.dom.Element g) {
        super.fillShape(svgDocument, g);
        // No additional shapes to fill for a rectangle
    }
}

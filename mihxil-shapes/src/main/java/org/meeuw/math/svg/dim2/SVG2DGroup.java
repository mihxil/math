package org.meeuw.math.svg.dim2;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.svg.SVGDocument;
import org.meeuw.math.svg.SVGGroup;
import org.w3c.dom.Element;

public interface SVG2DGroup<E extends ScalarFieldElement<E, C>,
C extends CompleteScalarFieldElement<C>> extends SVGGroup<E, C> {

    default void fill(SVGDocument<E, C> svgDocument, Element g) {
        if (svgDocument instanceof SVGFiguresDocument<E, C> figuresDocument) {
            fill(figuresDocument, g);
        } else {

        }
    }

    void fill(SVGFiguresDocument<E, C> svgDocument, Element g);
}

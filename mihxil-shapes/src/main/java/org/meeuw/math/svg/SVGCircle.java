package org.meeuw.math.svg;

import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.dim2.Circle;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;


public class SVGCircle<F extends ScalarFieldElement<F>> extends SVGShape<Circle<F>> {


    @lombok.Builder
    private SVGCircle(Circle<F> circle, boolean circumscribedRectangle, Consumer<Element> circumscribedRectangleAttributes) {
        super(circle, false, circumscribedRectangle, null, circumscribedRectangleAttributes);
    }

    @Override
    public void fillShape(SVGDocument svgDocument, Element g) {
        Element circleElement = createElement(g.getOwnerDocument(), "circle");
        circleElement.setAttribute("stroke", svgDocument.stroke());
        circleElement.setAttribute("stroke-width", "1");
        circleElement.setAttribute("fill", "none");

        circleElement.setAttribute("r", String.valueOf(shape.radius().doubleValue()));

        g.appendChild(circleElement);

        //g.appendChild(info(doc, circle.times(0.1)));

    }
}

package org.meeuw.math.svg.dim2;

import java.util.function.Consumer;

import org.meeuw.math.abstractalgebra.CompleteScalarFieldElement;
import org.meeuw.math.abstractalgebra.ScalarFieldElement;
import org.meeuw.math.shapes.dim2.Circle;
import org.w3c.dom.Element;

import static org.meeuw.math.svg.SVG.createElement;


public class SVGCircle<
    E extends ScalarFieldElement<E, C>,
    C extends CompleteScalarFieldElement<C>>
    extends SVGFigure<E, C, Circle<E, C>> {


    @lombok.Builder
    private SVGCircle(Circle<E, C> circle, boolean circumscribedRectangle, Consumer<Element> circumscribedRectangleAttributes) {
        super(circle, false, circumscribedRectangle, null, circumscribedRectangleAttributes);
    }

    @Override
    public void fillShape(SVGFiguresDocument<E, C> svgDocument, Element g) {
        Element circleElement = createElement(g.getOwnerDocument(), "circle");
        circleElement.setAttribute("stroke", svgDocument.stroke());
        circleElement.setAttribute("stroke-width", "1");
        circleElement.setAttribute("fill", "none");

        circleElement.setAttribute("r", String.valueOf(shape.radius().doubleValue()));

        g.appendChild(circleElement);

        //g.appendChild(info(doc, circle.times(0.1)));

    }
}
